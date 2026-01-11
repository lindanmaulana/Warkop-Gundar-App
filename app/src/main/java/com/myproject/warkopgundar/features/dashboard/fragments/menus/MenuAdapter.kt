package com.myproject.warkopgundar.features.dashboard.fragments.menus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.myproject.warkopgundar.R
import com.myproject.warkopgundar.db.Menu
import com.myproject.warkopgundar.utils.toParseCurrency

class MenuAdapter(private val onItemClick: (Menu) -> Unit, private val onAddClick: (Menu) -> Unit) : ListAdapter<Menu, RecyclerView.ViewHolder>(DiffCallback()) {
    private val TYPE_GRID = 1
    private val TYPE_LIST = 2
    var isGridView: Boolean = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return if (isGridView) TYPE_GRID else TYPE_LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder = if (viewType == TYPE_GRID) {
            val view = inflater.inflate(R.layout.item_menu_grid, parent, false)
            GridViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_menu_list, parent, false)
            ListViewHolder(view)
        }

        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(getItem(position))
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when(holder) {
            is GridViewHolder -> holder.bind(item, onAddClick)
            is ListViewHolder -> holder.bind(item)
        }

//        holder.itemView.setOnClickListener {
//            onItemClick(item)
//        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Menu>() {
    override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
        return oldItem == newItem
    }
}

class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imgMenu: ShapeableImageView = view.findViewById(R.id.imgMenu)
    val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    val tvSubtitle: TextView = view.findViewById(R.id.tvSubtitle)
    val tvPrice: TextView = view.findViewById(R.id.tvPrice)
    val tvRatingValue: TextView = view.findViewById(R.id.tvRatingValue)
    val actionAddToCart: MaterialButton = view.findViewById(R.id.actionAddToCart)

    fun bind(item: Menu, onAddClick: (Menu) -> Unit) {
        tvTitle.text = item.name
        tvSubtitle.text = item.description
        tvPrice.text = item.price.toParseCurrency()
        Glide.with(itemView.context)
            .load(item.imageRes)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .centerCrop()
            .into(imgMenu)
        tvRatingValue.text = item.rating.toString()
        // Tambahkan logic klik tombol jika perlu

        actionAddToCart.setOnClickListener {
            onAddClick(item)
        }
    }
}

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imgMenu: ShapeableImageView = view.findViewById(R.id.imgMenu)
    val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    val tvPrice: TextView = view.findViewById(R.id.tvPrice)

    fun bind(item: Menu) {
        tvTitle.text = item.name
        tvPrice.text = item.price.toParseCurrency()
        Glide.with(itemView.context)
            .load(item.imageRes)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .centerCrop()
            .into(imgMenu)
        // tvLikes.text = item.likes
    }
}
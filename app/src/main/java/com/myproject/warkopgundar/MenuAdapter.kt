package com.myproject.warkopgundar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView

class MenuAdapter(private val onItemClick: (Menu) -> Unit) : ListAdapter<Menu, RecyclerView.ViewHolder>(DiffCallback()) {
    private val TYPE_GRID = 1
    private val TYPE_LIST = 2
    var isGridView: Boolean = true

    override fun getItemViewType(position: Int): Int {
        return if (isGridView) TYPE_GRID else TYPE_LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_GRID) {
            val view = inflater.inflate(R.layout.item_menu_grid, parent, false)
            GridViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_menu_list, parent, false)
            ListViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when(holder) {
            is GridViewHolder -> holder.bind(item)
            is ListViewHolder -> holder.bind(item)
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
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
    val btnAdd: MaterialButton = view.findViewById(R.id.btnAdd)

    fun bind(item: Menu) {
        tvTitle.text = item.name
        tvSubtitle.text = item.description
        tvPrice.text = "Rp ${item.price}"
        imgMenu.setImageResource(item.imageRes ?: R.drawable.img_placeholder)
        // Tambahkan logic klik tombol jika perlu
    }
}

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imgMenu: ShapeableImageView = view.findViewById(R.id.imgMenu)
    val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    val tvPrice: TextView = view.findViewById(R.id.tvPrice)

    fun bind(item: Menu) {
        tvTitle.text = item.name
        tvPrice.text = "Rp ${item.price}"
        imgMenu.setImageResource(item.imageRes ?: R.drawable.img_placeholder)
        // tvLikes.text = item.likes
    }
}
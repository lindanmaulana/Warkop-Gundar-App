package com.myproject.warkopgundar.features.dashboard.fragments.carts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myproject.warkopgundar.databinding.ItemCartListBinding
import com.myproject.warkopgundar.utils.toParseCurrency

class CartAdapter(
    private val onPlusClick: (CartWithMenu) -> Unit,
    private val onMinClick: (CartWithMenu) -> Unit,
    private val onDeleteClick: (CartWithMenu) -> Unit,
) : ListAdapter<CartWithMenu, CartAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(val binding: ItemCartListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            tvMenuName.text = item.menu.name
            tvPrice.text = item.menu.price.toParseCurrency()
            tvQty.text = item.cart.quantity.toString()

            Glide.with(root).load(item.menu.imageRes).into(imgMenu)

            actionPlus.setOnClickListener { onPlusClick(item) }
            actionMin.setOnClickListener { onMinClick(item) }
            actionDelete.setOnClickListener { onDeleteClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CartWithMenu>() {
        override fun areItemsTheSame(oldItem: CartWithMenu, newItem: CartWithMenu): Boolean {
            return oldItem.cart.id == newItem.cart.id
        }

        override fun areContentsTheSame(oldItem: CartWithMenu, newItem: CartWithMenu): Boolean {
            return oldItem == newItem
        }
    }
}
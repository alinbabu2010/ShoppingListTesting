package com.sample.shoppinglist.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import com.sample.shoppinglist.data.local.ShoppingItem
import com.sample.shoppinglist.databinding.ItemShoppingBinding
import javax.inject.Inject

class ShoppingItemAdapter @Inject constructor(
    private val glide: RequestManager
) : ListAdapter<ShoppingItem, ShoppingItemAdapter.ShoppingItemViewHolder>(ShoppingItemDiffUtil()) {

    class ShoppingItemDiffUtil : DiffUtil.ItemCallback<ShoppingItem>() {

        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem) =
            oldItem == newItem

    }

    class ShoppingItemViewHolder(val binding: ItemShoppingBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ShoppingItemViewHolder(
        ItemShoppingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val shoppingItem = getItem(position)
        holder.binding.apply {
            glide.load(shoppingItem.imageUrl).into(ivShoppingImage)
            tvName.text = shoppingItem.name
            val amountText = "${shoppingItem.amount}x"
            tvShoppingItemAmount.text = amountText
            val priceText = "${shoppingItem.price}₹"
            tvShoppingItemPrice.text = priceText
        }
    }

}
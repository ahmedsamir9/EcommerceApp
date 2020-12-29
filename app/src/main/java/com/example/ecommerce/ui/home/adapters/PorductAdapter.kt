package com.example.ecommerce.ui.home.adapters

import android.media.Image
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ProductItemBinding
import com.example.ecommerce.model.Product

class ProductAdapter(private val interaction: Interaction? = null) :
    ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =ProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding,interaction)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(
        val productItemBinding: ProductItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(productItemBinding.root) {



        fun bind(item: Product) {
            productItemBinding.product =item
            productItemBinding.root.setOnClickListener {
                interaction?.onItemSelected(item,productItemBinding.productImg)
            }
        }
    }

    interface Interaction {

        fun onItemSelected(item: Product,imageView:ImageView)
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }
}
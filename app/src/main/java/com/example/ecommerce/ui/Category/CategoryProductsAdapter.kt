package com.example.ecommerce.ui.Category

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ProductItemBinding
import com.example.ecommerce.model.Product

class CategoryProductsAdapter(private val interaction: Interaction? = null) :
    ListAdapter<Product, CategoryProductsAdapter.CategoryProductsViewHolder>(ProductsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductsViewHolder {
        val binding=ProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryProductsViewHolder(binding ,interaction)
    }

    override fun onBindViewHolder(holder: CategoryProductsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryProductsViewHolder(
       val productItemBinding: ProductItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(productItemBinding.root), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            if (adapterPosition == RecyclerView.NO_POSITION) return

            interaction?.onItemSelected(adapterPosition, getItem(adapterPosition))
        }

        fun bind(item: Product)  {
           productItemBinding.product= item
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: Product)
    }

    private class ProductsDiffCallback : DiffUtil.ItemCallback<Product>() {
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
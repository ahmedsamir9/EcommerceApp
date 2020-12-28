package com.example.ecommerce.ui.home.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.ecommerce.R
import com.example.ecommerce.databinding.CategoryItemBinding
import com.example.ecommerce.model.Category
import com.example.ecommerce.utils.CONSTANTS
import com.example.ecommerce.utils.OnClickOnItem
import com.example.ecommerce.utils.removeSpace

class CategoryListAdapter(private val interaction:Interaction? = null) :
    ListAdapter<Category, CategoryListAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater =  LayoutInflater.from(parent.context)
        val binder = CategoryItemBinding.inflate(layoutInflater,parent,false)
       return CategoryViewHolder(binder, interaction)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(
       val binder: CategoryItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binder.root) {

        fun bind(item: Category) {
            binder.root.setOnClickListener {
                interaction?.onItemSelected(item)
            }
            binder.categoryName.text = item.name
            when(item.name.removeSpace()){
                CONSTANTS.CATEGORY_CARS -> binder.categoryImg.setImageResource(R.drawable.ic_car)
                CONSTANTS.CATEGORY_CLOTHEES -> binder.categoryImg.setImageResource(R.drawable.ic_tshirt)
                CONSTANTS.CATEGORY_ELECTONICS -> binder.categoryImg.setImageResource(R.drawable.ic_cpu)
                CONSTANTS.CATEGORY_FURNITURE -> binder.categoryImg.setImageResource(R.drawable.ic_furniture)
            }
        }
    }
    interface Interaction {

        fun onItemSelected(item:Category)
    }



    private class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(
            oldItem: Category,
            newItem: Category
        ): Boolean {

            return   oldItem.id == newItem.id

        }

        override fun areContentsTheSame(
            oldItem: Category,
            newItem: Category
        ): Boolean {
            return   oldItem.id == newItem.id && oldItem.name == newItem.name
        }
    }
}
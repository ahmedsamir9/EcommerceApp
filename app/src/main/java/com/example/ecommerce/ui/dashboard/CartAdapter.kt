package com.example.ecommerce.ui.dashboard

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.ecommerce.R
import com.example.ecommerce.databinding.CartItemBinding
import com.example.ecommerce.model.OrderWithProduct
import com.example.ecommerce.model.Product
import com.example.ecommerce.utils.CONSTANTS
import com.example.ecommerce.utils.Resources
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class CartAdapter(private val interaction: Interaction? = null) :
        ListAdapter<OrderWithProduct, CartAdapter.CartViewHolder>(OrderWithProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding,interaction)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(val binding :CartItemBinding,
                               private val interaction: Interaction?):RecyclerView.ViewHolder(binding.root){

        fun bind(item: OrderWithProduct){
            binding.removeBtn.setOnClickListener {
              interaction?.onDeleteItem(adapterPosition,item)
            }
            binding.plusBtn.setOnClickListener {
                item.quantity +=1
                interaction?.onChangeValue(item,"plus")
                binding.productData=item
            }
            binding.minusBtn.setOnClickListener {
                if (item.quantity >1){
                    item.quantity -=1
                    interaction?.onChangeValue(item,"minus")
                    binding.productData=item
                }
            }
            binding.productData=item
            binding.productData=item
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = Firebase.firestore.collection(CONSTANTS.PRODUCT_KEY).document(item.productId).get().await()
                    val product = result.toObject<Product>()
                    binding.product = product
                }
                catch (ex:Exception) {

                }
            }
            }
        }


    interface Interaction {
        fun onChangeValue(item: OrderWithProduct,type:String)
        fun onDeleteItem(position: Int,item: OrderWithProduct)
    }

    private class OrderWithProductDiffCallback : DiffUtil.ItemCallback<OrderWithProduct>() {
        override fun areItemsTheSame(
                oldItem: OrderWithProduct,
                newItem: OrderWithProduct
        ): Boolean {
             return oldItem.orderId == newItem.orderId && oldItem.productId== newItem.productId
        }

        override fun areContentsTheSame(
                oldItem: OrderWithProduct,
                newItem: OrderWithProduct
        ): Boolean {
            return oldItem == newItem
        }
    }
}
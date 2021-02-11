package com.example.ecommerce.ui.home.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.ecommerce.R
import com.example.ecommerce.databinding.AdsItemBinding
import com.example.ecommerce.utils.OnClickOnItem

class adsAdapter(private val interaction: OnClickOnItem? = null) :
    ListAdapter<Int, adsAdapter.adsViewHolder>(IntDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adsViewHolder {
        val binding = AdsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return adsViewHolder(binding,interaction)
    }

    override fun onBindViewHolder(holder: adsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class adsViewHolder(
        val adsItemBinding: AdsItemBinding,
        private val interaction: OnClickOnItem?
    ) : RecyclerView.ViewHolder(adsItemBinding.adsImg), OnClickListener {

        override fun onClick(v: View?) {

            if (adapterPosition == RecyclerView.NO_POSITION) return

        }

        fun bind(item: Int) {
           adsItemBinding.adsImg.setImageResource(item)
            adsItemBinding.root.setOnClickListener{
                interaction?.onClickItem(0)
            }
            animateView(adsItemBinding.root)
        }

        private fun animateView(viewToAnimate: View) {
            if (viewToAnimate.animation == null) {
                val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.scale_xy)
                viewToAnimate.animation = animation
            }
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: Int)
    }

    private class IntDiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(
            oldItem: Int,
            newItem: Int
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: Int,
            newItem: Int
        ): Boolean {
          return false
        }
    }
}
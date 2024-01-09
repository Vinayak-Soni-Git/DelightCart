package com.example.delightcart.Adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.delightcart.databinding.ProductColorsItemLayoutBinding

class ProductColorsAdapter : RecyclerView.Adapter<ProductColorsAdapter.ViewHolder>() {

    private var selectedPosition = -1

    inner class ViewHolder(private val binding: ProductColorsItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(color: Int, position: Int) {
            val imageDrawable = ColorDrawable(color)
            binding.productColorCircleImage2.setImageDrawable(imageDrawable)
            if (position == selectedPosition) { //color is selected
                binding.apply {
                    productColorCircleImageShadow.visibility = View.VISIBLE
                    productImagePicker.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    productColorCircleImageShadow.visibility = View.INVISIBLE
                    productImagePicker.visibility = View.INVISIBLE
                }
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ProductColorsItemLayoutBinding.inflate(LayoutInflater.from(parent.context)))


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = differ.currentList[position]
        holder.bind(color, position)

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) {
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)

            onItemClick?.invoke(color)
        }
    }

    var onItemClick: ((Int) -> Unit)? = null

}
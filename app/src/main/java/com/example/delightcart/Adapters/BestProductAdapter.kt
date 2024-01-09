package com.example.delightcart.Adapters

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.delightcart.Models.Product
import com.example.delightcart.R
import com.example.delightcart.databinding.MainProductItemLayoutBinding
import kotlin.math.roundToInt

class BestProductAdapter(private val context: Context) :
    RecyclerView.Adapter<BestProductAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: MainProductItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                product.offerPercentage?.let {
                    val remainingPricePercentage = it
                    Log.d("pricePercentage", remainingPricePercentage.toString())
                    val offerAmount = (remainingPricePercentage * product.price) / 100
                    val amountAfterOffer =
                        Math.subtractExact(product.price.toInt(), offerAmount.toInt())

                    productNewPrice.text =
                        context.getString(R.string.productPrice, amountAfterOffer)
                    Log.d("priceNewPrice", productNewPrice.toString())
                    productPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                if (product.offerPercentage == null) {
                    productNewPrice.visibility = View.VISIBLE
                }
                productPrice.text =
                    context.getString(R.string.productPrice, product.price.roundToInt())
                Log.d("pricePrice", product.price.toString())
                productName.text = product.name
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MainProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    var onClick: ((Product) -> Unit)? = null
}
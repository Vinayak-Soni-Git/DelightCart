package com.example.delightcart.Adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.delightcart.Models.CartProduct
import com.example.delightcart.R
import com.example.delightcart.databinding.BillingProductsItemLayoutBinding

class BillingProductsAdapter(private val context: Context) :
    Adapter<BillingProductsAdapter.BillingProductsViewHolder>() {

    inner class BillingProductsViewHolder(val binding: BillingProductsItemLayoutBinding) :
        ViewHolder(binding.root) {

        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(billingProductImage)
                billingProductName.text = cartProduct.product.name
                billingProductQuantity.text = cartProduct.quantity.toString()

                val remainingPricePercentage = cartProduct.product.offerPercentage
                val offerAmount =
                    (cartProduct.product.price.times(remainingPricePercentage!!)).div(100).toInt()
                val totalPrice = (cartProduct.product.price - offerAmount) * cartProduct.quantity
                Log.d("billingbOfferPercentange", cartProduct.product.offerPercentage.toString())
                Log.d("billingbProductPrice", cartProduct.product.price.toString())
                Log.d("billingbOfferAmount", offerAmount.toString())
                Log.d("billingbTotalPrice", totalPrice.toString())

                billingProductTotalPrice.text = context.getString(
                    R.string.productPrice,
                    totalPrice.toInt()
                )

                imageCartProductColor.setImageDrawable(
                    ColorDrawable(
                        cartProduct.selectedColor ?: Color.TRANSPARENT
                    )
                )
                billingProductSize.text = cartProduct.selectedSize ?: "".also {
                    imageCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
                }
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductsViewHolder {
        return BillingProductsViewHolder(
            BillingProductsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BillingProductsViewHolder, position: Int) {
        val billingProduct = differ.currentList[position]

        holder.bind(billingProduct)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
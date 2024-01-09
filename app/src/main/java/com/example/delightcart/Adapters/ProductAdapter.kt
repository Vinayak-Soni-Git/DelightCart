package com.example.delightcart.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.delightcart.Models.Product
import com.example.delightcart.R

class ProductAdapter(private val context: Context, private val productsList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item_layout, parent, false);

        return ViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: Product = productsList[position]
//        holder.productImage.setImageResource(product.productImage)
//        holder.modelName.text = product.model
//        holder.productPrice.text = context.getString(R.string._888_inr, product.price.toString())
//        holder.discountText.text = context.getString(R.string.save_up_to, product.discount.toString(), "%")
//        holder.rating.rating = product.rating


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val modelName: TextView = itemView.findViewById(R.id.modelName)
        val productPrice: TextView = itemView.findViewById(R.id.price)
        val discountText: TextView = itemView.findViewById(R.id.discountSaveText)
        val rating: RatingBar = itemView.findViewById(R.id.ratingBar)

    }

}
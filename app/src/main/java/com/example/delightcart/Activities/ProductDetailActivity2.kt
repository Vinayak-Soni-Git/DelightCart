package com.example.delightcart.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.delightcart.MainActivity
import com.example.delightcart.R

class ProductDetailActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail_2)
        var productQuantity: Int = 1;
        val productPrice: Float = 40000f
        var newProductPrice: Float = productPrice
        var isWishListButtonPressed = false

        val backButton: ImageView = findViewById(R.id.productDetailsBackButton)
        val wishListButton: ImageView = findViewById(R.id.productDetailsWishlistButton)
        wishListButton.setOnClickListener {
            if (!isWishListButtonPressed) {
                wishListButton.setImageResource(R.drawable.ic_liked)
                isWishListButtonPressed = true
            } else {
                wishListButton.setImageResource(R.drawable.ic_unliked)
                isWishListButtonPressed = false
            }

        }

        val productQuantityText: TextView = findViewById(R.id.productDetailsProductQuantity)
        val increaseProductQuantityButton: TextView =
            findViewById(R.id.productDetailsIncreaseQuantityButton)
        val decreaseProductQuantityButton: TextView =
            findViewById(R.id.productDetailsDecreaseQuantityButton)
        val productPriceText: TextView = findViewById(R.id.productDetailsProductPrice)
        productPriceText.text = productPrice.toString()

        increaseProductQuantityButton.setOnClickListener {
            if (productQuantity < 20) {
                productQuantity += 1
                newProductPrice += productPrice

                productQuantityText.text = productQuantity.toString()
                productPriceText.text = newProductPrice.toString()
            }
        }

        decreaseProductQuantityButton.setOnClickListener {
            if (productQuantity > 0) {
                productQuantity -= 1
                newProductPrice -= productPrice

                productQuantityText.text = productQuantity.toString()
                productPriceText.text = newProductPrice.toString()
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }


//        initiateSimilarItemsRecyclerView()
    }

    //    private fun initiateSimilarItemsRecyclerView(){
//        val recyclerView: RecyclerView = findViewById(R.id.similarItemsRecyclerView)
//        recyclerView.setHasFixedSize(true)
//        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.itemAnimator = DefaultItemAnimator()
//
//        val arrayList:List<Product> = MobilePhonesData().mobilesList
//        val adapter = SimilarProductAdapter(this, arrayList)
//        recyclerView.adapter = adapter
//    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
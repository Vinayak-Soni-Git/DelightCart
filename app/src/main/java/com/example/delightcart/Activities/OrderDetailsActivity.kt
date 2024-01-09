package com.example.delightcart.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delightcart.Adapters.BillingProductsAdapter
import com.example.delightcart.Models.CartProduct
import com.example.delightcart.Models.Order
import com.example.delightcart.Models.OrderStatus
import com.example.delightcart.Models.getOrderStatus
import com.example.delightcart.R
import com.example.delightcart.SpacingDecorators.VerticalItemDecoration
import com.example.delightcart.databinding.ActivityOrdersDetailsBinding

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersDetailsBinding
    private val billingProductAdapter by lazy { BillingProductsAdapter(this@OrderDetailsActivity) }
    private var products = emptyList<CartProduct>()
    private var order: Order? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrdersDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageCloseOrder.setOnClickListener {
            finish()
        }

        if (intent.hasExtra("order")) {
            order = intent.getParcelableExtra<Order>("order")
            products = intent.getParcelableArrayExtra("products")?.filterIsInstance<CartProduct>()
                ?.toList() ?: emptyList()
        }
        setupOrderRecyclerView()
        if (order != null) {
            binding.apply {
                tvOrderId.text = "#${order?.orderId}"

                stepView.setSteps(
                    mutableListOf(
                        OrderStatus.Ordered.status,
                        OrderStatus.Confirmed.status,
                        OrderStatus.Shipped.status,
                        OrderStatus.Delivered.status
                    )
                )
                val currentOrderState = when (getOrderStatus(order!!.orderStatus)) {
                    is OrderStatus.Ordered -> 0
                    is OrderStatus.Confirmed -> 1
                    is OrderStatus.Shipped -> 2
                    is OrderStatus.Delivered -> 3
                    else -> 0
                }
                stepView.go(currentOrderState, false)
                if (currentOrderState == 3) {
                    stepView.done(true)
                }
                tvFullName.text = order!!.address.fullName
                tvAddress.text =
                    getString(R.string.fullAddress, order!!.address.street, order!!.address.city)
                tvPhoneNumber.text = order!!.address.phoneNumber

                cartTotalPrice.text = getString(R.string.productPrice, order!!.totalPrice.toInt())
            }
        }


        billingProductAdapter.differ.submitList(products)
    }

    private fun setupOrderRecyclerView() {
        binding.rvProducts.apply {
            adapter = billingProductAdapter
            layoutManager =
                LinearLayoutManager(this@OrderDetailsActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(VerticalItemDecoration())
        }
    }
}
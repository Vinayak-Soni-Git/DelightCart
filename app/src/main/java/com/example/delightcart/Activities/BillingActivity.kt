package com.example.delightcart.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delightcart.Adapters.AddressAdapter
import com.example.delightcart.Adapters.BillingProductsAdapter
import com.example.delightcart.Dialog.setupOrderConfirmedDialog
import com.example.delightcart.Home.Home
import com.example.delightcart.Models.Address
import com.example.delightcart.Models.CartProduct
import com.example.delightcart.Models.Order
import com.example.delightcart.Models.OrderStatus
import com.example.delightcart.R
import com.example.delightcart.SpacingDecorators.HorizontalItemDecoration
import com.example.delightcart.Util.Resource
import com.example.delightcart.ViewModel.BillingViewModel
import com.example.delightcart.ViewModel.OrderViewModel
import com.example.delightcart.databinding.ActivityBillingBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductsAdapter by lazy { BillingProductsAdapter(this@BillingActivity) }
    private val billingViewModel by viewModels<BillingViewModel>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f

    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBillingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val rootView: View = findViewById(android.R.id.content)
        
        binding.imageCloseBilling.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        if (intent.hasExtra("productsList")) {
            products =
                intent.getParcelableArrayExtra("productsList")?.filterIsInstance<CartProduct>()
                    ?.toList() ?: emptyList()
            totalPrice = intent.extras?.getFloat("totalPrice")!!
        }
        setupBillingProductsRecyclerView()
        setupAddressRecyclerView()



        lifecycleScope.launchWhenStarted {
            billingViewModel.address.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility = View.GONE
                    }

                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(
                            this@BillingActivity,
                            "Error ${it.message}",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonPlaceOrder.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        setupOrderConfirmedDialog()
                        Snackbar.make(rootView, "Your order was placed", Snackbar.LENGTH_LONG)
                            .show()
                    }

                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(
                            this@BillingActivity,
                            "Error ${it.message}",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                    else -> Unit
                }
            }
        }

        billingProductsAdapter.differ.submitList(products)
        binding.cartTotalPrice.text = getString(R.string.productPrice, totalPrice.toInt())

        addressAdapter.onClick = {
            selectedAddress = it
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if (selectedAddress == null) {
                Toast.makeText(this@BillingActivity, "Please select an address", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            showOrderConfirmationDialog()
        }
        binding.imageAddAddress.setOnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun showOrderConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this).apply {
            setTitle("Order items")
            setMessage("Do you want to order your cart items?")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { dialog, _ ->
                val order = Order(
                    OrderStatus.Ordered.status,
                    totalPrice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }


    private fun setupAddressRecyclerView() {
        binding.rvAddress.apply {
            layoutManager =
                LinearLayoutManager(this@BillingActivity, RecyclerView.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingProductsRecyclerView() {
        binding.rvProducts.apply {
            layoutManager =
                LinearLayoutManager(this@BillingActivity, RecyclerView.HORIZONTAL, false)
            adapter = billingProductsAdapter
            addItemDecoration(HorizontalItemDecoration())

        }
    }

}
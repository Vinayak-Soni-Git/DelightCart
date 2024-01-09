package com.example.delightcart.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delightcart.Adapters.AllOrdersAdapter
import com.example.delightcart.Home.Home
import com.example.delightcart.R
import com.example.delightcart.Util.Resource
import com.example.delightcart.ViewModel.AllOrdersViewModel
import com.example.delightcart.databinding.ActivityOrdersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AllOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    val viewModel by viewModels<AllOrdersViewModel>()
    private val orderAdapter by lazy { AllOrdersAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpOrdersRecyclerView()
        binding.imageCloseOrders.setOnClickListener {
            finish()
        }

        lifecycleScope.launchWhenCreated {
            viewModel.allOrders.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAllOrders.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        orderAdapter.differ.submitList(it.data)
                        if (it.data.isNullOrEmpty()) {
                            binding.tvEmptyOrders.visibility = View.VISIBLE
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(this@AllOrdersActivity, it.message, Toast.LENGTH_LONG).show()
                        binding.progressbarAllOrders.visibility = View.GONE
                    }

                    else -> Unit
                }
            }
        }

        orderAdapter.onClick = {
            val intent = Intent(this@AllOrdersActivity, OrderDetailsActivity::class.java)
            intent.putExtra("order", it)
            intent.putExtra("products", it.products.toTypedArray())
            startActivity(intent)
        }
    }

    private fun setUpOrdersRecyclerView() {
        binding.allOrdersRecyclerView.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(this@AllOrdersActivity, RecyclerView.VERTICAL, false)

        }
    }
}
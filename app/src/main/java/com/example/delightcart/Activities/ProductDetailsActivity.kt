package com.example.delightcart.Activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delightcart.Adapters.ImagesViewPagerAdapter
import com.example.delightcart.Adapters.ProductColorsAdapter
import com.example.delightcart.Adapters.ProductSizesAdapter
import com.example.delightcart.Models.CartProduct
import com.example.delightcart.Models.Product
import com.example.delightcart.R
import com.example.delightcart.Util.Resource
import com.example.delightcart.ViewModel.DetailsViewModel
import com.example.delightcart.databinding.ActivityProductDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private val viewPagerAdapter by lazy { ImagesViewPagerAdapter() }
    private val productSizesAdapter by lazy { ProductSizesAdapter() }
    private val productColorsAdapter by lazy { ProductColorsAdapter() }
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val product = intent.getParcelableExtra<Product>("product")

        setupProductSizesRecyclerView()
        setupProductColorsRecyclerView()
        setupViewPager()

        if (product != null) {
            binding.apply {
                productName.text = product.name
                productPrice.text = getString(R.string.productPrice, product.price.toInt())
                productDescription.text = product.description
                if (product.sizes.isNullOrEmpty()) {
                    productSizeText.visibility = View.INVISIBLE
                    productSizesRecyclerView.visibility = View.INVISIBLE
                }
                if (product.colors.isNullOrEmpty()) {
                    productColorsText.visibility = View.GONE
                    productColorsRecyclerView.visibility = View.INVISIBLE
                }
            }
        }
        productSizesAdapter.onItemClick = {
            selectedSize = it
        }
        productColorsAdapter.onItemClick = {
            selectedColor = it
        }
        if (product != null) {
            binding.buttonAddToCart.setOnClickListener {
                viewModel.addUpdateProductInCart(
                    CartProduct(
                        product,
                        1,
                        selectedColor,
                        selectedSize
                    )
                )
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonAddToCart.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonAddToCart.revertAnimation()
                        Toast.makeText(
                            this@ProductDetailsActivity,
                            "Product was added",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Error -> {
                        binding.buttonAddToCart.revertAnimation()
                        Toast.makeText(this@ProductDetailsActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> Unit
                }
            }
        }


        viewPagerAdapter.differ.submitList(product?.images)
        product?.colors?.let {
            productColorsAdapter.differ.submitList(product.colors)
        }
        product?.sizes?.let {
            productSizesAdapter.differ.submitList(product.sizes)
        }
    }

    private fun setupViewPager() {
        binding.apply {
            productImagesViewPager.adapter = viewPagerAdapter
        }
    }

    private fun setupProductColorsRecyclerView() {
        binding.productColorsRecyclerView.apply {
            adapter = productColorsAdapter
            layoutManager = LinearLayoutManager(
                this@ProductDetailsActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setupProductSizesRecyclerView() {
        binding.productSizesRecyclerView.apply {
            adapter = productSizesAdapter
            layoutManager = LinearLayoutManager(
                this@ProductDetailsActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }
}
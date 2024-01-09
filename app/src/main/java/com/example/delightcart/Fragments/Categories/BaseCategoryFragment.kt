package com.example.delightcart.Fragments.Categories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delightcart.Activities.ProductDetailsActivity
import com.example.delightcart.Adapters.BestProductAdapter
import com.example.delightcart.R
import com.example.delightcart.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter(requireContext()) }
    protected val bestProductsAdapter: BestProductAdapter by lazy {
        BestProductAdapter(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOfferRecyclerView()
        setupBestProductRecyclerView()

        binding.bestProductsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dx != 0) {
                    onOfferPagingRequest()
                }
            }
        })

        binding.baseCategoryNestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                onBestProductsPagingRequest()
            }
        })
        offerAdapter.onClick = {
            val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
            intent.putExtra("product", it)
            startActivity(intent)
        }
        bestProductsAdapter.onClick = {
            val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
            intent.putExtra("product", it)
            startActivity(intent)
        }

    }

    fun showOfferProductLoading() {
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideOfferProductLoading() {
        binding.offerProductsProgressBar.visibility = View.GONE
    }

    fun showBestProductLoading() {
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideBestProductLoading() {
        binding.bestProductsProgressBar.visibility = View.GONE
    }


    open fun onOfferPagingRequest() {

    }

    open fun onBestProductsPagingRequest() {

    }


    private fun setupBestProductRecyclerView() {
        binding.bestProductsRecyclerView.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun setupOfferRecyclerView() {
        binding.offerRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
        }
    }
}
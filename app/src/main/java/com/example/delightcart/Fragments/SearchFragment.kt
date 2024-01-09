package com.example.delightcart.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delightcart.Activities.ProductDetailsActivity
import com.example.delightcart.Adapters.CategoriesAdapter
import com.example.delightcart.Adapters.SearchAdapter
import com.example.delightcart.Home.Home
import com.example.delightcart.R
import com.example.delightcart.SpacingDecorators.VerticalItemDecoration
import com.example.delightcart.Util.Resource
import com.example.delightcart.ViewModel.MainCategoryViewModel
import com.example.delightcart.ViewModel.SearchViewModel
import com.example.delightcart.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val TAG = "SearchFragment"
    private lateinit var binding: FragmentSearchBinding
    private lateinit var inputMethodManager: InputMethodManager
    private val viewModel by viewModels<MainCategoryViewModel>()
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.categories.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showCategoriesLoading()
                    }

                    is Resource.Success -> {
                        hideCategoriesLoading()
                        categoriesAdapter.differ.submitList(it.data)
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            searchViewModel.searchResult.collectLatest {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        searchAdapter.differ.submitList(it.data)
                        showCancelTextView()
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        showCancelTextView()
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root

    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            var position = 0
            when (category.name) {
                resources.getString(R.string.mobileCategoryName) -> position = 1
                resources.getString(R.string.tabletCategoryName) -> position = 2
                resources.getString(R.string.laptopsCategoryName) -> position = 3
                resources.getString(R.string.clothesCategoryName) -> position = 4
                resources.getString(R.string.furnitureCategoryName) -> position = 5
            }

            val bundle = Bundle()
            bundle.putInt("position", position)

        }

        binding.frameScan.setOnClickListener {
            Snackbar.make(requireView(), "The Feature is not available", Snackbar.LENGTH_SHORT)
                .show()
        }
        binding.frameMicrophone.setOnClickListener {
            Snackbar.make(requireView(), "The Feature is not available", Snackbar.LENGTH_SHORT)
                .show()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoryRecyclerView()
        setupSearchRecyclerView()
        showKeyboardAutomatically()

        searchProducts()
        observeSearch()

        observeCategories()

        onSearchTextClick()

        onCancelTvClick()

        onCategoryClick()

    }

    private fun onCancelTvClick() {
        binding.cancelText.setOnClickListener {
            searchAdapter.differ.submitList(emptyList())
            binding.searchEditText.setText("")
            hideCancelTextView()
        }
    }

    private fun onSearchTextClick() {
        searchAdapter.onItemClick = { product ->
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(requireView().windowToken, 0)

            val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)

        }
    }

    private fun setupSearchRecyclerView() {
        searchAdapter = SearchAdapter()
        binding.searchResultRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupCategoryRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.categoriesRecyclerView.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            addItemDecoration(VerticalItemDecoration(40))
        }
    }

    private fun observeCategories() {

    }

    private fun hideCategoriesLoading() {
        binding.progressbarCategories.visibility = View.GONE

    }

    private fun showCategoriesLoading() {
        binding.progressbarCategories.visibility = View.VISIBLE
    }

    private fun observeSearch() {
        lifecycleScope.launchWhenStarted {
            searchViewModel.searchResult.collectLatest {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        searchAdapter.differ.submitList(it.data)
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    var job: Job? = null
    private fun searchProducts() {
        binding.searchEditText.addTextChangedListener { query ->
            val queryTrim = query.toString().trim()
            if (queryTrim.isNotEmpty()) {
                val searchQuery = query.toString().substring(0, 1).toUpperCase()
                    .plus(query.toString().substring(1))
                job?.cancel()
                job = CoroutineScope(Dispatchers.IO).launch {
                    delay(500L)
                    searchViewModel.searchProducts(searchQuery)
                }
            } else {
                searchAdapter.differ.submitList(emptyList())
                hideCancelTextView()
            }
        }
    }

    private fun showCancelTextView() {
        binding.cancelText.visibility = View.VISIBLE
        binding.imgMic.visibility = View.GONE
        binding.imgScan.visibility = View.GONE
        binding.frameMicrophone.visibility = View.GONE
        binding.frameScan.visibility = View.GONE

    }

    private fun hideCancelTextView() {
        binding.cancelText.visibility = View.GONE
        binding.imgMic.visibility = View.VISIBLE
        binding.imgScan.visibility = View.VISIBLE
        binding.frameMicrophone.visibility = View.VISIBLE
        binding.frameScan.visibility = View.VISIBLE
    }

//    private fun onHomeClick() {
//        val btm = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
//        btm?.menu?.getItem(0)?.setOnMenuItemClickListener {
//            activity?.onBackPressed()
//            true
//        }
//    }

    private fun showKeyboardAutomatically() {
        inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )

        binding.searchEditText.requestFocus()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        binding.searchEditText.clearFocus()
        val intent = Intent(requireContext(), Home::class.java)
        startActivity(intent)
    }

}
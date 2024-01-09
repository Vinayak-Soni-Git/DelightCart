package com.example.delightcart.Home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.delightcart.Fragments.CartFragment
import com.example.delightcart.Fragments.SearchFragment
import com.example.delightcart.R
import com.example.delightcart.Util.Resource
import com.example.delightcart.ViewModel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val viewModel by viewModels<CartViewModel>()

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#034A6A"))
        actionBar?.setBackgroundDrawable(colorDrawable)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    loadFragment(HomeFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_explore -> {
                    loadFragment(SearchFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_cart -> {
                    loadFragment(CartFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_account -> {
                    loadFragment(AccountFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> false
            }
        }

        // Set the default fragment
        loadFragment(HomeFragment(), false)


        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        val count = it.data?.size ?: 0
                        val bottomNavigationView =
                            findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                        bottomNavigationView.getOrCreateBadge(R.id.menu_cart).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.colorPrimary)
                        }
                    }

                    else -> Unit
                }
            }
        }

    }

    private fun loadFragment(fragment: Fragment, flag: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (flag) {
            fragmentTransaction.add(R.id.bottomNavigationContainer, fragment)
        } else {
            fragmentTransaction.replace(R.id.bottomNavigationContainer, fragment)
        }

        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
    
    
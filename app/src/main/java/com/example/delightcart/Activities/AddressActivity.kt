package com.example.delightcart.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.delightcart.Home.Home
import com.example.delightcart.Models.Address
import com.example.delightcart.R
import com.example.delightcart.Util.Resource
import com.example.delightcart.ViewModel.AddressViewModel
import com.example.delightcart.databinding.ActivityAddressBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    val viewModel by viewModels<AddressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddressBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        finish()
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    }

                    is Resource.Error -> {
                        Toast.makeText(this@AddressActivity, it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(this@AddressActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.imageAddressClose.setOnClickListener {
            val intent = Intent(this@AddressActivity, Home::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.apply {
            buttonSave.setOnClickListener {
                val addressTitle = addressTitle.text.toString()
                val fullName = addressFullName.text.toString()
                val street = addressStreet.text.toString()
                val phone = addressPhone.text.toString()
                val city = addressCity.text.toString()
                val state = addressState.text.toString()
                val address = Address(addressTitle, fullName, street, phone, city, state)
                viewModel.addAddress(address)
            }
        }

    }


}
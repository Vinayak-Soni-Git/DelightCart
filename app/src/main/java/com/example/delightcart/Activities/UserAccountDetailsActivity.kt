package com.example.delightcart.Activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.delightcart.Models.User
import com.example.delightcart.R
import com.example.delightcart.Util.Resource
import com.example.delightcart.ViewModel.UserAccountViewModel
import com.example.delightcart.databinding.ActivityUserAccountDetailsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UserAccountDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserAccountDetailsBinding
    private val viewModel by viewModels<UserAccountViewModel>()
    private var imageUri: Uri? = null
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserAccountDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val rootView: View = findViewById(android.R.id.content)
        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                imageUri = it.data?.data
                Glide.with(this).load(imageUri).into(binding.profileImage)
            }

        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showUserLoading()
                    }

                    is Resource.Success -> {
                        hideUserLoading()
                        showUserInformation(it.data!!)
                    }

                    is Resource.Error -> {
                        Toast.makeText(
                            this@UserAccountDetailsActivity,
                            it.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.updateInfo.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.updateButton.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.updateButton.revertAnimation()
                        Snackbar.make(rootView, "Details updated", Snackbar.LENGTH_SHORT).show()
                    }

                    is Resource.Error -> {
                        Toast.makeText(
                            this@UserAccountDetailsActivity,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit
                }
            }
        }
        binding.updateButton.setOnClickListener {
            binding.apply {
                val firstName = profileFirstName.text.toString().trim()
                val lastName = profileLastName.text.toString().trim()
                val email = profileEmail.text.toString().trim()
                val phone = profilePhoneNumber.text.toString().trim()
                val user = User(firstName, lastName, email, phone)
                viewModel.updateUser(user, imageUri)
            }
        }

        binding.imageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(intent)
        }
        binding.addressCardView.setOnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
        }

    }


    private fun showUserInformation(data: User) {
        binding.apply {
            Glide.with(this@UserAccountDetailsActivity).load(data.imagePath)
                .error(ColorDrawable(Color.BLACK)).into(profileImage)
            profileFullUserName.text =
                getString(R.string.userFullName, data.firstName, data.lastName)

            profileFirstName.setText(data.firstName)
            profileLastName.setText(data.lastName)
            profileEmail.setText(data.email)
            profilePhoneNumber.setText(data.phone)

            cartCountLabel.text = viewModel.getTotalCartItemCount().toString()
            ordersCountLabel.text = viewModel.getTotalOrdersCount().toString()
            addressCountLabel.text = viewModel.getTotalAddressCount().toString()

        }
    }

    private fun hideUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.GONE
            profileLinearLayout.visibility = View.VISIBLE
            cardViewsLinearLayout.visibility = View.VISIBLE
            editTextsLinearLayout.visibility = View.VISIBLE
        }
    }

    private fun showUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.VISIBLE
            profileLinearLayout.visibility = View.GONE
            cardViewsLinearLayout.visibility = View.GONE
            editTextsLinearLayout.visibility = View.GONE
        }
    }
}
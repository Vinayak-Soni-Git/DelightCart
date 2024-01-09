package com.example.delightcart.Home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.delightcart.Activities.AllOrdersActivity
import com.example.delightcart.Activities.BillingActivity
import com.example.delightcart.Activities.SignInActivities.SignInActivity
import com.example.delightcart.Activities.UserAccountDetailsActivity
import com.example.delightcart.Util.Resource
import com.example.delightcart.ViewModel.ProfileViewModel
import com.example.delightcart.databinding.FragmentAccountBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintProfile.setOnClickListener {
            val intent = Intent(requireContext(), UserAccountDetailsActivity::class.java)
            startActivity(intent)
        }

        binding.userAccountDetailsArrow.setOnClickListener {
            val intent = Intent(requireContext(), UserAccountDetailsActivity::class.java)
            startActivity(intent)
        }
        binding.accountsAllOrdersArrow.setOnClickListener {
            val intent = Intent(requireContext(), AllOrdersActivity::class.java)
            startActivity(intent)
        }
        binding.linearAllOrders.setOnClickListener {
            val intent = Intent(requireContext(), AllOrdersActivity::class.java)
            startActivity(intent)
        }

        binding.linearBilling.setOnClickListener {
            val intent = Intent(requireContext(), BillingActivity::class.java)
            startActivity(intent)
        }

        binding.linearLogOut.setOnClickListener {
            viewModel.logOut()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            googleSignInClient.signOut()

            val intent = Intent(requireActivity(), SignInActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarSettings.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Glide.with(requireView()).load(it.data!!.imagePath).error(
                            ColorDrawable(
                                Color.BLACK
                            )
                        ).into(binding.accountUserImage)
                        binding.accountUserName.text = "${it.data.firstName} ${it.data.lastName}"
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        binding.progressbarSettings.visibility = View.GONE
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
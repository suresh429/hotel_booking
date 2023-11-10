package com.example.myapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.di.AppModule
import com.example.myapplication.network.ApiState
import com.example.myapplication.viewmodel.ConstantViewModel
import com.example.myapplication.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModels by viewModels<MainViewModel>()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var locationViewModel: ConstantViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        locationViewModel = ViewModelProvider(requireActivity())[ConstantViewModel::class.java]

        // city name
        val cityName = arguments?.getString("cityName")?:"Hyderabad"
        binding.txtSearchLocation.text = cityName
        binding.txtSearchLocation.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_locationDialogFragment)
        }
        getCities()
        return binding.root
    }


    private fun getCities() {
        lifecycleScope.launch {
            viewModels.getCities(
                AppModule.USER_APP_KEY,
                "0",
                "0"
            ).collect {
                when (it) {
                    is ApiState.Success -> {
                        if (it.data?.result.equals("success")) {
                            // Populate city data and notify the Spinner to update
                            locationViewModel.mutableLiveData.value = it.data?.data

                        } else {
                            Snackbar.make(
                                binding.root,
                                it.data?.msg.toString(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        dismissLoading()
                    }

                    is ApiState.Failure -> {
                        Snackbar.make(
                            binding.root,
                            it.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                        dismissLoading()
                    }

                    ApiState.Loading -> {
                        showLoading()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.progressLayout.visibility = View.VISIBLE
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun dismissLoading() {
        binding.progressBar.progressLayout.visibility = View.GONE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    companion object {
        private const val TAG = "HomeFragment"
    }

}
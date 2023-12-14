package com.example.myapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.di.AppModule
import com.example.myapplication.model.HomeDataResponse
import com.example.myapplication.network.ApiState
import com.example.myapplication.ui.HotelListActivity
import com.example.myapplication.ui.adapter.HomeAdapter
import com.example.myapplication.utils.covertTimeToDate
import com.example.myapplication.utils.startNewActivity
import com.example.myapplication.utils.toast
import com.example.myapplication.viewmodel.ConstantViewModel
import com.example.myapplication.viewmodel.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.LinkedList

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModels by viewModels<MainViewModel>()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var locationViewModel: ConstantViewModel
    private var adapter: HomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        locationViewModel = ViewModelProvider(requireActivity())[ConstantViewModel::class.java]

        // city name
        val cityName = arguments?.getString("cityName") ?: "Hyderabad"
        val cityId = arguments?.getString("cityId") ?: "3"
        val adultCount = arguments?.getString("adultCount") ?: "1"
        val childCount = arguments?.getString("childCount") ?: "0"
        val roomCount = arguments?.getString("roomCount") ?: "1"
        val checkIn = arguments?.getString("checkIn")?:""
        val checkOut = arguments?.getString("checkOut")?:""

        binding.txtSearchLocation.text = cityName

        "$roomCount Room, $adultCount Adults & $childCount Children".also { binding.txtFilter.text = it }

        binding.txtCheckIn.text = checkIn
        binding.txtCheckOut.text = checkOut

        binding.txtSearchLocation.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("adultCount", adultCount)
            bundle.putString("childCount", childCount)
            bundle.putString("roomCount", roomCount)
            bundle.putString("checkIn", binding.txtCheckIn.text.toString())
            bundle.putString("checkOut", binding.txtCheckOut.text.toString())
            findNavController().navigate(R.id.action_navigation_home_to_locationDialogFragment,bundle)
        }

        binding.layout.setOnClickListener {
           rangePicker()
        }

        binding.txtFilter.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("cityName", cityName)
            bundle.putString("cityId", cityId)
            bundle.putString("checkIn", binding.txtCheckIn.text.toString())
            bundle.putString("checkOut", binding.txtCheckOut.text.toString())
            findNavController().navigate(
                R.id.action_navigation_home_to_filterDialogFragment,bundle
            )
        }

        binding.btnSearch.setOnClickListener {
            if (!binding.txtSearchLocation.text.isNullOrEmpty() && !binding.txtCheckIn.text.isNullOrEmpty() && !binding.txtCheckOut.text.isNullOrEmpty() && !binding.txtFilter.text.isNullOrEmpty()) {
                val intent = Intent(requireActivity(), HotelListActivity::class.java)
                intent.putExtra("cityId", cityId)
                intent.putExtra("checkInDate", binding.txtCheckIn.text.toString())
                intent.putExtra("checkOutDate", binding.txtCheckOut.text.toString())
                intent.putExtra("adultCount", adultCount)
                intent.putExtra("childCount", childCount)
                intent.putExtra("roomCount", roomCount)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }else {
                requireActivity().toast("Please Enter All Fields")
            }
        }


        getCities()
        getRecommendedHotels()
        return binding.root
    }

    private fun getRecommendedHotels(){

        lifecycleScope.launch {
            viewModels.getRecommendedHotels(
                AppModule.USER_APP_KEY,
                "1",
            ).collect {
                when (it) {
                    is ApiState.Success -> {
                        if (it.data?.result.equals("success")) {
                            // Populate city data and notify the Spinner to update

                            adapter = it.data?.data?.let { it1 -> HomeAdapter(it1) }
                            binding.recommendedRecyleList.adapter = adapter
                            adapter?.onItemClick = { _, data ->
                                /*val intent = Intent(this, WebActivity::class.java)
                                //Create the bundle
                                val bundle = Bundle()
                                bundle.putString("title", data.name)
                                //bundle.putString("endPoint", BuildConfig.CHAT_BOAT_BASE_URL)
                                bundle.putString("endPoint", data.url)
                                intent.putExtras(bundle)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)*/
                            }

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

    private fun rangePicker(){
        val rangePicker = MaterialDatePicker.Builder.dateRangePicker()
            // .setTheme(android.R.style.Widget_Material_Light_CalendarView)
            .setTitleText("Choose Date")
            .setSelection(Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
            .build()

        rangePicker.show(this.childFragmentManager, "Tag")

        rangePicker.addOnPositiveButtonClickListener {
            (covertTimeToDate(it.first)).also { binding.txtCheckIn.text = it }
            (covertTimeToDate(it.second)).also { binding.txtCheckOut.text = it }
        }

        rangePicker.addOnNegativeButtonClickListener {
            rangePicker.dismiss()
        }
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
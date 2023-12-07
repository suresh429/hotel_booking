package com.example.myapplication.ui.fragments

import android.os.Bundle
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
import com.example.myapplication.ui.adapter.HomeAdapter
import com.example.myapplication.utils.covertTimeToDate
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
    private var recommendedArray: LinkedList<HomeDataResponse> = LinkedList<HomeDataResponse>()
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
        binding.txtSearchLocation.text = cityName
        binding.txtSearchLocation.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_locationDialogFragment)
        }

        binding.layout.setOnClickListener {
           rangePicker()
        }
        getCities()
        getHotels()
        return binding.root
    }

    private fun getHotels(){
        recommendedArray.add(HomeDataResponse("Star Hotel",R.drawable.airplane,"https://cdn.pixabay.com/photo/2020/05/09/09/13/house-5148865_1280.jpg"))
        recommendedArray.add(HomeDataResponse("Sea Stay Hotel",R.drawable.hotel,"https://cdn.pixabay.com/photo/2022/10/23/02/26/hotel-7540353_640.jpg"))
        recommendedArray.add(HomeDataResponse("Pam Beach Hotel ",R.drawable.bus,"https://cdn.pixabay.com/photo/2017/06/11/12/33/swimming-2392283_640.jpg"))
        recommendedArray.add(HomeDataResponse("The Westin",R.drawable.holiday,"https://cdn.pixabay.com/photo/2016/08/26/20/30/hotel-1623064_640.jpg"))
        recommendedArray.add(HomeDataResponse("Park Hyatt",R.drawable.holiday,"https://cdn.pixabay.com/photo/2017/08/06/14/56/people-2593251_640.jpg"))
        recommendedArray.add(HomeDataResponse("Trident Hotel",R.drawable.holiday,"https://cdn.pixabay.com/photo/2017/05/31/10/23/manor-house-2359884_640.jpg"))
        adapter = HomeAdapter(recommendedArray)
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
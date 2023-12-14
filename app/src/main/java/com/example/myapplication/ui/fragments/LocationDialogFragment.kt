package com.example.myapplication.ui.fragments

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLocationDialogBinding
import com.example.myapplication.model.CitiesResponse
import com.example.myapplication.ui.adapter.LocationListAdapter
import com.example.myapplication.utils.UserPreferences
import com.example.myapplication.viewmodel.ConstantViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LocationDialogFragment : BottomSheetDialogFragment() {
    private var originalList: MutableList<CitiesResponse.Data>? = null
    private var _binding: FragmentLocationDialogBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userPreferences: UserPreferences

    @Inject
    lateinit var adapter: LocationListAdapter

    private lateinit var locationViewModel: ConstantViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationDialogBinding.inflate(inflater, container, false)
        (dialog as? BottomSheetDialog)?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED


        locationViewModel = ViewModelProvider(requireActivity())[ConstantViewModel::class.java]
        locationViewModel.mutableLiveData.observe(viewLifecycleOwner) {
            originalList = it
            adapter.submitList(it)
        }

        // Inside your activity or fragment
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(newText: Editable?) {
                originalList?.let { adapter.submitListWithFilter(it, newText.toString().orEmpty()) }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }
        })
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adultCount = arguments?.getString("adultCount") ?: "1"
        val childCount = arguments?.getString("childCount") ?: "0"
        val roomCount = arguments?.getString("roomCount") ?: "1"
        val checkIn = arguments?.getString("checkIn")
        val checkOut = arguments?.getString("checkOut")

        binding.countryList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        binding.countryList.adapter = adapter
        adapter.onItemClick = { cityResponse, _ ->
            lifecycleScope.launch {
                Log.d(TAG, "onViewCreated: ${cityResponse.city_id}")
                val bundle = Bundle()
                bundle.putString("cityName", cityResponse.city_name)
                bundle.putString("cityId", cityResponse.city_id)
                bundle.putString("adultCount", adultCount)
                bundle.putString("childCount", childCount)
                bundle.putString("roomCount", roomCount)
                bundle.putString("checkIn", checkIn)
                bundle.putString("checkOut", checkOut)

                findNavController().navigate(
                    R.id.action_locationDialogFragment_to_navigation_home,
                    bundle
                )
                dialog?.dismiss()
            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CountryDialogFragment"
    }
}
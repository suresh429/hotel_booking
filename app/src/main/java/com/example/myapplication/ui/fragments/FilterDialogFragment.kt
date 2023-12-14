package com.example.myapplication.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFilterDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        (dialog as? BottomSheetDialog)?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED

        val cityName = arguments?.getString("cityName") ?: "Hyderabad"
        val cityId = arguments?.getString("cityId") ?: "3"
        val checkIn = arguments?.getString("checkIn")
        val checkOut = arguments?.getString("checkOut")

        binding.btnDone.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("cityName", cityName)
            bundle.putString("cityId", cityId)
            bundle.putString("checkIn", checkIn)
            bundle.putString("checkOut", checkOut)
            bundle.putString("adultCount", binding.numberPickerAdults.value.toString())
            bundle.putString("childCount", binding.numberPickerChild.value.toString())
            bundle.putString("roomCount", binding.numberPickerRoom.value.toString())

            findNavController().navigate(
                R.id.action_filterDialogFragment_to_navigation_home,
                bundle
            )
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CountryDialogFragment"
    }
}
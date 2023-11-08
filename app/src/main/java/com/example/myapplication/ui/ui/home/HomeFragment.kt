package com.example.myapplication.ui.ui.home

import android.annotation.SuppressLint
import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.di.AppModule.USER_APP_KEY
import com.example.myapplication.network.ApiState
import com.example.myapplication.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModels by viewModels<MainViewModel>()
    private lateinit var binding: FragmentHomeBinding
    private val cityIdArray = ArrayList<String>()
    private val cityNameArray = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        //   (activity as AppCompatActivity).supportActionBar?.hide()

        getCities()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //optionMenu()


    }
    private fun getCities() {
        lifecycleScope.launch {
            viewModels.getCities(
                USER_APP_KEY,
                "0",
                "0"
            ).collect {
                when (it) {
                    is ApiState.Success -> {
                        if (it.data?.result.equals("success")) {
                            // Populate city data and notify the Spinner to update
                            cityIdArray.clear()
                            cityNameArray.clear()
                            for (value in it.data?.data!!) {
                                cityIdArray.add(value.city_id)
                                cityNameArray.add(value.city_name.trim())
                            }
                            // Notify the Spinner to update its data
                            //updateCitySpinner()
                            optionMenu()
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


    private fun optionMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)

                val itemCountry = menu.findItem(R.id.action_spinner)
                val spinner = itemCountry?.actionView as AppCompatSpinner
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.layout_drop_title, cityNameArray)
                arrayAdapter.setDropDownViewResource(R.layout.layout_drop_list)
                spinner.adapter = arrayAdapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        // Handle item selection here
                        itemCountry.title = cityNameArray[position]
                        showToast(cityIdArray[position] + " ${cityNameArray[position]} selected")
                        // Do something with the selected item
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Handle nothing selected
                    }
                }


            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    /* R.id.action_filter -> {
                         bottomFilterDialog()
                         true
                     }*/

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            requireContext(), msg, Toast.LENGTH_SHORT
        ).show()
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
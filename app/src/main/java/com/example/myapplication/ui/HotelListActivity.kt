package com.example.myapplication.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityHotelListBinding
import com.example.myapplication.di.AppModule
import com.example.myapplication.ui.adapter.HotelPagingAdapter
import com.example.myapplication.ui.adapter.LoaderStateAdapter
import com.example.myapplication.viewmodel.HotelListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HotelListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHotelListBinding
    private val viewModel by viewModels<HotelListViewModel>()

    @Inject
    lateinit var pagingAdapter: HotelPagingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Hotel List"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val cityId = intent.getStringExtra("cityId")
        val checkInDate = intent.getStringExtra("checkInDate")
        val checkOutDate = intent.getStringExtra("checkOutDate")
        val adultCount = intent.getStringExtra("adultCount")
        val childCount = intent.getStringExtra("childCount")
        val roomCount = intent.getStringExtra("roomCount")

        binding.apply {

            lifecycleScope.launch {
                viewModel.HotelPagingList(
                    AppModule.USER_APP_KEY,
                    cityId,
                    checkInDate,
                    checkOutDate,
                    adultCount,
                    childCount,
                    "1",
                ).collect {
                    pagingAdapter.submitData(it)
                }
            }

            lifecycleScope.launch {
                prgBar.isVisible = true
                pagingAdapter.loadStateFlow.collect{
                    val state = it.refresh
                    prgBar.isVisible = state is LoadState.Loading

                    if (pagingAdapter.itemCount < 1){
                        txtError.isVisible = true
                        donationRecycler.isVisible = false
                    }else{
                        txtError.isVisible = false
                        donationRecycler.isVisible = true
                    }
                }
            }

            donationRecycler.apply {
                layoutManager = LinearLayoutManager(this@HotelListActivity)
                adapter = pagingAdapter
            }

            donationRecycler.adapter=pagingAdapter.withLoadStateFooter(
                LoaderStateAdapter{
                    pagingAdapter.retry()
                }
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
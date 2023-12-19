package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.model.paging.Data
import com.example.myapplication.network.ApiService
import com.example.myapplication.paging.DonationRequestPagingList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class HotelListViewModel
@Inject constructor(private val apiService: ApiService) : ViewModel() {

    fun HotelPagingList(
        userAppKey: String?,
        cityId: String?,
        check_in_date: String?,
        check_out_date: String?,
        adult_count: String?,
        child_count: String?,
        latitude: String?,
        longitude: String?,
        hotel_star_id: String?,
        pay_at_hotel: String?,
    ): Flow<PagingData<Data>> {

        return Pager(config = PagingConfig(10, enablePlaceholders = false)) {
            DonationRequestPagingList(userAppKey,cityId,check_in_date,check_out_date,adult_count,child_count,latitude,longitude,hotel_star_id,pay_at_hotel,apiService)
        }.flow.cachedIn(viewModelScope)

    }


}
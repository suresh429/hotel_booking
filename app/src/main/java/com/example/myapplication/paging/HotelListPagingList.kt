package com.example.myapplication.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.model.paging.Data
import com.example.myapplication.model.paging.HotelsListResponse
import com.example.myapplication.network.ApiService

import retrofit2.HttpException
import java.io.IOException

const val INDEX = 0

class DonationRequestPagingList(
    private val userAppKey: String?,
    private val cityId: String?,
    private val check_in_date: String?,
    private val check_out_date: String?,
    private val adult_count: String?,
    private val child_count: String?,
    private val pay_at_hotel: String?,
    private val apiInterface: ApiService
) : PagingSource<Int, Data>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {

        val position = params.key ?: INDEX
        return try {

            // doctor list with map
            val data: HotelsListResponse = apiInterface.getHotels(userAppKey, position, cityId, check_in_date, check_out_date, adult_count, child_count, pay_at_hotel)

            LoadResult.Page(
                data = data.data,
                prevKey = if (position == INDEX) null else position,
                nextKey = if (data.data.isEmpty()) null else position + 10
            )
        } catch (e: Exception) {
            Log.d(TAG, "IOException: 92 $e")
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.d(TAG, "Exception: 95 ${e.message}")
            LoadResult.Error(e)
        } catch (e: IOException) {
            Log.d(TAG, "IOException: 98 $e")
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val TAG = "PagingList"
    }

}
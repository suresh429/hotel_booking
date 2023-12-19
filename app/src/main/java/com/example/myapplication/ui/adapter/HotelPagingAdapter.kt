package com.example.myapplication.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.myapplication.R
import com.example.myapplication.databinding.HotelListItemBinding
import com.example.myapplication.model.paging.Data
import com.example.myapplication.utils.BASE_IMAGE_URL
import com.example.myapplication.utils.COMMONFILES
import com.example.myapplication.utils.format
import com.example.myapplication.utils.isVisible
import java.util.*
import javax.inject.Inject


class HotelPagingAdapter @Inject constructor() :
    PagingDataAdapter<Data, HotelPagingAdapter.MyViewHolder>(DIFF_UTIL) {
    var onItemClick: ((Data) -> Unit)? = null


    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem == newItem
            }
        }
        private const val TAG = "PagingAdapter"
    }

    inner class MyViewHolder(private val binding: HotelListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(response: Data) {

            binding.apply {

                txtTitle.text = response.hotel_name
                //txtHotelsPrice.text = "\u20b9 " + response.base_room_price
                txtHotelsPrice.text = "\u20b9 " + format(response.base_room_price.toDouble()).replace(".00","")
                ratingBar.rating = (response.rating.toFloat() ?: 0.0) as Float
                txtLocation.text = response.location
                txtUserRatings.text = "(${response.rating_users_count} Ratings)"

                if (response.pay_at_hotel == "1") {
                    txtPayType.text = "Pay at Hotel"
                    txtPayType.isVisible = true
                } else {
                    txtPayType.isVisible = false
                }

                if (response.top_hotel == "yes") {
                    txtHotelType.text = "Top Hotel"
                    txtHotelType.isVisible = true
                } else {
                    txtHotelType.isVisible = false
                }

                imgHotel.load("$BASE_IMAGE_URL${response.id}+$COMMONFILES+${response.hotel_pic}") {
                    crossfade(true)
                    crossfade(1000)
                    placeholder(R.drawable.home_banner)
                    error(R.drawable.home_banner)
                    transformations(CircleCropTransformation())
                }


            }

        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        return MyViewHolder(
            HotelListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

}
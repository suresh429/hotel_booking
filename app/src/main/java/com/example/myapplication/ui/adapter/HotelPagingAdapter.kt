package com.example.myapplication.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.myapplication.R
import com.example.myapplication.databinding.HotelListItemBinding
import com.example.myapplication.model.paging.Data
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
                /*imgUser.load(response.user_info.image_url) {
                    crossfade(true)
                    crossfade(1000)
                    placeholder(R.drawable.ic_account_circle)
                    error(R.drawable.ic_account_circle)
                    transformations(CircleCropTransformation())
                }
                txtTitle.text = response.title
                txtDaysAgo.text = (response.due_date?.let { TimeAgo.getTimeAgo(it) }
                    ?: txtDaysAgo.visible(false)).toString()
                cardImage.load(response.defaultImageUrl) {
                    crossfade(true)
                    crossfade(1000)
                    placeholder(R.drawable.app_icon)
                    error(R.drawable.app_icon)
                    // transformations(CircleCropTransformation())
                }*/

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
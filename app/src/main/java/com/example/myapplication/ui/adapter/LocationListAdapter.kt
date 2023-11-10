package com.example.myapplication.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.LocationDialogListItemBinding
import com.example.myapplication.model.CitiesResponse
import javax.inject.Inject

class LocationListAdapter
@Inject
constructor() : ListAdapter<CitiesResponse.Data, LocationListAdapter.HomeViewHolder>(Diff) {
    private var originalList: List<CitiesResponse.Data> = emptyList()

    var onItemClick: ((CitiesResponse.Data, Int) -> Unit)? = null

    inner class HomeViewHolder(private val binding: LocationDialogListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cityResponse : CitiesResponse.Data, position: Int) {
            binding.apply {
                txtLocation.text = cityResponse.city_name
                itemView.setOnClickListener {
                    onItemClick?.invoke(cityResponse,position)
                }
            }

        }
    }

    object Diff : DiffUtil.ItemCallback<CitiesResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: CitiesResponse.Data,
            newItem: CitiesResponse.Data
        ): Boolean {
            return oldItem.city_id == newItem.city_id
        }

        override fun areContentsTheSame(
            oldItem: CitiesResponse.Data,
            newItem: CitiesResponse.Data
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LocationDialogListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val post = getItem(position)
        if (post != null) {
            holder.bind(post,position)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun submitListWithFilter(list: List<CitiesResponse.Data>, filter: String) {
        val filteredList = if (filter.isEmpty()) {
            list // If the filter is empty, show the original list
        } else {
            list.filter { it.city_name.contains(filter, true) }
        }
        submitList(filteredList)
        originalList = list
    }


}
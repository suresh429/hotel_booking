package com.example.myapplication.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.R
import com.example.myapplication.databinding.CardLayoutBinding
import com.example.myapplication.model.Data
import com.example.myapplication.model.HomeDataResponse
import com.example.myapplication.utils.BASE_IMAGE_URL
import com.example.myapplication.utils.COMMONFILES

class HomeAdapter(
    private var homeList: List<Data>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Int,Data) -> Unit)? = null

    override fun getItemCount(): Int {
        return homeList.size
    }

    class ItemViewHolder(var viewBinding: CardLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder

        val data = homeList[position]

        itemViewHolder.viewBinding.apply {
            txtTitle.text = data.hotel_name
            imageView.load("$BASE_IMAGE_URL${data.id}+$COMMONFILES+${data.hotel_pic}"){
                crossfade(true)
                crossfade(1000)
                placeholder(R.drawable.app_icon)
                error(R.drawable.app_icon)
            }
            cardView.setOnClickListener{
                onItemClick?.invoke(position,homeList[position])
            }
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
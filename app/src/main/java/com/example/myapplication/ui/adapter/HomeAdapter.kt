package com.example.myapplication.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.databinding.CardLayoutBinding
import com.example.myapplication.model.HomeDataResponse
import java.util.LinkedList

class HomeAdapter(
    private var homeList: LinkedList<HomeDataResponse>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Int,HomeDataResponse) -> Unit)? = null

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

        itemViewHolder.viewBinding.apply {
            txtTitle.text = homeList[position].name
            imageView.load(homeList[position].url)
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
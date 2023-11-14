package com.example.myapplication.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.MenuListItemBinding
import com.example.myapplication.model.MenuModel
import javax.inject.Inject

class MenuListAdapter
@Inject
constructor() : ListAdapter<MenuModel, MenuListAdapter.HomeViewHolder>(Diff) {

    var onItemClick: ((MenuModel, Int) -> Unit)? = null

   inner class HomeViewHolder(private val binding: MenuListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(menuModel : MenuModel, position: Int) {
            binding.apply {
                txtName.text = menuModel.itemName
                val drawableStart = ContextCompat.getDrawable(itemView.context, menuModel.image)
                val drawableRight = ContextCompat.getDrawable(itemView.context, R.drawable.action_arrow_forward)
                txtName.setCompoundDrawablesWithIntrinsicBounds(drawableStart, null,drawableRight , null)

                txtName.setOnClickListener {
                    onItemClick?.invoke(menuModel,position)
                }

            }



        }
    }

    object Diff : DiffUtil.ItemCallback<MenuModel>() {
        override fun areItemsTheSame(
            oldItem: MenuModel,
            newItem: MenuModel
        ): Boolean {
            return oldItem.itemName == newItem.itemName
        }

        override fun areContentsTheSame(
            oldItem: MenuModel,
            newItem: MenuModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            MenuListItemBinding.inflate(
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
}
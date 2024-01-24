package com.example.ProjetAndroid.View.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ProjetAndroid.R
import com.example.ProjetAndroid.databinding.ItemVnInfoBinding
import com.example.ProjetAndroid.Model.DTOs.Vn


class VnListAdapter :
    PagingDataAdapter<Vn, VnListAdapter.VnItemViewHolder>(DiffCallback) {

    class VnItemViewHolder(val binding: ItemVnInfoBinding) : RecyclerView.ViewHolder(binding.root)

    var onVnClickListener: ((Vn) -> Unit)? = null

    var count = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VnItemViewHolder {
        Log.d("onCreateViewHolder", "onCreateViewHolder, count: ${++count}")
        val binding = ItemVnInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VnItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VnItemViewHolder, position: Int) {

        val vn = getItem(position)
        holder.binding.title.text = vn?.title
        holder.binding.poster.load(vn?.image) {
            crossfade(true)
            crossfade(250)
            placeholder(R.drawable.loading_animation)
            error(R.drawable.gif_error)
        }
        holder.binding.root.setOnClickListener {
            if (vn != null) {
                onVnClickListener?.invoke(vn)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Vn>() {
        override fun areItemsTheSame(oldItem: Vn, newItem: Vn): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vn, newItem: Vn): Boolean {
            return oldItem == newItem
        }
    }
}
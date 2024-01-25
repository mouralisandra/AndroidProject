package com.example.ProjetAndroid.View.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ProjetAndroid.Model.api.DataClasses.Tags
import com.example.ProjetAndroid.databinding.ItemTagBinding

class TagListAdapter : ListAdapter<Tags, TagListAdapter.TagViewHolder>(DiffCallback) {

    class TagViewHolder(val binding: ItemTagBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<Tags>() {
        override fun areItemsTheSame(oldItem: Tags, newItem: Tags): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tags, newItem: Tags): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context))
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = getItem(position)
        Log.d("adapter", getItem(position).name)
        holder.binding.tagName.text = tag.name
    }
}
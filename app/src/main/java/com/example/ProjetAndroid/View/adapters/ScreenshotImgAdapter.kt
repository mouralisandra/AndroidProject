package com.example.ProjetAndroid.View.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ProjetAndroid.R
import com.example.ProjetAndroid.databinding.ScreenshotImgBinding


class ScreenshotImgAdapter :
    ListAdapter<Pair<String, Double>, ScreenshotImgAdapter.ScreenshotImgViewHolder>(DiffCallback) {

    class ScreenshotImgViewHolder(val binding: ScreenshotImgBinding) :
        RecyclerView.ViewHolder(binding.root)

    var onImgClickListener: ((Pair<String, Double>) -> Unit)? = null

    private object DiffCallback : DiffUtil.ItemCallback<Pair<String, Double>>() {
        override fun areItemsTheSame(
            oldItem: Pair<String, Double>,
            newItem: Pair<String, Double>
        ): Boolean {
            return oldItem.first == newItem.first
        }

        override fun areContentsTheSame(
            oldItem: Pair<String, Double>,
            newItem: Pair<String, Double>
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotImgViewHolder {
        val binding = ScreenshotImgBinding.inflate(LayoutInflater.from(parent.context))
        return ScreenshotImgViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScreenshotImgViewHolder, position: Int) {
        val img = getItem(position)
        holder.binding.screenshotImg.load(img.first) {
            crossfade(true)
            crossfade(250)
            placeholder(R.drawable.loading_animation)
            error(R.drawable.gif_error)
            Log.d("img", img.first)
        }
        holder.binding.screenshotImg.setOnClickListener {
            if (img != null) {
                onImgClickListener?.invoke(img)
            }
            Log.d("fragment", "clicked on $position")
        }
    }
}
package com.example.ProjetAndroid.View.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ProjetAndroid.R
import com.example.ProjetAndroid.databinding.ScreenshotFullscreenImgBinding

class ViewPagerAdapter(private val urlList: Array<String>) :
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    class PagerViewHolder(val binding: ScreenshotFullscreenImgBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = ScreenshotFullscreenImgBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val img = urlList[position]
        holder.binding.screenshotFullscreen.load(img.replace(THUMBNAIL_PREFIX, FULL_SIZE_PREFIX)) {
            crossfade(true)
            crossfade(250)
            placeholder(R.drawable.loading_animation)
            error(R.drawable.gif_error)
        }
    }

    override fun getItemCount(): Int {
        return urlList.size
    }

    companion object {
        private const val THUMBNAIL_PREFIX = "/st/"
        private const val FULL_SIZE_PREFIX = "/sf/"
    }
}


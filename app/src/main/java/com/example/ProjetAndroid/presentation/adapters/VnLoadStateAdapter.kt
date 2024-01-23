package com.example.ProjetAndroid.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ProjetAndroid.databinding.VnLoadStateFooterViewItemBinding

class VnLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<VnLoadStateAdapter.VnLoadStateViewHolder>() {

    class VnLoadStateViewHolder(val binding: VnLoadStateFooterViewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VnLoadStateViewHolder, loadState: LoadState) {
        if (loadState is LoadState.Error) {
            holder.binding.errorMsg.text = loadState.error.localizedMessage
        }
        holder.binding.progressBar.isVisible = loadState is LoadState.Loading
        holder.binding.retryButton.isVisible = loadState is LoadState.Error
        holder.binding.errorMsg.isVisible = loadState is LoadState.Error
        holder.binding.retryButton.setOnClickListener { retry.invoke() }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): VnLoadStateViewHolder {
        val binding = VnLoadStateFooterViewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VnLoadStateViewHolder(binding)
    }
}
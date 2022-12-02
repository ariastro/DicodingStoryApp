package io.astronout.dicodingstoryapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.databinding.LayoutStoryLoadingBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        return LoadingStateViewHolder(LayoutStoryLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadingStateViewHolder(private val binding: LayoutStoryLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            with(binding) {
                if (loadState is LoadState.Error) {
                    errorMsg.text = root.context.getString(R.string.loading_error_message)
                }
                retryButton.setOnClickListener { retry() }
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible = loadState is LoadState.Error
            }
        }
    }
}
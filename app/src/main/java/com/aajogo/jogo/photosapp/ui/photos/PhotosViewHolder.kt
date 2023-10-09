package com.aajogo.jogo.photosapp.ui.photos

import androidx.recyclerview.widget.RecyclerView
import com.aajogo.jogo.photosapp.databinding.PhotoLayoutBinding
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import com.bumptech.glide.Glide

class PhotosViewHolder(
    private val binding: PhotoLayoutBinding,
    private val itemClick: (photo: ImageModel) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(photo: ImageModel) {
        Glide.with(itemView.context)
            .load(photo.url)
            .into(binding.imageView)
        binding.dateView.text = photo.date
        itemView.setOnClickListener {
            itemClick(photo)
        }
    }
}
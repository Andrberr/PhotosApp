package com.aajogo.jogo.photosapp.ui.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.aajogo.jogo.photosapp.databinding.PhotoLayoutBinding
import com.aajogo.jogo.photosapp.domain.models.ImageModel

class PhotosAdapter : ListAdapter<ImageModel, PhotosViewHolder>(DiffCallback()) {

    var itemClick: (photo: ImageModel) -> Unit = {}
    var itemDelete: (id: Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = PhotoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding, itemClick, itemDelete)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<ImageModel>() {
        override fun areItemsTheSame(oldPhoto: ImageModel, newPhoto: ImageModel): Boolean {
            return oldPhoto.id == newPhoto.id
        }

        override fun areContentsTheSame(oldPhoto: ImageModel, newPhoto: ImageModel): Boolean {
            return newPhoto == oldPhoto
        }
    }
}
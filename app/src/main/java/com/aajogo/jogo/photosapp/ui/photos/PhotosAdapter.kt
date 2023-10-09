package com.aajogo.jogo.photosapp.ui.photos

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aajogo.jogo.photosapp.databinding.PhotoLayoutBinding
import com.aajogo.jogo.photosapp.domain.models.ImageModel

class PhotosAdapter : RecyclerView.Adapter<PhotosViewHolder>() {

    private val photos = mutableListOf<ImageModel>()
    var itemClick: () -> Unit = {}
    var itemDelete: (id: Int, position: Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = PhotoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding, itemClick, itemDelete)
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(photos[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPhotos(photos: List<ImageModel>) {
        this.photos.clear()
        this.photos.addAll(photos)
        notifyDataSetChanged()
    }
}
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = PhotoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPhotos(photos: List<ImageModel>) {
        this.photos.clear()
        this.photos.addAll(photos)
        notifyDataSetChanged()
    }

    fun addPhoto(photo: ImageModel) {
        this.photos.add(0, photo)
        notifyItemChanged(0)
    }
}
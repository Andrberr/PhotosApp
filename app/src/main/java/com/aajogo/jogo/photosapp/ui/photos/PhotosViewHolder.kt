package com.aajogo.jogo.photosapp.ui.photos

import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.aajogo.jogo.photosapp.R
import com.aajogo.jogo.photosapp.databinding.PhotoLayoutBinding
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import com.bumptech.glide.Glide

class PhotosViewHolder(
    private val binding: PhotoLayoutBinding,
    private val itemClick: (photo: ImageModel) -> Unit,
    private val itemDelete: (id: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(photo: ImageModel) {
        Glide.with(itemView.context)
            .load(photo.url)
            .into(binding.imageView)
        binding.dateView.text = photo.date
        itemView.setOnClickListener {
            itemClick(photo)
        }
        itemView.setOnLongClickListener {
            showDeleteConfirmationDialog(photo.id)
            true
        }
    }

    private fun showDeleteConfirmationDialog(id: Int) {
        val alertDialogBuilder = AlertDialog.Builder(itemView.context)
        alertDialogBuilder.setMessage(itemView.context.getString(R.string.delete_question))

        alertDialogBuilder.setPositiveButton(itemView.context.getString(R.string.yes)) { _, _ ->
            itemDelete(id)
        }

        alertDialogBuilder.setNegativeButton(itemView.context.getString(R.string.no)) { dialog, _ ->
            dialog.cancel()
        }
        alertDialogBuilder.show()
    }
}
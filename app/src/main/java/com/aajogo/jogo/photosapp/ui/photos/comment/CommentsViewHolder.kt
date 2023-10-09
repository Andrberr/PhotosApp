package com.aajogo.jogo.photosapp.ui.photos.comment

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.aajogo.jogo.photosapp.databinding.CommentLayoutBinding
import com.aajogo.jogo.photosapp.domain.models.CommentModel

class CommentsViewHolder(
    private val binding: CommentLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(comment: CommentModel) {
        with(binding) {
            commentView.text = comment.text
            dateView.text = comment.date + " " + comment.time
        }
    }
}
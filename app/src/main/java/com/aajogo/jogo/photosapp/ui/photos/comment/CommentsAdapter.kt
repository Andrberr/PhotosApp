package com.aajogo.jogo.photosapp.ui.photos.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.aajogo.jogo.photosapp.databinding.CommentLayoutBinding
import com.aajogo.jogo.photosapp.domain.models.CommentModel

class CommentsAdapter : ListAdapter<CommentModel, CommentsViewHolder>(DiffCallback()) {

    var itemDelete: (commentId: Int) -> Unit = { }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val binding =
            CommentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsViewHolder(binding, itemDelete)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<CommentModel>() {
        override fun areItemsTheSame(oldComment: CommentModel, newComment: CommentModel): Boolean {
            return oldComment.id == newComment.id
        }

        override fun areContentsTheSame(
            oldComment: CommentModel,
            newComment: CommentModel
        ): Boolean {
            return newComment == oldComment
        }
    }
}
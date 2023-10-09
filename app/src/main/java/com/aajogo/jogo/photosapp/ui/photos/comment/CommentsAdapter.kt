package com.aajogo.jogo.photosapp.ui.photos.comment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aajogo.jogo.photosapp.databinding.CommentLayoutBinding
import com.aajogo.jogo.photosapp.domain.models.CommentModel

class CommentsAdapter : RecyclerView.Adapter<CommentsViewHolder>() {

    private val comments = mutableListOf<CommentModel>()
    var itemDelete: (commentId: Int) -> Unit = { }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val binding =
            CommentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsViewHolder(binding, itemDelete)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setComments(comments: List<CommentModel>) {
        this.comments.clear()
        this.comments.addAll(comments)
        notifyDataSetChanged()
    }
}
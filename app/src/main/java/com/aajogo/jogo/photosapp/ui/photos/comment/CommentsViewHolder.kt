package com.aajogo.jogo.photosapp.ui.photos.comment

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.aajogo.jogo.photosapp.databinding.CommentLayoutBinding
import com.aajogo.jogo.photosapp.domain.models.CommentModel

class CommentsViewHolder(
    private val binding: CommentLayoutBinding,
    private val itemDelete: (commentId: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(comment: CommentModel) {
        with(binding) {
            commentView.text = comment.text
            dateView.text = comment.date + " " + comment.time
            itemView.setOnLongClickListener {
                showDeleteConfirmationDialog(comment.id)
                true
            }
        }
    }

    private fun showDeleteConfirmationDialog(commentId: Int) {
        val alertDialogBuilder = AlertDialog.Builder(itemView.context)
        alertDialogBuilder.setTitle("")
        alertDialogBuilder.setMessage("")

        alertDialogBuilder.setPositiveButton("") { _, _ ->
            itemDelete(commentId)
        }

        alertDialogBuilder.setNegativeButton("") { dialog, _ ->
            dialog.cancel()
        }

        alertDialogBuilder.show()
    }

}
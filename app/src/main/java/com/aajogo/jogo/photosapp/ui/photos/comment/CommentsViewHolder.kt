package com.aajogo.jogo.photosapp.ui.photos.comment

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.aajogo.jogo.photosapp.R
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

    private fun showDeleteConfirmationDialog(id: Int) {
        val alertDialogBuilder = AlertDialog.Builder(itemView.context)
        alertDialogBuilder.setMessage(itemView.context.getString(R.string.delete_question_comment))

        alertDialogBuilder.setPositiveButton(itemView.context.getString(R.string.yes)) { _, _ ->
            itemDelete(id)
        }

        alertDialogBuilder.setNegativeButton(itemView.context.getString(R.string.no)) { dialog, _ ->
            dialog.cancel()
        }
        alertDialogBuilder.show()
    }
}
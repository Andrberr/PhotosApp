package com.aajogo.jogo.photosapp.domain.models

data class CommentModel(
    val id: Int,
    val date: String,
    val time: String,
    val text: String
) {
    companion object {
        fun empty() = CommentModel(0, "", "", "")
    }
}

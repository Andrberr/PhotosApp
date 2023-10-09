package com.aajogo.jogo.photosapp.domain.repository

import com.aajogo.jogo.photosapp.domain.models.CommentModel

interface CommentRepository {
    suspend fun addComment(comment: String, imageId: Int): CommentModel
    suspend fun getComments(imageId: Int): List<CommentModel>
}
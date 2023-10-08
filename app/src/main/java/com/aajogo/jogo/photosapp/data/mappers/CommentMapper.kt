package com.aajogo.jogo.photosapp.data.mappers

import com.aajogo.jogo.photosapp.data.models.data.CommentDto
import com.aajogo.jogo.photosapp.data.models.data.CommentResponse
import com.aajogo.jogo.photosapp.domain.models.CommentModel
import javax.inject.Inject

class CommentMapper @Inject constructor() {
    operator fun invoke(comment: String) =
        CommentDto(text = comment)

    fun mapResponseToModel(response: CommentResponse) =
        with(response){
            CommentModel(
                id = id ?: 0,
                date = date ?: "",
                text = text ?: ""
            )
        }
}
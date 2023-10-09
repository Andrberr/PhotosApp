package com.aajogo.jogo.photosapp.data.mappers

import android.text.format.DateFormat
import com.aajogo.jogo.photosapp.data.models.data.CommentDto
import com.aajogo.jogo.photosapp.data.models.data.CommentResponse
import com.aajogo.jogo.photosapp.domain.models.CommentModel
import java.util.*
import javax.inject.Inject

class CommentMapper @Inject constructor() {
    operator fun invoke(comment: String) =
        CommentDto(text = comment)

    fun mapResponseToModel(response: CommentResponse): CommentModel {
        with(response) {
            var commentDate = ""
            var commentTime = ""
            if (date != null) {
                val dateTime = getDate(date.toLong()).split(" ")
                commentDate = dateTime[0]
                commentTime = dateTime[1]
            }
            return CommentModel(
                id = id ?: 0,
                date = commentDate,
                time = commentTime,
                text = text ?: ""
            )
        }
    }

    private fun getDate(timestamp: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestamp * 1000L
        return DateFormat.format("dd.MM.yyyy HH:mm:ss", cal).toString()
    }
}
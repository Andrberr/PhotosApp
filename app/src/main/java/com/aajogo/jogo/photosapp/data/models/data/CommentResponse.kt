package com.aajogo.jogo.photosapp.data.models.data

import com.squareup.moshi.Json

data class CommentResponse(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "date") val date: String? = null,
    @Json(name = "text") val text: String? = null,
)
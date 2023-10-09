package com.aajogo.jogo.photosapp.data.models.data

import com.squareup.moshi.Json

data class CommentDataResponse(
    @Json(name = "data") val commentResponse: CommentResponse? = null
)

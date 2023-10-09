package com.aajogo.jogo.photosapp.data.models.data

import com.squareup.moshi.Json

data class CommentDataListResponse(
    @Json(name = "data") val list: List<CommentResponse>? = null
)

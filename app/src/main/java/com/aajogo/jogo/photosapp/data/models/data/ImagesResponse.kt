package com.aajogo.jogo.photosapp.data.models.data

import com.squareup.moshi.Json

data class ImagesResponse(
    @Json(name = "data") val images: List<ImageResponse>? = null
)

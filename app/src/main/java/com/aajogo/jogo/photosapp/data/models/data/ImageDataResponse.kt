package com.aajogo.jogo.photosapp.data.models.data

import com.squareup.moshi.Json

data class ImageDataResponse(
    @Json(name = "data") val photoData: ImageResponse? = null
)

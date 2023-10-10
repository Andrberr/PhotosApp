package com.aajogo.jogo.photosapp.data.models.data

import com.squareup.moshi.Json

data class ImageResponse(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "url") val url: String? = null,
    @Json(name = "date") val date: Int? = null,
    @Json(name = "lat") val lat: Double? = null,
    @Json(name = "lng") val lng: Double? = null,
)

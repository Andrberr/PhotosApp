package com.aajogo.jogo.photosapp.data.models.data

import com.squareup.moshi.Json

data class UserResponse(
    @Json(name = "userId") val id: Int? = null,
    @Json(name = "login") val login: String? = null,
    @Json(name = "token") val token: String? = null
)

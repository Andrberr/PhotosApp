package com.aajogo.jogo.photosapp.data.models.data

import com.squareup.moshi.Json

data class UserDataResponse(
    @Json(name = "data") val user: UserResponse? = null,
)

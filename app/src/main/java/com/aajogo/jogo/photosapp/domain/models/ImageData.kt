package com.aajogo.jogo.photosapp.domain.models

data class ImageData(
    val base64Image: String,
    val date: Int,
    val lat: Double,
    val lng: Double
)

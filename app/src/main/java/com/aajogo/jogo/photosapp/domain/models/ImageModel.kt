package com.aajogo.jogo.photosapp.domain.models

data class ImageModel(
    val id: Int,
    val url: String,
    val date: String,
    val time: String,
    val lat: Double,
    val lng: Double
) {
    companion object {
        fun empty() = ImageModel(0, "", "", "", 0.0, 0.0)
    }
}
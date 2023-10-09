package com.aajogo.jogo.photosapp.domain.models

data class ImageModel(
    val id: Int,
    val url: String,
    val date: String,
    val time: String,
    val lat: Int,
    val lng: Int
) {
    companion object {
        fun empty() = ImageModel(0, "", "", "", 0, 0)
    }
}
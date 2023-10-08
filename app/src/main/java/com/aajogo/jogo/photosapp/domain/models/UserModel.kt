package com.aajogo.jogo.photosapp.domain.models

data class UserModel(
    val id: Int,
    val login: String,
    val token: String,
) {
    companion object {
        fun empty() = UserModel(0, "", "")
    }
}

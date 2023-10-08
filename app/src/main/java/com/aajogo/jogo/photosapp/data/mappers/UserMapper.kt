package com.aajogo.jogo.photosapp.data.mappers

import com.aajogo.jogo.photosapp.data.models.data.UserDataResponse
import com.aajogo.jogo.photosapp.data.models.data.UserDto
import com.aajogo.jogo.photosapp.domain.models.UserData
import com.aajogo.jogo.photosapp.domain.models.UserModel
import javax.inject.Inject

class UserMapper @Inject constructor() {
    operator fun invoke(data: UserData) = with(data) {
        UserDto(
            login = login,
            password = password
        )
    }

    fun mapResponseToModel(response: UserDataResponse): UserModel {
        val user = response.user ?: return UserModel.empty()
        return UserModel(
            id = user.id ?: 0,
            login = user.login ?: "",
            token = user.token ?: "",
        )
    }
}
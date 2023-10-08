package com.aajogo.jogo.photosapp.domain.repository

import com.aajogo.jogo.photosapp.domain.models.UserData
import com.aajogo.jogo.photosapp.domain.models.UserModel

interface SignRepository {
    suspend fun signUp(userData: UserData)
    suspend fun signIn(userData: UserData)
    suspend fun saveUserData(userModel: UserModel)
    suspend fun getUserLogin(): String
}
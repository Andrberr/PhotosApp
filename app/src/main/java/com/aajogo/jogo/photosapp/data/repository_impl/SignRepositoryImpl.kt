package com.aajogo.jogo.photosapp.data.repository_impl

import com.aajogo.jogo.photosapp.data.mappers.UserMapper
import com.aajogo.jogo.photosapp.data.network.Service
import com.aajogo.jogo.photosapp.data.sources.PrefsSource
import com.aajogo.jogo.photosapp.domain.repository.SignRepository
import com.aajogo.jogo.photosapp.domain.models.UserData
import com.aajogo.jogo.photosapp.domain.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val service: Service,
    private val userMapper: UserMapper,
    private val prefsSource: PrefsSource
) : SignRepository {
    override suspend fun signUp(userData: UserData) {
        return withContext(Dispatchers.IO) {
            val response = service.signUp(userMapper(userData))
            val user = userMapper.mapResponseToModel(response)
            saveUserData(user)
        }
    }

    override suspend fun signIn(userData: UserData) {
        return withContext(Dispatchers.IO) {
            val response = service.signIn(userMapper(userData))
            val user = userMapper.mapResponseToModel(response)
            saveUserData(user)
        }
    }

    override suspend fun saveUserData(userModel: UserModel) {
        withContext(Dispatchers.IO) {
            prefsSource.setLogin(userModel.login)
            prefsSource.setToken(userModel.token)
        }
    }

    override suspend fun getUserLogin(): String {
        return withContext(Dispatchers.IO) {
            prefsSource.getLogin()
        }
    }
}
package com.aajogo.jogo.photosapp.data.sources

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class PrefsSource @Inject constructor(
    private val prefs: SharedPreferences
) {

    fun getToken(): String = prefs.getString(TOKEN_KEY, EMPTY_STRING).orEmpty()

    fun setToken(token: String) = prefs.edit {
        putString(TOKEN_KEY, token)
    }

    fun getLogin(): String = prefs.getString(LOGIN_KEY, EMPTY_STRING).orEmpty()

    fun setLogin(login: String) = prefs.edit {
        putString(LOGIN_KEY, login)
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val LOGIN_KEY = "LOGIN_KEY"
    }
}
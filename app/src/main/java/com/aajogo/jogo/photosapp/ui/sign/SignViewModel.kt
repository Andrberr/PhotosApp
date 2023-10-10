package com.aajogo.jogo.photosapp.ui.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aajogo.jogo.photosapp.domain.repository.SignRepository
import com.aajogo.jogo.photosapp.domain.models.UserData
import com.aajogo.jogo.photosapp.ui.sign.SignInFragment.Companion.INCORRECT
import com.aajogo.jogo.photosapp.ui.sign.SignUpFragment.Companion.ALREADY_USE_ERROR
import com.aajogo.jogo.photosapp.ui.sign.SignUpFragment.Companion.NETWORK_ERROR
import com.aajogo.jogo.photosapp.ui.sign.SignUpFragment.Companion.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor(
    private val signRepository: SignRepository
) : ViewModel() {

    private var _isSignIn = MutableLiveData<Pair<Boolean, Int>>()
    val isSignIn: LiveData<Pair<Boolean, Int>> = _isSignIn

    private var _isSignUp = MutableLiveData<Pair<Boolean, Int>>()
    val isSignUp: LiveData<Pair<Boolean, Int>> = _isSignUp

    private var _login = MutableLiveData<String>()
    val login: LiveData<String> = _login

    private var _isMenuInitialized = MutableLiveData<Boolean>()
    val isMenuInitialized: LiveData<Boolean> = _isMenuInitialized

    private val signInHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            val error = if (throwable is UnknownHostException){
                NETWORK_ERROR
            } else INCORRECT
            _isSignIn.value = false to error
        }
    }
    private val signUpHandler = CoroutineExceptionHandler { _, throwable  ->
        viewModelScope.launch {
            val error = if (throwable is UnknownHostException){
                NETWORK_ERROR
            } else ALREADY_USE_ERROR
            _isSignUp.value = false to error
        }
    }

    fun signUp(userData: UserData) {
        viewModelScope.launch(signUpHandler) {
            signRepository.signUp(userData)
            _isSignUp.value = true to SUCCESS
        }
    }

    fun signIn(userData: UserData) {
        viewModelScope.launch(signInHandler) {
            signRepository.signIn(userData)
            _isSignIn.value = true to SUCCESS
        }
    }

    fun getUserLogin() {
        viewModelScope.launch {
            _login.value = signRepository.getUserLogin()
        }
    }

    fun setMenuInitialized(isInitialized: Boolean) {
        _isMenuInitialized.value = isInitialized
    }
}
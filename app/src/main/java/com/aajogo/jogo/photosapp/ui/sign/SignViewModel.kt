package com.aajogo.jogo.photosapp.ui.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aajogo.jogo.photosapp.domain.repository.SignRepository
import com.aajogo.jogo.photosapp.domain.models.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor(
    private val signRepository: SignRepository
) : ViewModel() {

    private var _isSignIn = MutableLiveData<Boolean>()
    val isSignIn: LiveData<Boolean> = _isSignIn

    private var _isSignUp = MutableLiveData<Boolean>()
    val isSignUp: LiveData<Boolean> = _isSignUp

    private var _login = MutableLiveData<String>()
    val login: LiveData<String> = _login

    private var _isMenuInitialized = MutableLiveData<Boolean>()
    val isMenuInitialized: LiveData<Boolean> = _isMenuInitialized

    private val signInHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _isSignIn.value = false
        }
    }
    private val signUpHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _isSignUp.value = false
        }
    }

    fun signUp(userData: UserData) {
        viewModelScope.launch(signUpHandler) {
            signRepository.signUp(userData)
            _isSignUp.value = true
        }
    }

    fun signIn(userData: UserData) {
        viewModelScope.launch(signInHandler) {
            signRepository.signIn(userData)
            _isSignIn.value = true
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
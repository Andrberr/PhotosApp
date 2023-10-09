package com.aajogo.jogo.photosapp.ui.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aajogo.jogo.photosapp.domain.models.ImageData
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import com.aajogo.jogo.photosapp.domain.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val repository: PhotosRepository
) : ViewModel() {

    private var _photos = MutableLiveData<List<ImageModel>>()
    val photos: LiveData<List<ImageModel>> = _photos

    private var _savePhoto = MutableLiveData<ImageModel>()
    val savePhoto: LiveData<ImageModel> = _savePhoto

    private var _errorUpload = MutableLiveData<Boolean>()
    val errorUpload: LiveData<Boolean> = _errorUpload

    private var _errorDelete = MutableLiveData<Boolean>()
    val errorDelete: LiveData<Boolean> = _errorDelete

    var base64Img: String? = null
    var deleteId = 0
    var deletePosition = 0

    private val getHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _photos.value = repository.getPhotosFromDataBase()
        }
    }

    private val uploadHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _errorUpload.value = true
        }
    }

    private val deleteHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _errorDelete.value = true
        }
    }

    fun getPhotos() {
        viewModelScope.launch(getHandler) {
            _photos.value = repository.getPhotos()
        }
    }

    fun uploadPhoto(photo: ImageData) {
        viewModelScope.launch(uploadHandler) {
            _errorUpload.value = false
            _savePhoto.value = repository.uploadPhoto(photo)
        }
    }

    fun deletePhoto(id: Int) {
        viewModelScope.launch(deleteHandler) {
            repository.deletePhoto(id)
            _errorDelete.value = false
        }
    }

    fun savePhotoToDataBase(photo: ImageModel) {
        viewModelScope.launch {
            repository.savePhotoToDataBase(photo)
        }
    }

    fun deletePhotoFromDataBase() {
        viewModelScope.launch {
            repository.deletePhotoFromDataBase(deleteId)
        }
    }
}
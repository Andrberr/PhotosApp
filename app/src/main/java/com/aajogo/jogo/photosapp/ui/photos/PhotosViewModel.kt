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
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val repository: PhotosRepository
) : ViewModel() {

    private var _photos = MutableLiveData<List<ImageModel>>()
    val photos: LiveData<List<ImageModel>> = _photos

    private var _savePhoto = MutableLiveData<ImageModel>()
    val savePhoto: LiveData<ImageModel> = _savePhoto

    var base64Img: String? = null

    private val networkHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _photos.value = repository.getPhotosFromDataBase()
        }
    }

    fun getPhotos() {
        viewModelScope.launch(networkHandler) {
            _photos.value = repository.getPhotos()
        }
    }

    fun uploadPhoto(photo: ImageData) {
        viewModelScope.launch {
            _savePhoto.value = repository.uploadPhoto(photo)
        }
    }

    fun savePhotoToDataBase(photo: ImageModel) {
        viewModelScope.launch {
            repository.savePhotoToDataBase(photo)
        }
    }
}
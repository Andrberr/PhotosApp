package com.aajogo.jogo.photosapp.ui.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aajogo.jogo.photosapp.domain.models.ImageData
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import com.aajogo.jogo.photosapp.domain.repository.PhotosRepository
import com.aajogo.jogo.photosapp.ui.photos.PhotosFragment.Companion.ERROR_DELETE_ID
import com.yandex.mapkit.geometry.Point
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

    private var _errorUpload = MutableLiveData<Boolean>()
    val errorUpload: LiveData<Boolean> = _errorUpload

    private var _errorDelete = MutableLiveData<Boolean>()
    val errorDelete: LiveData<Boolean> = _errorDelete

    private var _markers = MutableLiveData<List<Point>>()
    val markers: LiveData<List<Point>> = _markers

    private var _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    var base64Img: String? = null
    var deleteId = ERROR_DELETE_ID

    private val getPhotosHandler = CoroutineExceptionHandler { _, _ ->
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
        viewModelScope.launch(getPhotosHandler) {
            _photos.value = repository.getPhotos()
        }
    }

    fun uploadPhoto(photo: ImageData) {
        viewModelScope.launch(uploadHandler) {
            _savePhoto.value = repository.uploadPhoto(photo)
            _errorUpload.value = false
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
            if (deleteId != ERROR_DELETE_ID) repository.deletePhotoFromDataBase(deleteId)
        }
    }

    fun savePhotoAndMarkerToDataBase(photo: ImageModel) {
        viewModelScope.launch {
            repository.savePhotoToDataBase(photo)
            _isSaved.value = true
            _isSaved.value = false
        }
    }

    fun getMarkers() {
        viewModelScope.launch {
            _markers.value = repository.getMarkers()
        }
    }
}
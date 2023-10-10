package com.aajogo.jogo.photosapp.domain.repository

import com.aajogo.jogo.photosapp.domain.models.ImageData
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import com.yandex.mapkit.geometry.Point

interface PhotosRepository {
    suspend fun getPhotos(): List<ImageModel>
    suspend fun uploadPhoto(photo: ImageData): ImageModel
    suspend fun deletePhoto(id: Int)
    suspend fun savePhotoToDataBase(photo: ImageModel)
    suspend fun getPhotosFromDataBase(): List<ImageModel>
    suspend fun deletePhotoFromDataBase(id: Int)
    suspend fun getMarkers(): List<Point>
}
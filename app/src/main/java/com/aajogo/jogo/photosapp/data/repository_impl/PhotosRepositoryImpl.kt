package com.aajogo.jogo.photosapp.data.repository_impl

import com.aajogo.jogo.photosapp.data.mappers.ImageMapper
import com.aajogo.jogo.photosapp.data.models.realm.ImageRealm
import com.aajogo.jogo.photosapp.data.network.Service
import com.aajogo.jogo.photosapp.data.sources.PrefsSource
import com.aajogo.jogo.photosapp.domain.models.ImageData
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import com.aajogo.jogo.photosapp.domain.repository.PhotosRepository
import com.yandex.mapkit.geometry.Point
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val service: Service,
    private val prefsSource: PrefsSource,
    private val realm: Realm,
    private val imageMapper: ImageMapper,
) : PhotosRepository {
    override suspend fun getPhotos(): List<ImageModel> {
        return withContext(Dispatchers.IO) {
            val token = prefsSource.getToken()
            val response = service.getImages(token, 0)
            response.images?.map { imageMapper(it) } ?: emptyList()
        }
    }

    override suspend fun uploadPhoto(photo: ImageData): ImageModel {
        return runBlocking(Dispatchers.IO) {
            val token = prefsSource.getToken()
            val response = service.uploadImage(token, imageMapper.mapDataToDto(photo))
            if (response.photoData != null) {
                imageMapper(response.photoData)
            } else ImageModel.empty()
        }
    }

    override suspend fun deletePhoto(id: Int) {
        runBlocking(Dispatchers.IO) {
            service.deleteImage(prefsSource.getToken(), id)
        }
    }

    override suspend fun savePhotoToDataBase(photo: ImageModel) {
        withContext(Dispatchers.IO) {
            realm.write {
                copyToRealm(imageMapper.mapToRealm(photo))
            }
        }
    }

    override suspend fun getPhotosFromDataBase(): List<ImageModel> {
        return withContext(Dispatchers.IO) {
            val photos: RealmResults<ImageRealm> = realm.query<ImageRealm>().find()
            photos.map { imageMapper.mapFromRealm(it) }.reversed()
        }
    }

    override suspend fun deletePhotoFromDataBase(id: Int) {
        withContext(Dispatchers.IO) {
            realm.write {
                val photo = this.query<ImageRealm>("id == $0", id).find().first()
                delete(photo)
            }
        }
    }

    override suspend fun getMarkers(): List<Point> {
        return withContext(Dispatchers.IO) {
            val photos = getPhotosFromDataBase()
            photos.map { imageMapper.getMarker(it) }
        }
    }
}
package com.aajogo.jogo.photosapp.data.mappers

import android.text.format.DateFormat
import com.aajogo.jogo.photosapp.data.models.data.ImageDto
import com.aajogo.jogo.photosapp.data.models.data.ImageResponse
import com.aajogo.jogo.photosapp.data.models.realm.ImageRealm
import com.aajogo.jogo.photosapp.domain.models.ImageData
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import java.util.*
import javax.inject.Inject

class ImageMapper @Inject constructor() {

    operator fun invoke(unmapped: ImageResponse) = with(unmapped) {
        var photoDate = ""
        var photoTime = ""
        if (date != null) {
            val dateTime = getDate(date.toLong()).split(" ")
            photoDate = dateTime[0]
            photoTime = dateTime[1]
        }
        ImageModel(
            id = id ?: 0,
            url = url ?: "",
            date = photoDate,
            time = photoTime,
            lat = lat ?: 0,
            lng = lng ?: 0,
        )
    }

    fun mapDataToDto(data: ImageData) = with(data) {
        ImageDto(
            base64Image = base64Image,
            date = date,
            lat = lat,
            lng = lng
        )
    }

    fun mapToRealm(model: ImageModel): ImageRealm {
        return ImageRealm().apply {
            id = model.id
            url = model.url
            date = model.date
            time = model.time
            lat = model.lat
            lng = model.lng
        }
    }

    fun mapFromRealm(realm: ImageRealm) = with(realm) {
        ImageModel(
            id, url, date, time, lat, lng
        )
    }

    private fun getDate(timestamp: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestamp * 1000L
        return DateFormat.format("dd.MM.yyyy HH:mm:ss", cal).toString()
    }
}
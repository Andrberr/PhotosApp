package com.aajogo.jogo.photosapp.data.models.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ImageRealm : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var url: String = ""
    var date: String = ""
    var time: String = ""
    var lat: Double = 0.0
    var lng: Double = 0.0
}
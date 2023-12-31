package com.aajogo.jogo.photosapp.ui.map

import android.content.Context
import com.yandex.mapkit.MapKitFactory

object MapKitInitializer {

    private var initialized = false

    fun initialize(apiKey: String, context: Context) {
        if (initialized) {
            return
        }

        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.initialize(context)
        initialized = true
    }
}
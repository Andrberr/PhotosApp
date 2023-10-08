package com.aajogo.jogo.photosapp.di

import com.aajogo.jogo.photosapp.data.models.realm.ImageRealm
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    fun getDataBase(): Realm {
        val configuration = RealmConfiguration.create(schema = setOf(ImageRealm::class))
        return Realm.open(configuration)
    }
}
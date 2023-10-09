package com.aajogo.jogo.photosapp.di

import com.aajogo.jogo.photosapp.data.repository_impl.CommentRepositoryImpl
import com.aajogo.jogo.photosapp.data.repository_impl.PhotosRepositoryImpl
import com.aajogo.jogo.photosapp.data.repository_impl.SignRepositoryImpl
import com.aajogo.jogo.photosapp.domain.repository.CommentRepository
import com.aajogo.jogo.photosapp.domain.repository.PhotosRepository
import com.aajogo.jogo.photosapp.domain.repository.SignRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun getSignRepository(impl: SignRepositoryImpl): SignRepository

    @Binds
    abstract fun getPhotosRepository(impl: PhotosRepositoryImpl): PhotosRepository

    @Binds
    abstract fun getCommentRepository(impl: CommentRepositoryImpl): CommentRepository
}
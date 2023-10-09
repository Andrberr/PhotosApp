package com.aajogo.jogo.photosapp.data.network

import com.aajogo.jogo.photosapp.data.models.data.ImageDataResponse
import com.aajogo.jogo.photosapp.data.models.data.ImageDto
import com.aajogo.jogo.photosapp.data.models.data.ImageResponse
import com.aajogo.jogo.photosapp.data.models.data.ImagesResponse
import com.aajogo.jogo.photosapp.data.models.data.UserDataResponse
import com.aajogo.jogo.photosapp.data.models.data.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @POST("api/account/signup")
    suspend fun signUp(@Body userDto: UserDto): UserDataResponse

    @POST("api/account/signin")
    suspend fun signIn(@Body userDto: UserDto): UserDataResponse

    @GET("api/image")
    suspend fun getImages(
        @Header("Access-Token") token: String,
        @Query("page") page: Int
    ): ImagesResponse

    @POST("api/image")
    suspend fun uploadImage(
        @Header("Access-Token") token: String,
        @Body imageDto: ImageDto
    ): ImageDataResponse

    @DELETE("api/image/{id}")
    suspend fun deleteImage(
        @Header("Access-Token") token: String,
        @Path("id") id: Int
    )
}
package com.aajogo.jogo.photosapp.data.network

import com.aajogo.jogo.photosapp.data.models.data.CommentDataListResponse
import com.aajogo.jogo.photosapp.data.models.data.CommentDataResponse
import com.aajogo.jogo.photosapp.data.models.data.CommentDto
import com.aajogo.jogo.photosapp.data.models.data.ImageDataResponse
import com.aajogo.jogo.photosapp.data.models.data.ImageDto
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

    @POST("api/image/{imageId}/comment")
    suspend fun addComment(
        @Header("Access-Token") token: String,
        @Body commentDto: CommentDto,
        @Path("imageId") imageId: Int
    ): CommentDataResponse

    @POST("api/image/{imageId}/comment/{commentId}")
    suspend fun deleteComment(
        @Header("Access-Token") token: String,
        @Path("imageId") imageId: Int,
        @Path("commentId") commentId: Int
    )

    @GET("api/image/{imageId}/comment")
    suspend fun getComments(
        @Header("Access-Token") token: String,
        @Path("imageId") imageId: Int,
        @Query("page") page: Int
    ): CommentDataListResponse
}
package com.aajogo.jogo.photosapp.data.repository_impl

import com.aajogo.jogo.photosapp.data.mappers.CommentMapper
import com.aajogo.jogo.photosapp.data.network.Service
import com.aajogo.jogo.photosapp.data.sources.PrefsSource
import com.aajogo.jogo.photosapp.domain.models.CommentModel
import com.aajogo.jogo.photosapp.domain.repository.CommentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val prefsSource: PrefsSource,
    private val service: Service,
    private val mapper: CommentMapper
) : CommentRepository {
    override suspend fun addComment(comment: String, imageId: Int): CommentModel {
        return runBlocking(Dispatchers.IO) {
            val token = prefsSource.getToken()
            val response = service.addComment(token, mapper(comment), imageId)
            if (response.commentResponse != null) {
                mapper.mapResponseToModel(response.commentResponse)
            } else CommentModel.empty()
        }
    }

    override suspend fun deleteComment(imageId: Int, commentId: Int) {
        return runBlocking(Dispatchers.IO) {
            service.deleteComment(prefsSource.getToken(), imageId, commentId)
        }
    }

    override suspend fun getComments(imageId: Int): List<CommentModel> {
        return withContext(Dispatchers.IO) {
            val response = service.getComments(prefsSource.getToken(), imageId, 0)
            if (response.list != null) {
                response.list.map { mapper.mapResponseToModel(it) }
            } else emptyList()
        }
    }
}
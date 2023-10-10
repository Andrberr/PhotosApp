package com.aajogo.jogo.photosapp.ui.photos.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aajogo.jogo.photosapp.domain.models.CommentModel
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import com.aajogo.jogo.photosapp.domain.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val repository: CommentRepository
) : ViewModel() {

    private var _comments = MutableLiveData<List<CommentModel>>()
    val comments: LiveData<List<CommentModel>> = _comments

    private var _photo = MutableLiveData<ImageModel>()
    val photo: LiveData<ImageModel> = _photo

    private var _addError = MutableLiveData<Boolean>()
    val addError: LiveData<Boolean> = _addError

    private var _deleteError = MutableLiveData<Boolean>()
    val deleteError: LiveData<Boolean> = _deleteError

    private var _getError = MutableLiveData<Boolean>()
    val getError: LiveData<Boolean> = _getError

    var imageId = 0

    private val addHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _addError.value = true
        }
    }

    private val deleteHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _deleteError.value = true
        }
    }

    private val getHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _getError.value = true
        }
    }

    fun addComment(comment: String, imageId: Int) {
        viewModelScope.launch(addHandler) {
            repository.addComment(comment, imageId)
            _addError.value = false
        }
    }

    fun deleteComment(commentId: Int) {
        viewModelScope.launch(deleteHandler) {
            repository.deleteComment(imageId, commentId)
            _deleteError.value = false
        }
    }

    fun getComments() {
        viewModelScope.launch(getHandler) {
            _comments.value = repository.getComments(imageId)
            _getError.value = false
        }
    }

    fun setPhoto(photo: ImageModel) {
        _comments.value = emptyList()
        _photo.value = photo
    }
}
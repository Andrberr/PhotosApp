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

    fun addComment(comment: String, imageId: Int) {
        viewModelScope.launch(addHandler) {
            _addError.value = false
            repository.addComment(comment, imageId)
        }
    }

    fun deleteComment(imageId: Int, commentId: Int) {
        viewModelScope.launch(deleteHandler) {
            _deleteError.value = false
            repository.deleteComment(imageId, commentId)
        }
    }

    fun getComments(imageId: Int) {
        viewModelScope.launch {
            _comments.value = repository.getComments(imageId)
        }
    }

    fun setPhoto(photo: ImageModel) {
        _photo.value = photo
    }
}
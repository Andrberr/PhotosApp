package com.aajogo.jogo.photosapp.ui.photos.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aajogo.jogo.photosapp.domain.models.CommentModel
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import com.aajogo.jogo.photosapp.domain.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun addComment(comment: String, imageId: Int) {
        viewModelScope.launch {
            repository.addComment(comment, imageId)
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
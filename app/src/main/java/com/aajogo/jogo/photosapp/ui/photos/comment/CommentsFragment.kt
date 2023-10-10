package com.aajogo.jogo.photosapp.ui.photos.comment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aajogo.jogo.photosapp.R
import com.aajogo.jogo.photosapp.databinding.FragmentCommentsBinding
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import com.aajogo.jogo.photosapp.ui.MainActivity
import com.aajogo.jogo.photosapp.ui.photos.PhotosFragment
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentsFragment : Fragment() {

    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!

    private val commentsViewModel by activityViewModels<CommentsViewModel>()

    private val commentsAdapter = CommentsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onBackPressed()
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
    }

    private fun initObservers() {
        commentsViewModel.comments.observe(viewLifecycleOwner) { comments ->
            commentsAdapter.setComments(comments)
        }
        commentsViewModel.photo.observe(viewLifecycleOwner) { photo ->
            commentsViewModel.imageId = photo.id
            commentsViewModel.getComments()
            initViews(photo)
        }
        commentsViewModel.addError.observe(viewLifecycleOwner) { isError ->
            updateComments(isError)
        }
        commentsViewModel.deleteError.observe(viewLifecycleOwner) { isError ->
            updateComments(isError)
        }
        commentsViewModel.getError.observe(viewLifecycleOwner) { isError ->
            binding.lottieError.isVisible = isError
        }
    }

    private fun updateComments(isError: Boolean) {
        if (isError) showError()
        else commentsViewModel.getComments()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(photo: ImageModel) {
        with(binding) {
            loadPhoto(photo.url)
            dateView.text = photo.date + SPACE + photo.time
            commentsAdapter.itemDelete = { commentId ->
                commentsViewModel.deleteComment(commentId)
            }
            commentsRecycler.apply {
                adapter = commentsAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            addCommentButton.setOnClickListener {
                if (commentEdit.text.isNotBlank()) {
                    commentsViewModel.addComment(commentEdit.text.toString(), photo.id)
                }
            }
        }
    }

    private fun loadPhoto(url: String) {
        Glide.with(requireContext())
            .load(url)
            .into(binding.photoView)
    }

    private fun showError() {
        Toast.makeText(requireContext(), getString(R.string.network_error), Toast.LENGTH_SHORT)
            .show()
    }

    private fun navigateToPhotos() {
        val action = CommentsFragmentDirections.actionCommentFragmentToPhotosFragment()
        findNavController().navigate(action)
    }

    fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateToPhotos()
                }
            })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val SPACE = " "
    }
}
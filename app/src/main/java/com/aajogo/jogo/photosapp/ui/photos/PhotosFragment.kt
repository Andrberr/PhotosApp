package com.aajogo.jogo.photosapp.ui.photos

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aajogo.jogo.photosapp.databinding.FragmentPhotosBinding
import com.aajogo.jogo.photosapp.domain.models.ImageData
import com.aajogo.jogo.photosapp.domain.models.ImageModel
import com.aajogo.jogo.photosapp.ui.base.BaseFragment
import com.aajogo.jogo.photosapp.ui.MainActivity
import com.aajogo.jogo.photosapp.ui.photos.comment.CommentsViewModel
import com.aajogo.jogo.photosapp.ui.sign.SignViewModel
import com.pixelcarrot.base64image.Base64Image
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosFragment : BaseFragment<FragmentPhotosBinding>() {

    private val signViewModel by activityViewModels<SignViewModel>()
    private val commentsViewModel by activityViewModels<CommentsViewModel>()

    private val photosAdapter = PhotosAdapter()
    private lateinit var photosLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onBackPressed()
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initObservers()
        photosViewModel.getPhotos()
    }

    private fun initViews() {
        photosAdapter.itemClick = { photo ->
            navigateToComments(photo)
        }
        photosAdapter.itemDelete = { id ->
            binding.progressBar.isVisible = true
            photosViewModel.deleteId = id
            photosViewModel.deletePhoto(id)
            photosViewModel.getPhotos()
        }
        photosLayoutManager = GridLayoutManager(context, COLUMNS)
        binding.photosRecycler.apply {
            adapter = photosAdapter
            layoutManager = photosLayoutManager
        }
        addScrollListener()

        binding.addButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun addScrollListener() {
        binding.photosRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (photosLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    binding.progressBar.isVisible = true
                    photosViewModel.getPhotos()
                }
            }
        })
    }

    private fun initObservers() {
        photosViewModel.photos.observe(viewLifecycleOwner) { photos ->
            binding.progressBar.isVisible = false
            photosAdapter.submitList(photos)
        }
        signViewModel.login.observe(viewLifecycleOwner) { login ->
            setMenuHeader(login)
        }
        photosViewModel.savePhoto.observe(viewLifecycleOwner) { photo ->
            binding.progressBar.isVisible = false
            photosViewModel.savePhotoToDataBase(photo)
        }
        signViewModel.isMenuInitialized.observe(viewLifecycleOwner) { isInitialized ->
            if (isInitialized) {
                setMenu()
            }
        }
        photosViewModel.errorUpload.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                binding.progressBar.isVisible = false
                showError()
            }
        }
        photosViewModel.errorDelete.observe(viewLifecycleOwner) { isError ->
            binding.progressBar.isVisible = false
            if (isError) {
                showError()
            } else {
                photosViewModel.deletePhotoFromDataBase()
            }
        }
    }

    private fun setMenu() {
        activity?.let {
            val mainActivity = (it as MainActivity)
            mainActivity.setMenuVisibility(true)
        }
    }

    private fun setMenuHeader(header: String) {
        activity?.let {
            (it as MainActivity).setHeader(header)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get(DATA) as Bitmap

            Base64Image.encode(bitmap) { base64 ->
                base64?.let { base64Img ->
                    photosViewModel.base64Img = base64Img
                    launchLocationPermission()
                }
            }
        }
    }

    override fun uploadPhoto(location: Point) {
        val date = getDate()
        val base64Img = photosViewModel.base64Img
        if (base64Img != null) {
            binding.progressBar.isVisible = true
            photosViewModel.uploadPhoto(
                ImageData(
                    base64Img,
                    date,
                    location.latitude,
                    location.longitude
                )
            )
            photosViewModel.getPhotos()
        }
    }

    private fun navigateToComments(photo: ImageModel) {
        commentsViewModel.setPhoto(photo)
        val action = PhotosFragmentDirections.actionPhotosFragmentToCommentFragment()
        findNavController().navigate(action)
    }

    companion object {
        private const val COLUMNS = 3
        private const val REQUEST_CODE = 123
        private const val DATA = "data"
    }
}
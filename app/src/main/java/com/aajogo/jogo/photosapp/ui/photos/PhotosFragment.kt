package com.aajogo.jogo.photosapp.ui.photos

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aajogo.jogo.photosapp.R
import com.aajogo.jogo.photosapp.databinding.FragmentPhotosBinding
import com.aajogo.jogo.photosapp.domain.models.ImageData
import com.aajogo.jogo.photosapp.ui.MainActivity
import com.aajogo.jogo.photosapp.ui.sign.SignViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.pixelcarrot.base64image.Base64Image
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private val photosViewModel by activityViewModels<PhotosViewModel>()
    private val signViewModel by activityViewModels<SignViewModel>()

    private val locationRequest = createLocationRequest()

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val locationCallback: LocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    uploadPhoto(location.latitude to location.longitude)
                    stopLocationUpdates()
                }
            }
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION,
                false
            ) -> {
                startLocationUpdates()
            }
            permissions.getOrDefault(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                false
            ) -> {
                startLocationUpdates()
            }
            else -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_location_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val photosAdapter = PhotosAdapter()
    private val photosLayoutManager = GridLayoutManager(context, COLUMNS)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initObservers()
        photosViewModel.getPhotos()
    }

    private fun initViews() {
        photosAdapter.itemClick = {

        }
        photosAdapter.itemDelete = { id, position ->
            binding.progressBar.isVisible = true
            photosViewModel.deletePosition = position
            photosViewModel.deleteId = id
            photosViewModel.deletePhoto(id)
            photosViewModel.getPhotos()
        }
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
            photosAdapter.setPhotos(photos)
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
            mainActivity.setBarTitle(BAR_TITLE)
        }
    }

    private fun setMenuHeader(header: String) {
        activity?.let {
            (it as MainActivity).setHeader(header)
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap

            Base64Image.encode(bitmap) { base64 ->
                base64?.let { base64Img ->
                    photosViewModel.base64Img = base64Img
                    launchLocationPermission()
                }
            }
        }
    }

    private fun uploadPhoto(location: Pair<Double, Double>) {
        val date = getDate()
        val base64Img = photosViewModel.base64Img
        if (base64Img != null) {
            binding.progressBar.isVisible = true
            photosViewModel.uploadPhoto(
                ImageData(
                    base64Img,
                    date,
                    location.first.toInt(),
                    location.second.toInt()
                )
            )
            photosViewModel.getPhotos()
        }
    }

    private fun getDate(): Int {
        val currentTimeMillis = System.currentTimeMillis()
        val date = Date(currentTimeMillis)
        val unixTimestamp = date.time / 1000
        return unixTimestamp.toInt()
    }

    private fun launchLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun createLocationRequest(): LocationRequest {
        val timeInterval = 1000L
        return LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, timeInterval
        ).build()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun showError() {
        Toast.makeText(
            requireContext(),
            getString(R.string.network_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val COLUMNS = 3
        private const val BAR_TITLE = ""
        private const val REQUEST_CODE = 123
    }
}
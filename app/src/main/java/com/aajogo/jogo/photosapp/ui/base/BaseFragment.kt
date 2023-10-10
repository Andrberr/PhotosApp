package com.aajogo.jogo.photosapp.ui.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.aajogo.jogo.photosapp.R
import com.aajogo.jogo.photosapp.ui.photos.PhotosViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.yandex.mapkit.geometry.Point
import java.util.*

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    var _binding: T? = null
    protected open val binding get() = _binding!!

    val photosViewModel by viewModels<PhotosViewModel>()

    private val locationRequest = createLocationRequest()

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val locationCallback: LocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    uploadPhoto(Point(location.latitude, location.longitude))
                    stopLocationUpdates()
                    break
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

    abstract fun uploadPhoto(location: Point)

    fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    fun getDate(): Int {
        val currentTimeMillis = System.currentTimeMillis()
        val date = Date(currentTimeMillis)
        val unixTimestamp = date.time / MILLISECONDS
        return unixTimestamp.toInt()
    }

    fun showError() {
        Toast.makeText(
            requireContext(),
            getString(R.string.network_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun launchLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, TIME_INTERVAL
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TIME_INTERVAL = 20000L
        private const val REQUEST_CODE = 123
        private const val MILLISECONDS = 1000
    }
}
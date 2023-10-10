package com.aajogo.jogo.photosapp.ui.map

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aajogo.jogo.photosapp.BuildConfig
import com.aajogo.jogo.photosapp.R
import com.aajogo.jogo.photosapp.databinding.FragmentMapBinding
import com.aajogo.jogo.photosapp.domain.models.ImageData
import com.aajogo.jogo.photosapp.ui.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.pixelcarrot.base64image.Base64Image
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>() {

    private val mapView by lazy {
        binding.mapView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitInitializer.initialize(BuildConfig.YANDEX_API_KEY, requireContext())
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
        binding.addButton.setOnClickListener {
            takePhoto()
        }
        photosViewModel.getMarkers()
    }

    private fun initObservers() {
        photosViewModel.markers.observe(viewLifecycleOwner) { markers ->
            markers.forEach { point ->
                putMarker(point)
            }
            moveCamera(markers[0])
        }
        photosViewModel.errorUpload.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                showError()
            }
        }
        photosViewModel.savePhoto.observe(viewLifecycleOwner) { photo ->
            photosViewModel.savePhotoAndMarkerToDataBase(photo)
        }
        photosViewModel.isSaved.observe(viewLifecycleOwner) { isSaved ->
            if (isSaved) {
                photosViewModel.getMarkers()
            }
        }
    }

    private fun putMarker(point: Point) {
        val markerSize =
            requireContext().resources.getDimensionPixelSize(R.dimen.map_marker_icon_size)
        Glide.with(requireContext()).asBitmap()
            .load(R.drawable.marker)
            .into(object : CustomTarget<Bitmap>(markerSize, markerSize) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {

                    mapView.map.mapObjects.addPlacemark(
                        point,
                        ImageProvider.fromBitmap(resource),
                        IconStyle()
                    )
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun moveCamera(point: Point) {
        mapView.map.move(
            CameraPosition(point, 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
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
            photosViewModel.uploadPhoto(
                ImageData(
                    base64Img,
                    date,
                    location.latitude,
                    location.longitude
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    companion object {
        private const val REQUEST_CODE = 123
        private const val DATA = "data"
    }
}
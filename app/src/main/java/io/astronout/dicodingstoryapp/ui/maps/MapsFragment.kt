package io.astronout.dicodingstoryapp.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.viewbinding.library.fragment.viewBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.databinding.FragmentMapsBinding
import io.astronout.dicodingstoryapp.domain.model.Story
import io.astronout.dicodingstoryapp.ui.base.BaseFragment
import io.astronout.dicodingstoryapp.utils.collectLifecycleFlow
import io.astronout.dicodingstoryapp.utils.showToast
import io.astronout.dicodingstoryapp.vo.Resource

class MapsFragment : BaseFragment(R.layout.fragment_maps) {

    private val binding: FragmentMapsBinding by viewBinding()
    private val viewModel: MapsViewModel by viewModels()

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }
        getDeviceLocation()
        setupMapStyle()
        fetchStories()
    }

    override fun initUI() {
        super.initUI()
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val mapFragment = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun setupMapStyle() {
        runCatching {
            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )
        }
    }

    private fun fetchStories() {
        collectLifecycleFlow(viewModel.allStories) {
            when (it) {
                is Resource.Error -> onFetchStoriesFailed(it.message)
                is Resource.Loading -> {}
                is Resource.Success -> {
                    onFetchStoriesSuccess(it.data)
                }
            }
        }
    }

    private fun onFetchStoriesSuccess(data: List<Story>) {
        data.filter { it.lat != 0.0 && it.lon != 0.0 }.forEach {
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(it.lat, it.lon))
                    .title(it.name)
                    .snippet("Lat: ${it.lat}, Lon: ${it.lon}")
            )
        }
    }

    private fun onFetchStoriesFailed(message: String) {
        showToast(message)
    }

    private fun getDeviceLocation() {
        if (isPermissionGranted()) {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f))
                } else {
                    showToast(getString(R.string.label_activate_location_message))
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            getDeviceLocation()
        }
    }

}
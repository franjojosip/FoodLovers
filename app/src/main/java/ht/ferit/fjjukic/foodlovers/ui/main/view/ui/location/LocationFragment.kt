package ht.ferit.fjjukic.foodlovers.ui.main.view.ui.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.data.SoundManager
import ht.ferit.fjjukic.foodlovers.databinding.FragmentLocationBinding
import ht.ferit.fjjukic.foodlovers.ui.base.LocationViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.LocationViewModel
import ht.ferit.fjjukic.foodlovers.utils.checkNetworkState
import ht.ferit.fjjukic.foodlovers.utils.showLocationIsDisabledAlert
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class LocationFragment : Fragment(), KodeinAware, OnMapReadyCallback {

    override val kodein by kodein()
    private val factory by instance<LocationViewModelFactory>()
    private lateinit var binding: FragmentLocationBinding
    private lateinit var viewModel: LocationViewModel
    private lateinit var map: GoogleMap
    private lateinit var client: FusedLocationProviderClient
    private lateinit var soundManager: SoundManager
    private var permissions: Array<String> = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(LocationViewModel::class.java)

        binding = FragmentLocationBinding.inflate(
            inflater,
            container,
            false
        )
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        viewModel.getUser(view)
        loadMap()
        loadSound()
    }

    private fun loadSound() {
        soundManager = SoundManager()
        soundManager.load(requireContext(), R.raw.pin_drop, 1)
        soundManager.load(requireContext(), R.raw.pin_pull, 1)
    }

    private fun loadMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.isZoomControlsEnabled = true
        loadLastLocation()
        if (!checkPermissions()) {
            requestPermissions(permissions, 10)
            return
        }
        map.isMyLocationEnabled = true
        setMarkerLocation()
        map.setOnMyLocationButtonClickListener {
            if (!isLocationEnabled()) {
                requireContext().showLocationIsDisabledAlert()
            } else if (!requireContext().checkNetworkState()) {
                Toast.makeText(
                    context,
                    "Turn on internet before using this service",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                setMarkerLocation()
            }
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (checkPermissions()) {
            map.isMyLocationEnabled = true
            setMarkerLocation()
            map.setOnMyLocationButtonClickListener {
                if (!isLocationEnabled()) {
                    requireContext().showLocationIsDisabledAlert()
                } else {
                    setMarkerLocation()
                }
                true
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Grant all permissions to use this feature!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadLastLocation() {
        if (viewModel.currentUser.value != null) {
            placeMarker(
                LatLng(
                    viewModel.currentUser.value!!.latitude.toDouble(),
                    viewModel.currentUser.value!!.longitude.toDouble()
                ), "Last known location"
            )
        }
    }

    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            requireContext(),
            permissions[0]
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permissions[1]
                ) == PackageManager.PERMISSION_GRANTED
                )
    }

    private fun isLocationEnabled(): Boolean {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun setMarkerLocation() {
        client.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                if (viewModel.currentUser.value != null && location.latitude.toString() == viewModel.currentUser.value!!.latitude && location.longitude.toString() == viewModel.currentUser.value!!.longitude) {
                    Toast.makeText(
                        requireContext(),
                        "New location is same as last known location!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    map.clear()
                    val latLng = LatLng(location.latitude, location.longitude)
                    placeMarker(latLng, "New location")
                    viewModel.updateUserLocation(latLng)
                }
            }
        }
    }

    private fun placeMarker(latLng: LatLng, title: String) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
        val marker = map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
        )
        marker.showInfoWindow()
        soundManager.play(R.raw.pin_drop)

    }
}

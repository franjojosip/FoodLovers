package ht.ferit.fjjukic.foodlovers.app_account.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_account.viewmodel.ChangeLocationViewModel
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.listener.LocationHandler
import ht.ferit.fjjukic.foodlovers.app_common.listener.PermissionHandler
import ht.ferit.fjjukic.foodlovers.app_common.model.DialogModel
import ht.ferit.fjjukic.foodlovers.app_common.sound.SoundManager
import ht.ferit.fjjukic.foodlovers.app_common.utils.checkNetworkState
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.utils.showAlertDialog
import ht.ferit.fjjukic.foodlovers.databinding.FragmentLocationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChangeLocationFragment : BaseFragment<ChangeLocationViewModel, FragmentLocationBinding>(),
    OnMapReadyCallback, PermissionHandler, LocationHandler {

    override val viewModel: ChangeLocationViewModel by viewModel()
    override val layoutId = R.layout.fragment_location

    private val soundManager: SoundManager by lazy { SoundManager() }

    private lateinit var map: GoogleMap
    private val client: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            requireActivity()
        )
    }

    private var permissions: Array<String> = arrayOf(ACCESS_COARSE_LOCATION)

    private var permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isGranted = permissions.all { it.value }
            if (isGranted) {
                handleNewLocation()
            } else {
                showToast(messageId = R.string.general_error_permissions)
                openSettings(requireContext())
            }
        }

    private var locationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { checkSettings() }

    override fun init() {
        setUpObservers()
        setUpSound()
        setUpMapFragment()
    }

    private fun setUpObservers() {
        viewModel.user.observeNotNull(viewLifecycleOwner) {
            binding.tvLocation.text = getLocation(requireContext(), it.latitude, it.longitude)
        }
    }

    private fun setUpSound() {
        soundManager.load(requireContext(), R.raw.pin_drop, 1)
        soundManager.load(requireContext(), R.raw.pin_pull, 1)
    }

    private fun setUpMapFragment() {
        (childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment).getMapAsync(
            this
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.isZoomControlsEnabled = true

        setUpLastLocationMarker()
        requestPermissions()
    }

    @SuppressLint("MissingPermission")
    private fun requestPermissions() {
        when {
            checkPermissions(requireContext(), permissions) -> {
                handleNewLocation()
            }
            checkShouldShowPermissionRationale(requireActivity(), permissions) -> {
                showToast(messageId = R.string.location_permission_error)
                permissionLauncher.launch(permissions)
            }
            else -> {
                showToast(messageId = R.string.general_error_permissions)
                openSettings(requireContext())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleNewLocation() {
        map.isMyLocationEnabled = true
        checkSettings()

        map.setOnMyLocationButtonClickListener {
            checkSettings()
            true
        }
    }

    private fun checkSettings() {
        when {
            !checkLocationEnabled() -> {
                context?.showAlertDialog(
                    DialogModel(title = R.string.location,
                        message = R.string.location_permission_question,
                        positiveTitleId = R.string.screen_settings,
                        positiveAction = {
                            showToast(messageId = R.string.location_settings_info)
                            locationLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        },
                        neutralAction = {
                            showToast(messageId = R.string.general_error_permissions)
                            locationLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        }
                    ))
            }
            !requireContext().checkNetworkState() -> {
                showToast(messageId = R.string.general_error_internet)
            }
            else -> {
                setMarkerLocation()
            }
        }
    }

    private fun setUpLastLocationMarker() {
        viewModel.user.value?.let {
            placeMarker(
                LatLng(
                    it.latitude.toDouble(),
                    it.longitude.toDouble()
                ), getString(R.string.location_last_known)
            )
        }
    }

    private fun checkLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun setMarkerLocation() {
        client.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            location?.let {
                val currentUser = viewModel.user.value
                when {
                    currentUser != null && location.latitude.toString() == currentUser.latitude && location.longitude.toString() == currentUser.longitude -> {
                        showToast(messageId = R.string.location_already_exist)
                    }
                    else -> {
                        map.clear()
                        val latLng = LatLng(location.latitude, location.longitude)
                        placeMarker(latLng, getString(R.string.location_new_location))
                        viewModel.updateUserLocation(latLng)
                    }
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
        marker?.showInfoWindow()
        soundManager.play(R.raw.pin_drop)
    }
}

package ht.ferit.fjjukic.foodlovers.app_account.view

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_account.viewmodel.AccountViewModel
import ht.ferit.fjjukic.foodlovers.app_common.listener.LocationHandler
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.DialogModel
import ht.ferit.fjjukic.foodlovers.app_common.notification.NotificationsManager
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.utils.showAlertDialog
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentAccountBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : BaseFragment<AccountViewModel, FragmentAccountBinding>(),
    ActivityCompat.OnRequestPermissionsResultCallback, LocationHandler {

    override val layoutId: Int = R.layout.fragment_account
    override val viewModel: AccountViewModel by viewModel()

    private var imgUri: Uri? = null

    private val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private var permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isGranted = permissions.all { it.value }
            if (isGranted) {
                chooseImageFromGallery()
            } else {
                openSettings(requireContext())
            }
        }


    private val imagePickerResultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { viewModel.handleImagePathChange(it) }
        }


    private var imageCaptureResultLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result) {
                imgUri?.let {
                    viewModel.handleImagePathChange(it)
                    context?.loadImage(it.toString(), binding.ivProfileImage)
                    sendImageCapturedNotification(it)
                }
            }
        }

    override fun init() {
        viewModel.init()

        setListeners()
    }

    private fun setListeners() {
        binding.ivProfileImage.setOnClickListener {
            requestRequiredPermissions(
                action = ::chooseImageFromGallery,
                permissions = arrayOf(storagePermission),
                messageId = R.string.image_storage_permission_error,
                permissionLauncher = permissionLauncher
            )
        }
        binding.clTakeImage.setOnClickListener {
            requestRequiredPermissions(
                action = ::captureImage,
                permissions = arrayOf(storagePermission, Manifest.permission.CAMERA),
                messageId = R.string.image_capture_permission_error,
                permissionLauncher = permissionLauncher

            )
        }
        binding.tvChangeUsername.setOnClickListener {
            viewModel.handleNavigation(ActionNavigate.ChangeUsername)
        }
        binding.tvChangeEmail.setOnClickListener {
            viewModel.handleNavigation(ActionNavigate.ChangeEmail)
        }
        binding.tvChangeLocation.setOnClickListener {
            viewModel.handleNavigation(ActionNavigate.ChangeLocation)
        }
        binding.clLogout.setOnClickListener {
            context?.showAlertDialog(
                DialogModel(
                    title = R.string.logout_question,
                    message = R.string.logout_message,
                    positiveTitleId = R.string.action_logout,
                    positiveAction = {
                        viewModel.onLogoutClick()
                    }
                )
            )
        }
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.user.observeNotNull(viewLifecycleOwner) {
            binding.tvProfileUsername.text = it.name
            binding.tvProfileEmail.text = it.email
            binding.tvProfileLocation.text =
                getLocation(requireContext(), it.latitude, it.longitude)

            if (it.imageUrl.isNotEmpty()) {
                context?.loadImage(it.imageUrl, binding.ivProfileImage)
            }
        }
    }

    override fun handleActionNavigate(actionNavigate: ActionNavigate) {
        when (actionNavigate) {

            is ActionNavigate.ChangeEmail -> {
                findNavController().navigate(
                    AccountFragmentDirections.actionNavProfileToNavChangeEmail()
                )
            }

            is ActionNavigate.ChangeUsername -> {
                findNavController().navigate(
                    AccountFragmentDirections.actionNavProfileToNavChangeUsername()
                )
            }

            is ActionNavigate.ChangeLocation -> {
                findNavController().navigate(
                    AccountFragmentDirections.actionNavProfileToNavChangeLocation()
                )
            }
            else -> {}
        }
    }

    private fun captureImage() {
        val values = ContentValues()
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "")
        imgUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
        imageCaptureResultLauncher.launch(imgUri)
    }

    private fun chooseImageFromGallery() {
        imagePickerResultLauncher.launch("image/*")
    }

    private fun sendImageCapturedNotification(uri: Uri) {
        NotificationsManager(requireContext()).sendNotification(
            uri,
            title = getString(R.string.image_new_saved),
            message = getString(R.string.image_notification)
        )
    }

    private fun Context.loadImage(url: String, view: ImageView) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(view)
    }
}

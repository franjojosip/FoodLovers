package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.yalantis.ucrop.UCrop
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.listener.PermissionHandler
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CreateRecipeViewModel
import ht.ferit.fjjukic.foodlovers.databinding.FragmentMainStepBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File


class MainStepFragment : BaseFragment<CreateRecipeViewModel, FragmentMainStepBinding>(),
    PermissionHandler {
    override val layoutId: Int = R.layout.fragment_main_step
    override val viewModel: CreateRecipeViewModel by sharedViewModel()

    private val storagePermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
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
            uri?.let {
                val destinationUri =
                    Uri.fromFile(File(context?.cacheDir, "IMG_" + System.currentTimeMillis()))

                UCrop.of(uri, destinationUri)
                    .withAspectRatio(16f, 9f)
                    .start(requireContext(), this)

            }
        }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && (requestCode == UCrop.REQUEST_CROP)) {
            data?.let {
                UCrop.getOutput(data)?.let { viewModel.onImageSelected(it) }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(context, "Error while cropping an image!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun init() {
        setListeners()
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.numOfServings.observeNotNull(viewLifecycleOwner) {
            binding.tvCounter.text = it.toString()
            binding.btnDecrement.visibility =
                if (it != viewModel.minServings) View.VISIBLE else View.INVISIBLE
            binding.btnIncrement.visibility =
                if (it != viewModel.maxServings) View.VISIBLE else View.INVISIBLE
        }
        viewModel.cookingTime.observeNotNull(viewLifecycleOwner) {
            binding.tvCookingTime.text = it.convertToTime()
            binding.seekbar.value = viewModel.getCookingTime()
        }
        viewModel.imagePath.observeNotNull(viewLifecycleOwner) {
            binding.ivAction.isVisible = false
            binding.ivRecipe.setImageURI(it)
        }
    }

    private fun setListeners() {
        binding.tvName.doOnTextChanged { text, _, _, _ ->
            viewModel.onNameChanged(text)
        }

        binding.btnDecrement.setOnClickListener {
            viewModel.onChangeCounter(false)
        }
        binding.btnIncrement.setOnClickListener {
            viewModel.onChangeCounter(true)
        }
        binding.seekbar.addOnChangeListener { _, value, _ ->
            viewModel.onChangeTime(value.toInt())
        }
        binding.clImage.setOnClickListener {
            requestRequiredPermissions(
                ::chooseImageFromGallery,
                storagePermissions,
                R.string.image_storage_permission_error,
                permissionLauncher
            )
        }
    }

    private fun chooseImageFromGallery() {
        imagePickerResultLauncher.launch("image/*")
    }

}
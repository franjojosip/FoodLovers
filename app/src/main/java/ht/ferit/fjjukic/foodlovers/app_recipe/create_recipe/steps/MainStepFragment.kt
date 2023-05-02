package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.yalantis.ucrop.UCrop
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToServings
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CreateRecipeViewModel
import ht.ferit.fjjukic.foodlovers.databinding.FragmentMainStepBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File


class MainStepFragment : BaseFragment<CreateRecipeViewModel, FragmentMainStepBinding>() {
    override val layoutId: Int = R.layout.fragment_main_step
    override val viewModel: CreateRecipeViewModel by sharedViewModel()

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val outputFile = File.createTempFile("temp", ".jpg", requireContext().cacheDir)
                UCrop.of(uri, outputFile.toUri())
                    .withAspectRatio(16f, 9f)
                    .withMaxResultSize(400, 240)
                    .start(requireContext(), this)
            }
        }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            data?.let {
                UCrop.getOutput(data)?.let { viewModel.onImageSelected(it) }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError =
                data?.let { UCrop.getError(data) }?.message ?: "Error while cropping an image!"
            Toast.makeText(context, cropError, Toast.LENGTH_SHORT).show()
        }
    }

    override fun init() {
        setListeners()
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.numOfServings.observeNotNull(viewLifecycleOwner) {
            binding.tvCounter.text = it.toString()
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
            selectImageFromGalleryResult.launch("image/*")
        }
    }

}
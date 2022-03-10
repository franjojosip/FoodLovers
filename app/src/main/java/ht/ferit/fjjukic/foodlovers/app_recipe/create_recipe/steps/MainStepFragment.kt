package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CreateRecipeViewModel
import ht.ferit.fjjukic.foodlovers.databinding.FragmentMainStepBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainStepFragment : BaseFragment<CreateRecipeViewModel, FragmentMainStepBinding>() {
    override val layoutId: Int = R.layout.fragment_main_step
    override val viewModel: CreateRecipeViewModel by sharedViewModel()

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.onImageSelected(uri)
        }
    }

    override fun init() {
        setListeners()
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.numOfServings.observeNotNull(viewLifecycleOwner) {
            binding.tvCounter.text = it
        }
        viewModel.cookingTime.observeNotNull(viewLifecycleOwner) {
            binding.tvCookingTime.text = it
            binding.seekbar.value = viewModel.getCookingTime()
        }
        viewModel.imagePath.observeNotNull(viewLifecycleOwner) {
            binding.ivAction.isVisible = false
            binding.ivRecipe.setImageURI(it)
        }
    }

    private fun setListeners() {
        binding.btnDecrement.setOnClickListener {
            viewModel.onChangeCounter(false)
        }
        binding.btnIncrement.setOnClickListener {
            viewModel.onChangeCounter(true)
        }
        binding.seekbar.addOnChangeListener { _, value, _ ->
            viewModel.onChangeTime(value.toInt())
        }
        binding.ivRecipe.setOnClickListener {
            selectImageFromGalleryResult.launch("image/*")
        }
    }

}
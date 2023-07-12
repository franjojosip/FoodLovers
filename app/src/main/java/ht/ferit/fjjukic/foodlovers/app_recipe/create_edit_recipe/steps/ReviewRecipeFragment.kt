package ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.steps

import android.view.LayoutInflater
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.model.IngredientModel
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.StepModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToServings
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_common.utils.getColorStateList
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.RecipeViewModel
import ht.ferit.fjjukic.foodlovers.databinding.FragmentReviewBinding
import ht.ferit.fjjukic.foodlovers.databinding.ItemIngredientListItemBinding
import ht.ferit.fjjukic.foodlovers.databinding.ItemNoListItemsBinding
import ht.ferit.fjjukic.foodlovers.databinding.ItemStepBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ReviewRecipeFragment : BaseFragment<RecipeViewModel, FragmentReviewBinding>() {
    override val layoutId: Int = R.layout.fragment_review
    override val viewModel: RecipeViewModel by sharedViewModel()

    override fun init() {
        if (viewModel.isEditMode) {
            binding.btnRecipeAction.setText(R.string.btn_edit_recipe)
        }

        setListeners()
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.oldRecipe.observeNotNull(viewLifecycleOwner) {
            setScreen(it)
        }

        viewModel.dataChanged.observeNotNull(viewLifecycleOwner) {
            if (it) {
                setScreen(viewModel.recipe)
            }
        }

    }

    private fun setScreen(data: RecipeModel) {
        binding.tvRecipeTitle.text = data.name.ifBlank { "Here goes your title" }
        binding.tvNumberOfServings.text = data.servings.convertToServings()
        binding.tvTime.text = data.time.convertToTime()

        binding.tvRecipeAuthor.isVisible = data.user != null

        if (data.imagePath.contains("https")) {
            Glide.with(binding.root)
                .load(data.imagePath)
                .placeholder(R.drawable.background_placeholder)
                .into(binding.ivRecipe)

            binding.ivRecipe.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.tvPlaceholder.isVisible = false
        } else if (data.imagePath.isNotBlank()) {
            binding.ivRecipe.scaleType = ImageView.ScaleType.FIT_XY
            binding.tvPlaceholder.isVisible = false
            binding.ivRecipe.setImageURI(data.imagePath.toUri())
        }

        binding.ivDifficulty.backgroundTintList =
            getColorStateList(data.difficulty?.name ?: "easy", resources)

        binding.tvSelectedCategory.text = data.category?.name
        binding.tvSelectedDifficulty.text = data.difficulty?.name

        binding.llIngredients.removeAllViews()
        binding.llSteps.removeAllViews()
        data.ingredients.forEach {
            addIngredientField(it)
        }
        setIngredients(data.ingredients)
        setSteps(data.steps)
    }

    private fun setListeners() {
        binding.btnRecipeAction.setOnClickListener {
            viewModel.onRecipeAction()
        }
    }

    private fun setIngredients(data: List<IngredientModel>) {
        binding.llIngredients.removeAllViews()
        if (data.isEmpty()) {
            binding.llIngredients.addView(
                ItemNoListItemsBinding.inflate(LayoutInflater.from(context), null, false).root
            )
        } else {
            data.forEach {
                addIngredientField(it)
            }
        }
    }

    private fun setSteps(data: List<StepModel>) {
        binding.llSteps.removeAllViews()

        if (data.isEmpty()) {
            binding.llSteps.addView(
                ItemNoListItemsBinding.inflate(LayoutInflater.from(context), null, false).root
            )
        } else {
            data.forEach {
                addStepField(it)
            }
        }
    }

    private fun addIngredientField(data: IngredientModel) {
        val view = ItemIngredientListItemBinding.inflate(LayoutInflater.from(context), null, false)
        view.tvIngredientAmount.text = data.amount.ifBlank { "XX" }
        view.tvIngredientName.text = data.name.ifBlank { "Ingredient name placeholder" }
        binding.llIngredients.addView(view.root)
    }

    private fun addStepField(data: StepModel) {
        val view = ItemStepBinding.inflate(LayoutInflater.from(context), null, false)
        view.tvStep.text = getString(R.string.step, data.position)
        view.tvDescription.text = data.description.ifBlank { "Step description placeholder" }
        binding.llSteps.addView(view.root)
    }
}
package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps

import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.DialogModel
import ht.ferit.fjjukic.foodlovers.app_common.model.MessageModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToServings
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.utils.showAlertDialog
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CreateRecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.IngredientUI
import ht.ferit.fjjukic.foodlovers.app_recipe.model.StepUI
import ht.ferit.fjjukic.foodlovers.databinding.FragmentReviewBinding
import ht.ferit.fjjukic.foodlovers.databinding.IngredientListItemBinding
import ht.ferit.fjjukic.foodlovers.databinding.NoItemsListItemBinding
import ht.ferit.fjjukic.foodlovers.databinding.StepListItemBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ReviewRecipeFragment : BaseFragment<CreateRecipeViewModel, FragmentReviewBinding>() {
    override val layoutId: Int = R.layout.fragment_review
    override val viewModel: CreateRecipeViewModel by sharedViewModel()

    override fun init() {
        setListeners()
        refreshScreen()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.title.observe(viewLifecycleOwner) {
            binding.tvRecipeTitle.text = it.ifBlank { "Here goes your title" }
        }
        viewModel.cookingTime.observeNotNull(viewLifecycleOwner) {
            binding.tvTime.text = it.convertToTime()
        }
        viewModel.numOfServings.observeNotNull(viewLifecycleOwner) {
            binding.tvNumberOfServings.text = it.convertToServings()
        }
        viewModel.imagePath.observeNotNull(viewLifecycleOwner) {
            binding.ivRecipe.setImageURI(viewModel.recipe.imageURI.toUri())
            binding.tvPlaceholder.isVisible = false
            binding.ivRecipe.alpha = 1f
        }
    }

    override fun onResume() {
        super.onResume()
        refreshScreen()
    }

    private fun refreshScreen() {
        if (!viewModel.isDataChanged) return

        binding.tvRecipeTitle.text = viewModel.recipe.name.ifBlank { "Here goes your title" }
        binding.tvRecipeAuthor.text = viewModel.author
        binding.tvNumberOfServings.text = viewModel.recipe.servings.convertToServings()
        binding.tvTime.text = viewModel.recipe.time.convertToTime()

        if (viewModel.recipe.imageURI.isNotBlank()) {
            binding.ivRecipe.setImageURI(viewModel.recipe.imageURI.toUri())
            binding.tvPlaceholder.isVisible = false
            binding.ivRecipe.alpha = 1f
        } else {
            binding.ivRecipe.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.image_sweet_food
                )
            )
            binding.ivRecipe.alpha = 0.9f
            binding.tvPlaceholder.isVisible = true
        }

        setIngredients()
        setSteps()

        viewModel.setDataChanged(false)
    }

    private fun setListeners() {
        binding.btnCreateRecipe.setOnClickListener {
            if (viewModel.isRecipeFilled()) {
                context?.showAlertDialog(
                    DialogModel(
                        title = R.string.dialog_create_recipe_title,
                        message = R.string.dialog_create_recipe_message,
                        positiveAction = {
                            viewModel.confirmCreateRecipe()
                        },
                        positiveTitleId = R.string.dialog_create_recipe_confirm
                    )
                )
            } else {
                binding.ivRecipe
                showToast(
                    MessageModel(
                        message = "Please check all the data before creating your recipe!"
                    )
                )
            }
        }
    }

    private fun setIngredients() {
        binding.llIngredients.removeAllViews()
        if (viewModel.recipe.ingredients.isEmpty()) {
            binding.llIngredients.addView(
                NoItemsListItemBinding.inflate(LayoutInflater.from(context), null, false).root
            )
        } else {
            viewModel.recipe.ingredients.forEach {
                addIngredientField(it)
            }
        }
    }

    private fun setSteps() {
        val steps = viewModel.recipe.steps
        binding.llSteps.removeAllViews()

        if (steps.isEmpty()) {
            binding.llSteps.addView(
                NoItemsListItemBinding.inflate(LayoutInflater.from(context), null, false).root
            )
        } else {
            steps.sortBy { it.position }
            steps.forEach {
                addStepField(it)
            }
        }
    }

    private fun addIngredientField(data: IngredientUI) {
        val view = IngredientListItemBinding.inflate(LayoutInflater.from(context), null, false)
        view.tvIngredientAmount.text = data.amount
        view.tvIngredientName.text = data.name
        binding.llIngredients.addView(view.root)
    }

    private fun addStepField(data: StepUI) {
        val view = StepListItemBinding.inflate(LayoutInflater.from(context), null, false)
        view.tvStep.text = "Step ${data.position}."
        view.tvDescription.text = data.description
        binding.llSteps.addView(view.root)
    }
}
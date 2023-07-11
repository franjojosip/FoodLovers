package ht.ferit.fjjukic.foodlovers.app_recipe.edit_recipe.steps

import android.view.LayoutInflater
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.model.IngredientModel
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.StepModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToServings
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_recipe.edit_recipe.EditRecipeViewModel
import ht.ferit.fjjukic.foodlovers.databinding.FragmentReviewEditBinding
import ht.ferit.fjjukic.foodlovers.databinding.IngredientListItemBinding
import ht.ferit.fjjukic.foodlovers.databinding.NoItemsListItemBinding
import ht.ferit.fjjukic.foodlovers.databinding.StepListItemBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ReviewRecipeEditFragment : BaseFragment<EditRecipeViewModel, FragmentReviewEditBinding>() {
    override val layoutId: Int = R.layout.fragment_review_edit
    override val viewModel: EditRecipeViewModel by sharedViewModel()

    override fun init() {
        setListeners()
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.oldRecipe.observeNotNull(viewLifecycleOwner) {
            setScreen(it)
        }

        viewModel.dataChanged.observeNotNull(viewLifecycleOwner) {
            if (it) {
                setScreen(viewModel.newRecipe)
            }
        }

    }

    private fun setScreen(data: RecipeModel) {
        binding.tvRecipeTitle.text = data.name
        binding.tvNumberOfServings.text = data.servings.convertToServings()
        binding.tvTime.text = data.time.convertToTime()

        if (data.imagePath.contains("https")) {
            Glide.with(binding.root)
                .load(data.imagePath)
                .placeholder(R.drawable.image_placeholder)
                .into(binding.ivRecipe)
        } else {
            binding.ivRecipe.setImageURI(data.imagePath.toUri())
        }

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
        binding.btnEditRecipe.setOnClickListener {
            viewModel.editRecipe()
        }
    }

    private fun setIngredients(data: List<IngredientModel>) {
        binding.llIngredients.removeAllViews()
        if (data.isEmpty()) {
            binding.llIngredients.addView(
                NoItemsListItemBinding.inflate(LayoutInflater.from(context), null, false).root
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
                NoItemsListItemBinding.inflate(LayoutInflater.from(context), null, false).root
            )
        } else {
            data.forEach {
                addStepField(it)
            }
        }
    }

    private fun addIngredientField(data: IngredientModel) {
        val view = IngredientListItemBinding.inflate(LayoutInflater.from(context), null, false)
        view.tvIngredientAmount.text = data.amount
        view.tvIngredientName.text = data.name
        binding.llIngredients.addView(view.root)
    }

    private fun addStepField(data: StepModel) {
        val view = StepListItemBinding.inflate(LayoutInflater.from(context), null, false)
        view.tvStep.text = "Step ${data.position}."
        view.tvDescription.text = data.description
        binding.llSteps.addView(view.root)
    }
}
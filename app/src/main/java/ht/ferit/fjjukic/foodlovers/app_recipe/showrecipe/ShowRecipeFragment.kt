package ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe

import android.view.LayoutInflater
import androidx.core.net.toUri
import androidx.navigation.fragment.navArgs
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToServings
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.model.IngredientUI
import ht.ferit.fjjukic.foodlovers.app_recipe.model.StepUI
import ht.ferit.fjjukic.foodlovers.databinding.FragmentShowRecipeBinding
import ht.ferit.fjjukic.foodlovers.databinding.IngredientListItemBinding
import ht.ferit.fjjukic.foodlovers.databinding.StepListItemBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowRecipeFragment: BaseFragment<ShowRecipeViewModel, FragmentShowRecipeBinding>() {
    private val args: ShowRecipeFragmentArgs by navArgs()

    override val layoutId: Int = R.layout.fragment_show_recipe
    override val viewModel: ShowRecipeViewModel by viewModel()

    override fun init() {
        toolbar = binding.toolbarLayout
        viewModel.loadRecipe(args.id)
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.recipe.observeNotNull(viewLifecycleOwner) { recipe ->
            binding.tvRecipeTitle.text = recipe.name
            binding.tvRecipeAuthor.text = recipe.author

            binding.tvTime.text = recipe.time.convertToTime()
            binding.tvNumberOfServings.text = recipe.servings.convertToServings()
            binding.ivRecipe.setImageURI(recipe.imageURI.toUri())

            recipe.ingredients.forEach { addIngredientField(it) }
            recipe.steps.forEach { addStepField(it) }
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
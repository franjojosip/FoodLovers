package ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe

import android.view.LayoutInflater
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToServings
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Ingredient
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Step
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

        binding.toolbarLayout.enableEndAction()
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.recipe.observeNotNull(viewLifecycleOwner) { recipe ->
            binding.toolbarLayout.setTitle(recipe.title)

            binding.tvRecipeTitle.text = recipe.title
            binding.tvRecipeAuthor.text = recipe.user

            binding.tvTime.text = recipe.time.toInt().convertToTime()
            binding.tvNumberOfServings.text = recipe.servings.convertToServings()

            Glide.with(binding.root)
                .load(recipe.imagePath)
                .into(binding.ivRecipe)

            recipe.ingredients.forEach { addIngredientField(it) }
            recipe.steps.forEach { addStepField(it) }
        }
    }

    private fun addIngredientField(data: Ingredient) {
        val view = IngredientListItemBinding.inflate(LayoutInflater.from(context), null, false)
        view.tvIngredientAmount.text = data.amount
        view.tvIngredientName.text = data.name
        binding.llIngredients.addView(view.root)
    }

    private fun addStepField(data: Step) {
        val view = StepListItemBinding.inflate(LayoutInflater.from(context), null, false)
        view.tvStep.text = "Step ${data.position}."
        view.tvDescription.text = data.description
        binding.llSteps.addView(view.root)
    }

}
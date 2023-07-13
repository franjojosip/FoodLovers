package ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe

import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.model.IngredientModel
import ht.ferit.fjjukic.foodlovers.app_common.model.StepModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToServings
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_common.utils.getColorStateList
import ht.ferit.fjjukic.foodlovers.databinding.FragmentShowRecipeBinding
import ht.ferit.fjjukic.foodlovers.databinding.ItemIngredientListItemBinding
import ht.ferit.fjjukic.foodlovers.databinding.ItemStepBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowRecipeFragment : BaseFragment<ShowRecipeViewModel, FragmentShowRecipeBinding>() {
    private val args: ShowRecipeFragmentArgs by navArgs()

    override val layoutId: Int = R.layout.fragment_show_recipe
    override val viewModel: ShowRecipeViewModel by viewModel()

    override fun init() {
        toolbar = binding.toolbarLayout
        loader = binding.loaderLayout
        viewModel.loadRecipe(args.id)

        binding.toolbarLayout.enableEndAction()
        binding.ivFavorite.setOnClickListener {
            viewModel.onFavoriteClick()
        }
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            binding.toolbarLayout.setTitle(recipe.title)

            binding.tvRecipeTitle.text = recipe.title
            binding.tvRecipeAuthor.text = recipe.user

            binding.tvTime.text = recipe.time.toInt().convertToTime()
            binding.tvNumberOfServings.text = recipe.servings.convertToServings(resources)

            binding.tvSelectedCategory.text = recipe.category
            binding.tvSelectedDifficulty.text = recipe.difficulty
            binding.ivDifficulty.backgroundTintList =
                getColorStateList(recipe.difficulty, binding.root.resources)

            if (recipe.userId == viewModel.getUserId() || viewModel.isAdmin()) {
                binding.ivEditRecipe.isVisible = true
                binding.ivEditRecipe.setOnClickListener {
                    viewModel.onEditClick()
                }
            }

            setFavoriteIconColor(recipe.isFavorite)

            Glide.with(binding.root)
                .load(recipe.imagePath)
                .placeholder(R.drawable.background_placeholder)
                .into(binding.ivRecipe)

            binding.llIngredients.removeAllViews()
            binding.llSteps.removeAllViews()

            recipe.ingredients.forEach { addIngredientField(it) }
            recipe.steps.forEach { addStepField(it) }
        }
    }

    private fun setFavoriteIconColor(isFavorite: Boolean) {
        binding.ivFavorite.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                if (isFavorite) R.color.color_heart_enabled else R.color.color_heart_disabled
            ),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    private fun addIngredientField(data: IngredientModel) {
        val view = ItemIngredientListItemBinding.inflate(LayoutInflater.from(context), null, false)
        view.tvIngredientAmount.text = data.amount
        view.tvIngredientName.text = data.name
        binding.llIngredients.addView(view.root)
    }

    private fun addStepField(data: StepModel) {
        val view = ItemStepBinding.inflate(LayoutInflater.from(context), null, false)
        view.tvStep.text = getString(R.string.step, data.position)
        view.tvDescription.text = data.description
        binding.llSteps.addView(view.root)
    }

}
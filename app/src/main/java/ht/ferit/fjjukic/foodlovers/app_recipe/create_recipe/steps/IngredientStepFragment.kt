package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps

import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.model.IngredientModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CreateRecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.custom_view.IngredientView
import ht.ferit.fjjukic.foodlovers.databinding.FragmentIngredientsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class IngredientStepFragment : BaseFragment<CreateRecipeViewModel, FragmentIngredientsBinding>() {
    override val layoutId: Int = R.layout.fragment_ingredients
    override val viewModel: CreateRecipeViewModel by sharedViewModel()

    override fun init() {
        setListeners()
    }

    private fun setListeners() {
        binding.tvAddIngredient.setOnClickListener {
            addIngredientField()
        }
        binding.ivInfoMetrics.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded)
                .apply {
                    setView(
                        LayoutInflater.from(context).inflate(R.layout.allowed_metrics_layout, null)
                    )
                }
                .show()
        }
        binding.actCategories.setOnItemClickListener { _, view, _, _ ->
            viewModel.onCategoryChanged((view as AppCompatTextView).text.toString())
        }
        binding.actDifficulties.setOnItemClickListener { _, view, _, _ ->
            viewModel.onDifficultyChanged((view as AppCompatTextView).text.toString())
        }
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.categories.observeNotNull(viewLifecycleOwner) { categories ->
            binding.actCategories.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_item,
                    categories.map { it.name })
            )
            val defaultCategory = categories.first().name
            binding.actCategories.setText(defaultCategory, false)
            viewModel.onCategoryChanged(defaultCategory)
        }
        viewModel.difficulties.observeNotNull(viewLifecycleOwner) { difficulties ->
            binding.actDifficulties.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_item,
                    difficulties.map { it.name })
            )
            val defaultDifficulty = difficulties.first().name
            binding.actDifficulties.setText(defaultDifficulty, false)
            viewModel.onDifficultyChanged(defaultDifficulty)
        }
    }

    override fun onPause() {
        super.onPause()
        saveIngredients()
    }

    private fun addIngredientField() {
        val view = IngredientView(requireContext()).apply {
            id = View.generateViewId()
            toggleCloseIcon(true)
            setListener { view ->
                val anim = AlphaAnimation(1f, 0f)
                anim.duration = 250
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        binding.llIngredients.removeView(view)
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                })
                startAnimation(anim)
            }
        }
        binding.llIngredients.addView(view)
    }

    private fun saveIngredients() {
        val ingredients = mutableListOf<IngredientModel>()
        binding.llIngredients.children.forEach { view ->
            (view as? IngredientView)?.getData().takeIf { it != null }?.let { ingredient ->
                ingredients.add(ingredient)
            }
        }
        if (ingredients.isNotEmpty()) {
            viewModel.setIngredients(ingredients)
        }
    }

}
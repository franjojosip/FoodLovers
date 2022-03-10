package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps

import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CreateRecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.view.IngredientView
import ht.ferit.fjjukic.foodlovers.databinding.FragmentIngredientsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class IngredientStepFragment: BaseFragment<CreateRecipeViewModel, FragmentIngredientsBinding>() {
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
                    setView(LayoutInflater.from(context).inflate(R.layout.allowed_metrics_layout, null))
                }
                .show()
        }
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

}
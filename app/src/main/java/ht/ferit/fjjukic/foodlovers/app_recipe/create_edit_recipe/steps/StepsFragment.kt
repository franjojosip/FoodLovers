package ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.steps

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.view.children
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.StepModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.RecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.custom_view.StepView
import ht.ferit.fjjukic.foodlovers.databinding.FragmentStepsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StepsFragment : BaseFragment<RecipeViewModel, FragmentStepsBinding>() {
    override val layoutId: Int = R.layout.fragment_steps
    override val viewModel: RecipeViewModel by sharedViewModel()

    override fun init() {
        binding.tvAddStep.setOnClickListener {
            addStepField()
        }
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
        binding.llSteps.removeAllViews()
        data.steps.forEachIndexed { index, value ->
            addStepField(value, index != 0)
        }
    }

    override fun onPause() {
        super.onPause()
        saveSteps()
    }

    private fun addStepField(data: StepModel? = null, isDeletable: Boolean = true) {
        val view = StepView(requireContext()).apply {
            id = View.generateViewId()
            toggleCloseIcon(isDeletable)

            data?.let {
                stepBinding.etStepDescription.setText(it.description)
            }
            setStepNumber(binding.llSteps.childCount + 1)
            setListener { view ->
                val anim = AlphaAnimation(1f, 0f)
                anim.duration = 250
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        binding.llSteps.removeView(view)
                        binding.llSteps.children.forEachIndexed { index, it ->
                            (it as? StepView)?.setStepNumber(index + 1)
                        }
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                })
                startAnimation(anim)
            }
        }
        binding.llSteps.addView(view)
    }

    private fun saveSteps() {
        val steps = mutableListOf<StepModel>()
        binding.llSteps.children.forEach { view ->
            (view as? StepView)?.getData().takeIf { it != null }?.let { step ->
                steps.add(step)
            }
        }
        if (steps.isNotEmpty()) {
            viewModel.setSteps(steps)
        }
    }
}
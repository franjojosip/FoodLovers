package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.view.children
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CreateRecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Step
import ht.ferit.fjjukic.foodlovers.app_recipe.view.StepView
import ht.ferit.fjjukic.foodlovers.databinding.FragmentStepsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StepsFragment : BaseFragment<CreateRecipeViewModel, FragmentStepsBinding>() {
    override val layoutId: Int = R.layout.fragment_steps
    override val viewModel: CreateRecipeViewModel by sharedViewModel()

    override fun init() {
        binding.tvAddStep.setOnClickListener {
            addStepField()
        }
    }

    override fun onPause() {
        super.onPause()
        saveSteps()
    }

    private fun addStepField() {
        val view = StepView(requireContext()).apply {
            id = View.generateViewId()
            toggleCloseIcon(true)
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
        val steps = mutableListOf<Step>()
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
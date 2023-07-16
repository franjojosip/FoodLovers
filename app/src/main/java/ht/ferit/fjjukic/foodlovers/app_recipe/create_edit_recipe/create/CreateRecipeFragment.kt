package ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.create

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseAnalyticsConstants
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.CustomPagerAdapter
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.RecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.steps.IngredientStepFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.steps.MainStepFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.steps.ReviewFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.steps.StepsFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentCreateRecipeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class CreateRecipeFragment : BaseFragment<RecipeViewModel, FragmentCreateRecipeBinding>() {

    override val screenConstant: String = FirebaseAnalyticsConstants.Event.Screen.CREATE_RECIPE

    override val layoutId: Int = R.layout.fragment_create_recipe
    override val viewModel: RecipeViewModel by sharedViewModel {
        parametersOf(null)
    }

    private val pagerAdapter by lazy {
        CustomPagerAdapter(
            this,
            listOf(
                MainStepFragment(),
                IngredientStepFragment(),
                StepsFragment(),
                ReviewFragment()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init()
    }

    override fun init() {
        binding.customStepperLayout.setNumOfSteps(RecipeViewModel.NUM_OF_STEPS)

        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.customStepperLayout.setStep(position)
            }
        })
    }
}
package ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.create

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.CustomPagerAdapter
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.RecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.steps.IngredientStepFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.steps.MainStepFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.steps.ReviewRecipeFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.steps.StepsFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentCreateRecipeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CreateRecipeFragment : BaseFragment<RecipeViewModel, FragmentCreateRecipeBinding>() {
    override val layoutId: Int = R.layout.fragment_create_recipe
    override val viewModel: RecipeViewModel by sharedViewModel()

    private val pagerAdapter by lazy {
        CustomPagerAdapter(
            this,
            listOf(
                MainStepFragment(),
                IngredientStepFragment(),
                StepsFragment(),
                ReviewRecipeFragment()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(null)
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
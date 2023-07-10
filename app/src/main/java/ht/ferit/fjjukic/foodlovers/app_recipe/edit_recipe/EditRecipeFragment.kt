package ht.ferit.fjjukic.foodlovers.app_recipe.edit_recipe

import androidx.viewpager2.widget.ViewPager2
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CustomPagerAdapter
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps.IngredientStepFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps.MainStepFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps.ReviewRecipeFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps.StepsFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentCreateRecipeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EditRecipeFragment : BaseFragment<EditRecipeViewModel, FragmentCreateRecipeBinding>() {

    override val layoutId: Int = R.layout.fragment_create_recipe
    override val viewModel: EditRecipeViewModel by sharedViewModel()

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

    override fun init() {
        binding.customStepperLayout.setNumOfSteps(viewModel.numOfSteps)

        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.customStepperLayout.setStep(position)
            }
        })
    }
}
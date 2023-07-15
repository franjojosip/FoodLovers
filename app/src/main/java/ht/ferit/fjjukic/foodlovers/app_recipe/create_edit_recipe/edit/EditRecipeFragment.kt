package ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.edit

import android.os.Bundle
import androidx.navigation.fragment.navArgs
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
import ht.ferit.fjjukic.foodlovers.databinding.FragmentEditRecipeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EditRecipeFragment : BaseFragment<RecipeViewModel, FragmentEditRecipeBinding>() {

    private val args: EditRecipeFragmentArgs by navArgs()

    override val screenConstant: String = FirebaseAnalyticsConstants.Event.Screen.EDIT_RECIPE

    override val layoutId: Int = R.layout.fragment_edit_recipe
    override val viewModel: RecipeViewModel by sharedViewModel()

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
        viewModel.init(args.id)
    }

    override fun init() {
        loader = binding.loaderLayout
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
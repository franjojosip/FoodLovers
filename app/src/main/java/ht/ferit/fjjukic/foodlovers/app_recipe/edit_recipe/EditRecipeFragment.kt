package ht.ferit.fjjukic.foodlovers.app_recipe.edit_recipe

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CustomPagerAdapter
import ht.ferit.fjjukic.foodlovers.app_recipe.edit_recipe.steps.IngredientStepEditFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.edit_recipe.steps.MainStepEditFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.edit_recipe.steps.ReviewRecipeEditFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.edit_recipe.steps.StepsEditFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentEditRecipeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EditRecipeFragment : BaseFragment<EditRecipeViewModel, FragmentEditRecipeBinding>() {

    private val args: EditRecipeFragmentArgs by navArgs()

    override val layoutId: Int = R.layout.fragment_edit_recipe
    override val viewModel: EditRecipeViewModel by sharedViewModel()

    private val pagerAdapter by lazy {
        CustomPagerAdapter(
            this,
            listOf(
                MainStepEditFragment(),
                IngredientStepEditFragment(),
                StepsEditFragment(),
                ReviewRecipeEditFragment()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(args.id)
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
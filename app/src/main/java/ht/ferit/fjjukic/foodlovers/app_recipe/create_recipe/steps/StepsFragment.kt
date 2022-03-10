package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CreateRecipeViewModel
import ht.ferit.fjjukic.foodlovers.databinding.FragmentMainStepBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StepsFragment  : BaseFragment<CreateRecipeViewModel, FragmentMainStepBinding>() {
    override val layoutId: Int = R.layout.fragment_main_step
    override val viewModel: CreateRecipeViewModel by sharedViewModel()

    override fun init() {
    }

}
package ht.ferit.fjjukic.foodlovers.app_recipe.recipes

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.RecipesViewModel
import ht.ferit.fjjukic.foodlovers.databinding.FragmentRecipesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipesFragment : BaseFragment<RecipesViewModel, FragmentRecipesBinding>() {

    override val layoutId: Int = R.layout.fragment_recipes
    override val viewModel: RecipesViewModel by viewModel()

    override fun init() {
    }
}
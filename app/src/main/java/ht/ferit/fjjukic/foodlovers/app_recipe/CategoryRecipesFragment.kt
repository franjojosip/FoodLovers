package ht.ferit.fjjukic.foodlovers.app_recipe

import androidx.navigation.fragment.navArgs
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentCategoryRecipesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryRecipesFragment : BaseFragment<RecipesViewModel, FragmentCategoryRecipesBinding>() {

    private val args: CategoryRecipesFragmentArgs by navArgs()

    override val layoutId: Int = R.layout.fragment_category_recipes
    override val viewModel: RecipesViewModel by viewModel()

    override val hasToolbar: Boolean = true

    override fun init() {
        args.category?.let {
            binding.toolbarLayout.setTitle("$it recipes")
            viewModel.loadRecipes(it)
        } ?: run {
            parentFragmentManager.popBackStack()
        }
    }

}
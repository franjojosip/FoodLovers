package ht.ferit.fjjukic.foodlovers.app_recipe

import androidx.core.os.bundleOf
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentCategoryRecipesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryRecipesFragment : BaseFragment<RecipesViewModel, FragmentCategoryRecipesBinding>() {

    companion object {
        const val TAG = "CategoryRecipesFragment"
        private const val CATEGORY = "CATEGORY"

        operator fun invoke(category: String): CategoryRecipesFragment {
            return CategoryRecipesFragment().apply {
                arguments = bundleOf(CATEGORY to category)
            }
        }
    }

    private val category by lazy { arguments?.getString(CATEGORY) }

    override val layoutId: Int = R.layout.fragment_category_recipes
    override val viewModel: RecipesViewModel by viewModel()

    override fun init() {
        category?.let {
            binding.toolbarLayout.setTitle("$it recipes")
        }
        viewModel.loadRecipes(category)
    }

}
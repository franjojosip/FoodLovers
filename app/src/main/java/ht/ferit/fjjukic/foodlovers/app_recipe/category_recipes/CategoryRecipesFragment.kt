package ht.ferit.fjjukic.foodlovers.app_recipe.category_recipes

import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_recipe.RecipeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.BasicRecipesAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentCategoryRecipesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CategoryRecipesFragment :
    BaseFragment<CategoryRecipesViewModel, FragmentCategoryRecipesBinding>(),
    RecipeListener {

    private val args: CategoryRecipesFragmentArgs by navArgs()

    override val layoutId: Int = R.layout.fragment_category_recipes
    override val viewModel: CategoryRecipesViewModel by viewModel { parametersOf(args.category) }

    private val recipeAdapter: BasicRecipesAdapter = BasicRecipesAdapter(this)

    override fun onResume() {
        super.onResume()
        viewModel.logCategoryRecipesScreenEvent()
    }

    override fun init() {
        toolbar = binding.toolbarLayout
        loader = binding.loaderLayout

        viewModel.loadRecipes()

        binding.ivFilter.isSelected = viewModel.isAscending
        binding.toolbarLayout.setTitle("${args.category} recipes")

        binding.cvFilter.setOnClickListener {
            viewModel.onSortByClick()
            binding.ivFilter.isSelected = viewModel.isAscending
        }

        binding.searchView.handleSearch {
            viewModel.filterBySearch(it)
        }

        binding.searchView.handleEndIconClicked {
            viewModel.removeSearchFilter()
        }

        binding.recyclerView.adapter = recipeAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.filteredRecipes.observeNotNull(viewLifecycleOwner) {
            recipeAdapter.setData(it)
        }
    }

    override fun onRecipeClick(id: String) {
        viewModel.onRecipeClick(
            CategoryRecipesFragmentDirections.actionNavCategoryRecipesToNavGraphShowRecipe(
                id
            )
        )
    }

}
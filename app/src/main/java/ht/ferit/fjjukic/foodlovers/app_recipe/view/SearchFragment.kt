package ht.ferit.fjjukic.foodlovers.app_recipe.view

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.ViewUtils
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.navigateToScreen
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.view.CustomRemovableChipGroup
import ht.ferit.fjjukic.foodlovers.app_main.view.MainActivity
import ht.ferit.fjjukic.foodlovers.app_recipe.filter.FilterFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.home.HomeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.RecipeAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentSearchRecipesBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : BaseFragment<HomeViewModel, FragmentSearchRecipesBinding>(),
    CustomRemovableChipGroup.RemovableClickListener {

    companion object {
        const val TAG = "SearchFragment"
    }

    override val viewModel: HomeViewModel by sharedViewModel()
    override val layoutId: Int = R.layout.fragment_search_recipes

    private val recipeAdapter: RecipeAdapter by lazy {
        RecipeAdapter()
    }

    override fun init() {
        setupClickListeners()
        setupRecyclerView()
        setupSelectedFilters()
        setupSearch()
    }

    private fun setupClickListeners() {
        binding.cvFilter.setOnClickListener {
            (activity as MainActivity).navigateToScreen(
                FilterFragment(),
                FilterFragment.TAG
            )
        }
        binding.toolbarLayout.setupAction {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = recipeAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recipeAdapter.setData(viewModel.getFilteredRecipes())
    }

    private fun setupSelectedFilters() {
        binding.cgFilters.setListener(this)
        binding.cgFilters.setData(viewModel.filters)
    }

    private fun setupSearch() {
        binding.tvLastSearched.isVisible = viewModel.filters.isNotEmpty()
        binding.searchView.handleSearch {
            binding.searchView.clearText()

            val item = FilterItem.Search(it)

            viewModel.addSearchFilter(item)
            binding.cgFilters.addChip(item)
        }
        binding.searchView.showKeyboardAndFocus()
    }

    override fun onItemClicked(item: FilterItem) {
        viewModel.removeFilter(item)
    }
}
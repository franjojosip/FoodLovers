package ht.ferit.fjjukic.foodlovers.app_recipe.search

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.navigateToScreen
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.view.CustomRemovableChipGroup
import ht.ferit.fjjukic.foodlovers.app_main.view.MainActivity
import ht.ferit.fjjukic.foodlovers.app_recipe.filter.FilterFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.home.HomeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NoRecipePlaceholder
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
        RecipeAdapter().apply {
            setData(mutableListOf(NoRecipePlaceholder))
        }
    }

    override fun init() {
        setListeners()
        setScreen()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.filters.observeNotNull(viewLifecycleOwner) {
            binding.tvLastSearched.isVisible = it.isNotEmpty()
            binding.cgFilters.setData(it)
            recipeAdapter.setData(viewModel.getFilteredRecipes()) //Remove this when search recipes implemented
        }
    }

    private fun setListeners() {
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

    private fun setScreen() {
        binding.recyclerView.adapter = recipeAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.tvLastSearched.isVisible = viewModel.filters.value?.isNotEmpty() == true

        binding.searchView.handleSearch {
            binding.searchView.clearText()
            viewModel.addSearchFilter(it)
        }

        binding.cgFilters.setListener(this)
    }

    override fun onItemClicked(item: FilterItem) {
        viewModel.removeFilter(item)
    }
}
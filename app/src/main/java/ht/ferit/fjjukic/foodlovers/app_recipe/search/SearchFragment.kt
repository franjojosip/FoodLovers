package ht.ferit.fjjukic.foodlovers.app_recipe.search

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseAnalyticsConstants
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.view.CustomRemovableChipGroup
import ht.ferit.fjjukic.foodlovers.app_recipe.RecipeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.BasicRecipesAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>(),
    CustomRemovableChipGroup.RemovableClickListener, RecipeListener {

    override val viewModel: SearchViewModel by sharedViewModel()
    override val layoutId: Int = R.layout.fragment_search

    private lateinit var recipeAdapter: BasicRecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeAdapter = BasicRecipesAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.logScreenEvent(FirebaseAnalyticsConstants.Event.Screen.SEARCH)
    }

    override fun init() {
        toolbar = binding.toolbarLayout
        loader = binding.loaderLayout

        binding.searchView.showKeyboardAndFocus()
        viewModel.removeSearchFilter()

        binding.recyclerView.adapter = recipeAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.cvFilter.setOnClickListener {
            viewModel.onFilterClick()
        }

        binding.searchView.handleSearch {
            viewModel.addSearchFilter(it)
        }

        binding.searchView.handleEndIconClicked {
            viewModel.removeSearchFilter()
        }

        binding.cgFilters.setListener(this)
        binding.cgHistory.setListener(this)
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.selectedFilters.observeNotNull(viewLifecycleOwner) {
            binding.tvFilters.isVisible = it.isNotEmpty()
            binding.cgFilters.setData(it)
        }
        viewModel.searchHistory.observeNotNull(viewLifecycleOwner) {
            binding.tvHistory.isVisible = it.isNotEmpty()
            binding.cgHistory.setData(it)
        }
        viewModel.filteredRecipes.observe(viewLifecycleOwner) { recipes ->
            recipes?.let { recipeAdapter.setData(it) }
        }
    }

    override fun onItemClick(item: FilterItem) {
        viewModel.removeFilter(item)
    }

    override fun onRecipeClick(id: String) {
        viewModel.onRecipeClick(id)
    }
}
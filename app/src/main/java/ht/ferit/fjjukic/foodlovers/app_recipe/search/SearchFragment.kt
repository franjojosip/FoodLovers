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
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.BasicRecipesAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentSearchRecipesBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : BaseFragment<HomeViewModel, FragmentSearchRecipesBinding>(),
    CustomRemovableChipGroup.RemovableClickListener {

    override val viewModel: HomeViewModel by sharedViewModel()
    override val layoutId: Int = R.layout.fragment_search_recipes

    private val recipeAdapter: BasicRecipesAdapter by lazy {
        BasicRecipesAdapter().apply {
            setData(mutableListOf(NoRecipePlaceholder))
        }
    }

    override fun init() {
        toolbar = binding.toolbarLayout

        setListeners()
        setScreen()
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.filters.observeNotNull(viewLifecycleOwner) {
            binding.tvFilters.isVisible = it.isNotEmpty()
            binding.cgFilters.setData(it)
        }
        viewModel.searchHistory.observeNotNull(viewLifecycleOwner) {
            binding.tvHistory.isVisible = it.isNotEmpty()
            binding.cgHistory.setData(it)
        }
        viewModel.currentRecipes.observe(viewLifecycleOwner) { recipes ->
            recipes?.let { recipeAdapter.setData(it) }
        }
    }

    private fun setListeners() {
        binding.cvFilter.setOnClickListener {
            (activity as MainActivity).navigateToScreen(
                FilterFragment(),
                FilterFragment.TAG
            )
        }
    }

    private fun setScreen() {
        binding.recyclerView.adapter = recipeAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.searchView.handleSearch {
            binding.searchView.clearText()
            viewModel.addSearchFilter(it)
        }

        binding.cgFilters.setListener(this)
        binding.cgHistory.setListener(this)
    }

    override fun onItemClicked(item: FilterItem) {
        viewModel.removeFilter(item)
    }
}
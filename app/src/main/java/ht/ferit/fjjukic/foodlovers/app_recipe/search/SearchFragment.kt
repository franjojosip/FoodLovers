package ht.ferit.fjjukic.foodlovers.app_recipe.search

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.view.CustomRemovableChipGroup
import ht.ferit.fjjukic.foodlovers.app_recipe.RecipeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.BasicRecipesAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentSearchRecipesBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchRecipesBinding>(),
    CustomRemovableChipGroup.RemovableClickListener, RecipeListener {

    override val viewModel: SearchViewModel by sharedViewModel()
    override val layoutId: Int = R.layout.fragment_search_recipes

    private lateinit var recipeAdapter: BasicRecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeAdapter = BasicRecipesAdapter(this)
    }

    override fun init() {
        viewModel.init()
        toolbar = binding.toolbarLayout

        binding.cvFilter.setOnClickListener {
            viewModel.onFilterClick()
        }

        binding.recyclerView.adapter = recipeAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.searchView.handleSearch {
            binding.searchView.clearText()
            viewModel.addSearchFilter(it)
        }

        binding.cgFilters.setListener(this)
        binding.cgHistory.setListener(this)
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

    override fun onItemClick(item: FilterItem) {
        viewModel.removeFilter(item)
    }

    override fun onRecipeClick(id: String) {
        viewModel.onRecipeClick(
            SearchFragmentDirections.actionNavSearchRecipesToNavGraphShowRecipe(id)
        )
    }
}
package ht.ferit.fjjukic.foodlovers.app_recipe.recipes

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseAnalyticsConstants
import ht.ferit.fjjukic.foodlovers.app_recipe.RecipeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.BasicRecipesAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentRecipesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipesFragment : BaseFragment<RecipesViewModel, FragmentRecipesBinding>(),
    RecipeListener {

    override val layoutId: Int = R.layout.fragment_recipes
    override val viewModel: RecipesViewModel by viewModel()

    private val recipesAdapter = BasicRecipesAdapter(this)

    override fun onResume() {
        super.onResume()
        viewModel.logScreenEvent(FirebaseAnalyticsConstants.Event.Screen.RECIPES)
    }

    override fun init() {
        loader = binding.loaderLayout
        viewModel.loadRecipes()

        binding.ivFilter.isSelected = viewModel.isAscending

        binding.recyclerView.adapter = recipesAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.fab.setOnClickListener {
            findNavController().navigate(
                RecipesFragmentDirections.actionNavRecipesToNavCreateRecipe()
            )
        }

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

        viewModel.filteredRecipes.observe(viewLifecycleOwner) { recipes ->
            val count = recipes.count { it.title.isNotBlank() }
            binding.tvTotalRecipes.text = getString(R.string.recipe_total_recipes, count)
            recipes?.let { recipesAdapter.setData(it) }
        }
    }

    override fun onRecipeClick(id: String) {
        viewModel.onRecipeClick(
            RecipesFragmentDirections.actionNavRecipesToNavGraphShowRecipe(id)
        )
    }
}
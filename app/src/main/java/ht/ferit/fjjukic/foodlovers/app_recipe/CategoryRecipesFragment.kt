package ht.ferit.fjjukic.foodlovers.app_recipe

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.recipes.RecipesFragmentDirections
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.BasicRecipesAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentCategoryRecipesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class
CategoryRecipesFragment : BaseFragment<RecipesViewModel, FragmentCategoryRecipesBinding>(),
    RecipeListener {

    private val args: CategoryRecipesFragmentArgs by navArgs()

    override val layoutId: Int = R.layout.fragment_category_recipes
    override val viewModel: RecipesViewModel by viewModel()

    private val recipeAdapter: BasicRecipesAdapter = BasicRecipesAdapter(this)

    override fun init() {
        viewModel.loadRecipes(args.category)

        toolbar = binding.toolbarLayout

        binding.toolbarLayout.setTitle("${args.category} recipes")

        binding.cvFilter.setOnClickListener {
            viewModel.handleSortBy(binding.ivFilter.isSelected)
            binding.ivFilter.isSelected = !binding.ivFilter.isSelected
        }

        binding.searchView.handleSearch {
//            viewModel.addSearchFilter(it)
        }

        binding.recyclerView.adapter = recipeAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.recipes.observeNotNull(viewLifecycleOwner) {
            recipeAdapter.setData(it)
        }
    }

    override fun onRecipeClick(id: String) {
        findNavController().navigate(
            RecipesFragmentDirections.actionNavRecipesToNavGraphShowRecipe(id)
        )
    }

}
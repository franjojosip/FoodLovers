package ht.ferit.fjjukic.foodlovers.app_recipe.recipes

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.RecipeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.RecipesViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.BasicRecipesAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentRecipesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipesFragment : BaseFragment<RecipesViewModel, FragmentRecipesBinding>(),
    RecipeListener {

    override val layoutId: Int = R.layout.fragment_recipes
    override val viewModel: RecipesViewModel by viewModel()

    private val recipesAdapter = BasicRecipesAdapter(this)

    override fun init() {
        viewModel.loadRecipes(null)

        binding.recyclerView.adapter = recipesAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.fab.setOnClickListener {
            findNavController().navigate(
                RecipesFragmentDirections.actionNavRecipesToNavCreateRecipe()
            )
        }

        viewModel.currentRecipes.observe(viewLifecycleOwner) { recipes ->
            binding.tvTotalRecipes.text = "${recipes.count()} total recipes"
            recipes?.let { recipesAdapter.setData(it) }
        }
    }

    override fun onRecipeClick(id: String) {
        findNavController().navigate(
            RecipesFragmentDirections.actionNavHomeToNavGraphShowRecipe(id)
        )
    }
}
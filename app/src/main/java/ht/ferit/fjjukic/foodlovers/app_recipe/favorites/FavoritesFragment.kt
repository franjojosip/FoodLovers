package ht.ferit.fjjukic.foodlovers.app_recipe.favorites

import androidx.recyclerview.widget.LinearLayoutManager
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.RecipeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.BasicRecipesAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoritesFragment : BaseFragment<FavoritesViewModel, FragmentFavoritesBinding>(),
    RecipeListener {

    override val layoutId: Int = R.layout.fragment_favorites
    override val viewModel: FavoritesViewModel by viewModel()

    private val recipesAdapter = BasicRecipesAdapter(this)

    override fun init() {
        binding.recyclerView.adapter = recipesAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            binding.tvTotalRecipes.text = "${recipes.count()} total recipes"
            recipes?.let { recipesAdapter.setData(it) }
        }
    }

    override fun onRecipeClick(id: String) {
        viewModel.onRecipeClick(id)
    }
}
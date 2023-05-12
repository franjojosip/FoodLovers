package ht.ferit.fjjukic.foodlovers.app_recipe.home

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.utils.navigateToScreen
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.CategoryRecipesFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.HomeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.RecipeAdapter
import ht.ferit.fjjukic.foodlovers.app_recipe.search.SearchFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe.ShowRecipeFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(), HomeListener {

    companion object {
        const val TAG = "HomeFragment"
    }

    override val viewModel: HomeViewModel by viewModel()
    override val layoutId: Int = R.layout.fragment_home

    private val categoryAdapter: RecipeAdapter by lazy {
        RecipeAdapter(this).apply {
            setData(viewModel.getCategories())
        }
    }
    private val todayChoiceAdapter: RecipeAdapter by lazy {
        RecipeAdapter(this).apply {
            setData(viewModel.getTodayChoiceRecipes())
        }
    }
    private val topRecipesAdapter: RecipeAdapter by lazy {
        RecipeAdapter(this).apply {
            setData(viewModel.getTopRecipes())
        }
    }

    override fun init() {
        setListeners()
        setScreen()
    }

    private fun setListeners() {
        binding.searchView.disableSearch()
        binding.searchView.handleViewClicked {
            activity?.navigateToScreen(
                SearchFragment(),
                SearchFragment.TAG
            )
        }

        viewModel.actionNavigate.observeNotNull(viewLifecycleOwner) {
            if (it is ActionNavigate.ShowRecipe) {
                activity?.navigateToScreen(
                    ShowRecipeFragment(it.id),
                    ShowRecipeFragment.TAG
                )
            } else if (it is ActionNavigate.CategoryRecipes) {
                activity?.navigateToScreen(
                    CategoryRecipesFragment(it.category),
                    CategoryRecipesFragment.TAG
                )
            }
        }
    }

    private fun setScreen() {
        binding.categoryRecyclerView.adapter = categoryAdapter
        binding.categoryRecyclerView.layoutManager =
            LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.HORIZONTAL
            }

        binding.todayChoiceRecyclerView.adapter = todayChoiceAdapter
        binding.todayChoiceRecyclerView.layoutManager =
            LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.HORIZONTAL
            }

        binding.topRecipeRecyclerView.adapter = topRecipesAdapter
        binding.topRecipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCategoryClicked(category: String) {
        viewModel.onCategoryClicked(category)
    }

    override fun onRecipeClicked(id: String) {
        viewModel.onRecipeClicked(id)
    }
}
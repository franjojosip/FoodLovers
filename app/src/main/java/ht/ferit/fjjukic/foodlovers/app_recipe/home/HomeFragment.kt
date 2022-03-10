package ht.ferit.fjjukic.foodlovers.app_recipe.home

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.navigateToScreen
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_main.view.MainActivity
import ht.ferit.fjjukic.foodlovers.app_recipe.filter.FilterFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.RecipeAdapter
import ht.ferit.fjjukic.foodlovers.app_recipe.view.SearchFragment
import ht.ferit.fjjukic.foodlovers.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    companion object {
        const val TAG = "HomeFragment"
    }

    override val viewModel: HomeViewModel by viewModel()
    override val layoutId: Int = R.layout.fragment_home

    private val categoryAdapter: RecipeAdapter by lazy {
        RecipeAdapter().apply {
            setData(viewModel.getCategories())
        }
    }
    private val todayChoiceAdapter: RecipeAdapter by lazy {
        RecipeAdapter().apply {
            setData(viewModel.getTodayChoiceRecipes())
        }
    }
    private val topRecipesAdapter: RecipeAdapter by lazy {
        RecipeAdapter().apply {
            setData(viewModel.getTopRecipes())
        }
    }

    private val snapHelper: LinearSnapHelper by lazy { LinearSnapHelper() }

    override fun init() {
        setupListeners()
        setupRecyclerView()
    }

    private fun setupListeners() {
        //TODO REMOVE THIS testScreen()

        binding.searchView.disableSearch()
        binding.searchView.handleViewClicked {
            (activity as MainActivity).navigateToScreen(
                SearchFragment(),
                SearchFragment.TAG
            )
        }
    }

    private fun testScreen() {
        (activity as MainActivity).navigateToScreen(
            SearchFragment(),
            SearchFragment.TAG
        )
    }

    private fun setupRecyclerView() {
        snapHelper.attachToRecyclerView(binding.categoryRecyclerView)

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
}
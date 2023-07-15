package ht.ferit.fjjukic.foodlovers.app_recipe.home

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseAnalyticsConstants
import ht.ferit.fjjukic.foodlovers.app_recipe.CategoryListener
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.CategoryAdapter
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.RecipeAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(), HomeListener,
    CategoryListener {

    override val screenConstant: String = FirebaseAnalyticsConstants.Event.Screen.HOME

    override val viewModel: HomeViewModel by viewModel()
    override val layoutId: Int = R.layout.fragment_home

    private val categoryAdapter = CategoryAdapter()
    private val todayChoiceAdapter = RecipeAdapter(this)
    private val topRecipesAdapter = RecipeAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter.setListener(this)
    }

    override fun init() {
        viewModel.init()
        loader = binding.loaderLayout

        binding.searchView.disableSearch()
        binding.searchView.handleViewClicked {
            viewModel.onSearchClick()
        }

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

    override fun setObservers() {
        super.setObservers()

        viewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.setData(it)
        }

        viewModel.todayChoiceRecipes.observe(viewLifecycleOwner) {
            todayChoiceAdapter.setData(it)
        }

        viewModel.topRecipes.observe(viewLifecycleOwner) {
            topRecipesAdapter.setData(it)
        }
    }

    override fun onCategoryClick(category: String) {
        viewModel.onCategoryClick(category)
    }

    override fun onRecipeClick(id: String) {
        viewModel.onRecipeClick(
            HomeFragmentDirections.actionNavHomeToNavGraphShowRecipe(id)
        )
    }
}
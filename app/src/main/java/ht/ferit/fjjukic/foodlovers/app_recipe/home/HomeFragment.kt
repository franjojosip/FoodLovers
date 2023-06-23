package ht.ferit.fjjukic.foodlovers.app_recipe.home

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.HomeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.RecipeAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(), HomeListener {
    override val viewModel: HomeViewModel by viewModel()
    override val layoutId: Int = R.layout.fragment_home

    private val categoryAdapter by lazy {
        RecipeAdapter(this)
            .apply {
                setData(viewModel.categories)
            }
    }

    private val todayChoiceAdapter = RecipeAdapter(this)
    private val topRecipesAdapter = RecipeAdapter(this)
    override fun init() {
        binding.searchView.disableSearch()
        binding.searchView.handleViewClicked {
            viewModel.navigateToSearch()
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

        viewModel.todayChoiceRecipes.observe(viewLifecycleOwner) {
            todayChoiceAdapter.setData(it)
        }

        viewModel.topRecipes.observe(viewLifecycleOwner) {
            topRecipesAdapter.setData(it)
        }

        viewModel.actionNavigate.observeNotNull(viewLifecycleOwner) {
            when (it) {
                is ActionNavigate.SearchRecipes -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionNavHomeToNavSearchRecipes()
                    )
                }

                is ActionNavigate.ShowRecipe -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionNavHomeToNavShowRecipe(it.id)
                    )
                }

                is ActionNavigate.CategoryRecipes -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionNavHomeToNavSearchCategory(it.category)
                    )
                }

                else -> {}
            }
        }
    }

    override fun onCategoryClicked(category: String) {
        viewModel.onCategoryClicked(category)
    }

    override fun onRecipeClicked(id: String) {
        viewModel.onRecipeClicked(id)
    }
}
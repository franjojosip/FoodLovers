package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.steps

import androidx.recyclerview.widget.LinearLayoutManager
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CreateRecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.RecipeAdapter
import ht.ferit.fjjukic.foodlovers.databinding.FragmentListItemsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ListStepsFragment: BaseFragment<CreateRecipeViewModel, FragmentListItemsBinding>() {
    override val layoutId: Int = R.layout.fragment_list_items
    override val viewModel: CreateRecipeViewModel by sharedViewModel()

    private val adapter: RecipeAdapter by lazy {
        RecipeAdapter().apply {
//            setData(viewModel.recipe.steps.toMutableList())
        }
    }

    override fun init() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
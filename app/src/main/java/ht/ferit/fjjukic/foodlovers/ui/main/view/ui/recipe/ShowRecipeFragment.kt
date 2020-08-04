package ht.ferit.fjjukic.foodlovers.ui.main.view.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ht.ferit.fjjukic.foodlovers.databinding.FragmentShowRecipeBinding
import ht.ferit.fjjukic.foodlovers.ui.base.RecipeViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.RecipeViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ShowRecipeFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory by instance<RecipeViewModelFactory>()
    private lateinit var viewModel: RecipeViewModel
    private lateinit var binding: FragmentShowRecipeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(RecipeViewModel::class.java)

        binding = FragmentShowRecipeBinding.inflate(
            inflater,
            container,
            false
        )
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        if (arguments != null) {
            val recipeId = requireArguments().getInt("id")
            viewModel.getRecipe(recipeId).observe(viewLifecycleOwner, Observer { recipe ->
                viewModel.recipe.value = recipe

                viewModel.getFoodType(recipe.typeID).observe(viewLifecycleOwner, Observer { foodType ->
                    viewModel.foodType.value = foodType
                })
                viewModel.getDifficultyLevel(recipe.difficultyLevelID)
                    .observe(viewLifecycleOwner, Observer {
                        viewModel.difficultyLevel.value = it
                    })
            })
        }
    }


}

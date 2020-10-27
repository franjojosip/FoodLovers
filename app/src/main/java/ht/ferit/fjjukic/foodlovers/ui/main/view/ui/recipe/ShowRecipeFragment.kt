package ht.ferit.fjjukic.foodlovers.ui.main.view.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ht.ferit.fjjukic.foodlovers.R
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
    private lateinit var ivRecipeImage: ImageView

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
            ivRecipeImage = requireView().findViewById(R.id.iv_recipe)
            viewModel.recipeID = requireArguments().getString("id")!!
            viewModel.getRecipe(viewModel.recipeID).observe(viewLifecycleOwner, Observer {
                if(it != null){
                    loadImage(it.imagePath)
                }
            })
            viewModel.getFoodTypes()
            viewModel.getDifficultyLevels()
        } else findNavController().navigate(R.id.nav_home, null)
    }

    private fun loadImage(url: String){
        Glide.with(this).load(url)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.app_icon)
            .error(android.R.drawable.stat_notify_error)
            .into(ivRecipeImage)
    }


}

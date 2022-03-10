package ht.ferit.fjjukic.foodlovers.app_recipe.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.databinding.FragmentShowRecipeBinding
import ht.ferit.fjjukic.foodlovers.app_recipe.view_model.ShowRecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull

class ShowRecipeFragment : Fragment() {
    companion object {
        const val RECIPE_ID: String = "recipe_ID"
    }

    private lateinit var viewModel: ShowRecipeViewModel
    private lateinit var binding: FragmentShowRecipeBinding
    private lateinit var ivRecipeImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        viewModel.init(arguments?.getString(RECIPE_ID) ?: "")
        setObservers()
    }

    private fun setObservers() {
        viewModel.recipe.observeNotNull(viewLifecycleOwner) {
            loadImage(it.toString())
        }
        viewModel.resultModel.observeNotNull(viewLifecycleOwner) {
            if (!it.message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
            if (!it.isSuccess) {
                //TODO Show error screen
            }
        }
    }

    private fun loadImage(url: String) {
        if (url.isEmpty()) return

        Glide.with(this).load(url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.ic_app)
            .error(android.R.drawable.stat_notify_error)
            .into(ivRecipeImage)
    }
}

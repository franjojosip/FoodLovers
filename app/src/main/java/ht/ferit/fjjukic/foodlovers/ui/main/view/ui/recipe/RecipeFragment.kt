package ht.ferit.fjjukic.foodlovers.ui.main.view.ui.recipe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.data.model.DifficultyLevel
import ht.ferit.fjjukic.foodlovers.data.model.FoodType
import ht.ferit.fjjukic.foodlovers.data.model.Recipe
import ht.ferit.fjjukic.foodlovers.data.repository.CodeRepository
import ht.ferit.fjjukic.foodlovers.databinding.FragmentRecipeBinding
import ht.ferit.fjjukic.foodlovers.ui.base.RecipeViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.RecipeViewModel
import kotlinx.android.synthetic.main.fragment_recipe.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class RecipeFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory by instance<RecipeViewModelFactory>()
    private lateinit var binding: FragmentRecipeBinding
    private lateinit var viewModel: RecipeViewModel
    private lateinit var btnFoodType: Button
    private lateinit var btnDifficultyMenu: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(RecipeViewModel::class.java)

        binding = FragmentRecipeBinding.inflate(
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
            viewModel.actionType = getString(R.string.update)
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Update Recipe"
            viewModel.recipeID = requireArguments().getInt("id")
            viewModel.getRecipe(viewModel.recipeID).observe(viewLifecycleOwner, Observer { recipe ->
                viewModel.setRecipe(recipe)
                viewModel.getFoodType(recipe.typeID).observe(viewLifecycleOwner, Observer {
                    viewModel.setFoodType(it)
                })
                viewModel.getDifficultyLevel(recipe.difficultyLevelID)
                    .observe(viewLifecycleOwner, Observer {
                        viewModel.setDifficultyLevel(it)
                    })
            })
        } else {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Add Recipe"
            viewModel.recipe.value = Recipe("", "", "", -1, -1)
        }

        setButtonListener()
        setMenu()
    }

    private fun setButtonListener() {
        val btnChooseImage: Button = requireView().findViewById(R.id.btn_choose_image)
        btnChooseImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permissions: Array<String> =
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, CodeRepository.PERMISSION_CODE)
            } else {
                chooseImageFromGallery()
            }
        }
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, CodeRepository.IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CodeRepository.IMAGE_PICK_CODE) {
            viewModel.recipe.value!!.imagePath = data?.data.toString()
        }
    }

    private fun setMenu() {
        btnFoodType = requireView().findViewById(R.id.btnFoodTypeMenu)
        btnDifficultyMenu = requireView().findViewById(R.id.btnDifficultyLevelMenu)
        val foodTypeMenu = PopupMenu(context, btnFoodTypeMenu)
        val difficultyLevelMenu = PopupMenu(context, btnDifficultyLevelMenu)

        viewModel.getFoodTypes().observe(viewLifecycleOwner, Observer {
            viewModel.foodTypes.value = it
            it.forEachIndexed { index, item ->
                foodTypeMenu.menu.add(Menu.NONE, item.id, index, item.name)
            }
        })

        viewModel.getDifficultyLevels().observe(viewLifecycleOwner, Observer {
            viewModel.difficultyLevels.value = it
            it.forEachIndexed { index, item ->
                difficultyLevelMenu.menu.add(Menu.NONE, item.id, index, item.name)
            }
        })

        foodTypeMenu.setOnMenuItemClickListener {
            viewModel.foodType.value = FoodType(it.title.toString())
            viewModel.recipe.value!!.typeID = it.itemId

            false
        }
        difficultyLevelMenu.setOnMenuItemClickListener {
            viewModel.difficultyLevel.value = DifficultyLevel(it.title.toString())
            viewModel.recipe.value!!.difficultyLevelID = it.itemId

            false
        }

        btnFoodType.setOnClickListener {
            foodTypeMenu.show()
        }

        btnDifficultyMenu.setOnClickListener {
            difficultyLevelMenu.show()
        }

    }
}

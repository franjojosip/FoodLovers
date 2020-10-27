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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.data.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.data.repository.CodeRepository
import ht.ferit.fjjukic.foodlovers.databinding.FragmentRecipeBinding
import ht.ferit.fjjukic.foodlovers.ui.base.RecipeViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.common.AuthListener
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.RecipeViewModel
import kotlinx.android.synthetic.main.fragment_recipe.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class RecipeFragment : Fragment(), KodeinAware, AuthListener {

    override val kodein by kodein()
    private val factory by instance<RecipeViewModelFactory>()
    private lateinit var binding: FragmentRecipeBinding
    private lateinit var viewModel: RecipeViewModel
    private lateinit var btnFoodType: Button
    private lateinit var btnDifficultyMenu: Button
    private lateinit var ivImage: ImageView

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
        init(view)
    }

    private fun init(view: View) {
        ivImage = view.findViewById(R.id.iv_image)
        viewModel.authListener = this

        if (arguments != null) {
            viewModel.actionType = getString(R.string.update)
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Update Recipe"
            viewModel.recipeID = requireArguments().getString("id")!!
            viewModel.getRecipe(viewModel.recipeID).observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    showRecipeImage(it.imagePath)
                }
            })
        } else {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Add Recipe"
            viewModel.recipe.value = RecipeModel()

        }
        setButtonListener()
        setMenu()
    }

    private fun showRecipeImage(imagePath: String) {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            550
        )
        params.setMargins(80, 0, 80, 0)
        ivImage.layoutParams = params

        Glide.with(this).load(imagePath)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.app_icon)
            .error(android.R.drawable.stat_notify_error)
            .into(ivImage)
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
            viewModel.setImagePath(data?.data.toString())
            showRecipeImage(data?.data.toString())
        }
    }

    private fun setMenu() {
        btnFoodType = requireView().findViewById(R.id.btnFoodTypeMenu)
        btnDifficultyMenu = requireView().findViewById(R.id.btnDifficultyLevelMenu)
        val foodTypeMenu = PopupMenu(context, btnFoodTypeMenu)
        val difficultyLevelMenu = PopupMenu(context, btnDifficultyLevelMenu)

        viewModel.getFoodTypes().observe(viewLifecycleOwner, Observer {
            foodTypeMenu.menu.clear()
            it.forEachIndexed { index, item ->
                foodTypeMenu.menu.add(Menu.NONE, index + 1, index + 1, item.name)
            }
        })

        viewModel.getDifficultyLevels().observe(viewLifecycleOwner, Observer {
            difficultyLevelMenu.menu.clear()
            it.forEachIndexed { index, item ->
                difficultyLevelMenu.menu.add(Menu.NONE, index + 1, index + 1, item.name)
            }
        })

        foodTypeMenu.setOnMenuItemClickListener {
            btnFoodTypeMenu.text = it.title
            val item = viewModel.foodTypes.value!!.first { item -> item.name.contains(it.title) }
            viewModel.setFoodType(item)

            false
        }
        difficultyLevelMenu.setOnMenuItemClickListener {
            btnDifficultyLevelMenu.text = it.title
            val item =
                viewModel.difficultyLevels.value!!.first { item -> item.name.contains(it.title) }
            viewModel.setDifficultyLevel(item)

            false
        }

        btnFoodType.setOnClickListener {
            foodTypeMenu.show()
        }

        btnDifficultyMenu.setOnClickListener {
            difficultyLevelMenu.show()
        }

    }

    override fun onSuccess() {
        Toast.makeText(context, "Task successfull!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.nav_home, null)
    }

    override fun onFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

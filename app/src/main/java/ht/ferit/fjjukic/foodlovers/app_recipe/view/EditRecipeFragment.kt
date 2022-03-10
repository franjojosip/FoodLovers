package ht.ferit.fjjukic.foodlovers.app_recipe.view

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.repository.CodeRepository
import ht.ferit.fjjukic.foodlovers.databinding.FragmentEditRecipeBinding
import ht.ferit.fjjukic.foodlovers.app_recipe.view_model.EditRecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.observeNotNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditRecipeFragment : Fragment() {
    companion object {
        const val RECIPE_ID: String = "recipe_ID"
    }

    private lateinit var binding: FragmentEditRecipeBinding
    private val viewModel: EditRecipeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRecipeBinding.inflate(
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
        setButtonListener()
        setMenu()
    }

    private fun showRecipeImage(imagePath: String) {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            550
        )
        params.setMargins(80, 0, 80, 0)
        binding.ivImage.layoutParams = params

        Glide.with(this).load(imagePath)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.ic_app)
            .error(android.R.drawable.stat_notify_error)
            .into(binding.ivImage)
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CodeRepository.IMAGE_PICK_CODE) {
            viewModel.setImagePath(data?.data.toString())
            showRecipeImage(data?.data.toString())
        }
    }

    private fun setMenu() {
        val foodTypeMenu = PopupMenu(context, binding.btnFoodTypeMenu)
        val difficultyLevelMenu = PopupMenu(context, binding.btnDifficultyLevelMenu)


        viewModel.foodTypes.observeNotNull(viewLifecycleOwner) {
            foodTypeMenu.menu.clear()
            it.forEachIndexed { index, item ->
                foodTypeMenu.menu.add(Menu.NONE, index + 1, index + 1, item.name)
            }
        }

        viewModel.difficultyLevels.observeNotNull(viewLifecycleOwner) {
            difficultyLevelMenu.menu.clear()
            it.forEachIndexed { index, item ->
                difficultyLevelMenu.menu.add(Menu.NONE, index + 1, index + 1, item.name)
            }
        }

        foodTypeMenu.setOnMenuItemClickListener {
            binding.btnFoodTypeMenu.text = it.title
            val item = viewModel.foodTypes.value!!.first { item -> item.name.contains(it.title.toString()) }
            viewModel.setFoodType(item)

            false
        }
        difficultyLevelMenu.setOnMenuItemClickListener {
            binding.btnDifficultyLevelMenu.text = it.title
            val item =
                viewModel.difficultyLevels.value!!.first { item -> item.name.contains(it.title.toString()) }
            viewModel.setDifficultyLevel(item)

            false
        }

        binding.btnFoodTypeMenu.setOnClickListener {
            foodTypeMenu.show()
        }

        binding.btnDifficultyLevelMenu.setOnClickListener {
            difficultyLevelMenu.show()
        }

    }
}

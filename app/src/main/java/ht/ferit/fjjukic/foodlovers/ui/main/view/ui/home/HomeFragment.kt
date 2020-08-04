package ht.ferit.fjjukic.foodlovers.ui.main.view.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.data.model.Recipe
import ht.ferit.fjjukic.foodlovers.ui.base.RecipeViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.common.ActionListener
import ht.ferit.fjjukic.foodlovers.ui.main.recycler.RecipeAdapter
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.RecipeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import ht.ferit.fjjukic.foodlovers.data.PreferenceManager
import ht.ferit.fjjukic.foodlovers.data.model.DifficultyLevel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory by instance<RecipeViewModelFactory>()
    private lateinit var viewModel: RecipeViewModel
    var mAdapter: RecipeAdapter? = null
    private lateinit var btnDifficultyLevelMenu: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, factory).get(RecipeViewModel::class.java)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()
        mAdapter =
            RecipeAdapter(object :
                ActionListener {
                override fun show(id: Int) {
                    val alertDialog: AlertDialog =
                        AlertDialog.Builder(requireContext()).create()
                    alertDialog.setTitle("Recipe")
                    alertDialog.setMessage("Do you want to delete or edit this recipe?")
                    alertDialog.setButton(
                        AlertDialog.BUTTON_NEUTRAL, "Cancel"
                    ) { dialog, _ ->
                        dialog.dismiss()
                    }
                    alertDialog.setButton(
                        AlertDialog.BUTTON_NEGATIVE, "Delete"
                    ) { dialog, _ ->
                        viewModel.deleteRecipe(id)
                        Toast.makeText(requireContext(), "Recipe successfully deleted", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    alertDialog.setButton(
                        AlertDialog.BUTTON_POSITIVE, "Edit"
                    ) { dialog, _ ->
                        navController.navigate(R.id.nav_recipe, Bundle().apply {
                            putInt("id", id)
                        }, null)
                        dialog.dismiss()
                    }
                    alertDialog.show()
                }

                override fun checkRecipe(id: Int) {
                    navController.navigate(R.id.nav_show_recipe, Bundle().apply {
                        putInt("id", id)
                    }, null)
                }
            })

        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }
        viewModel.getRecipes(PreferenceManager().getFoodTypeID()).observe(viewLifecycleOwner, Observer {
            mAdapter!!.refreshData(it as MutableList<Recipe>)
        })

        setMenu(view)
    }

    private fun setMenu(view: View) {
        btnDifficultyLevelMenu = view.findViewById(R.id.btnSelectDifficulty)
        val difficultyLevelMenu = PopupMenu(context, btnDifficultyLevelMenu)

        viewModel.getDifficultyLevels().observe(viewLifecycleOwner, Observer {
            viewModel.difficultyLevels.value = it
            difficultyLevelMenu.menu.add(Menu.NONE, 0, 0, "All")
            it.forEachIndexed { index, item ->
                difficultyLevelMenu.menu.add(Menu.NONE, item.id, index + 1, item.name)
            }
        })

        difficultyLevelMenu.setOnMenuItemClickListener {
            btnDifficultyLevelMenu.text = it.title
            mAdapter!!.filterData(it.itemId)
            false
        }

        btnDifficultyLevelMenu.setOnClickListener {
            difficultyLevelMenu.show()
        }
        btnDifficultyLevelMenu.text = "All"

    }
}

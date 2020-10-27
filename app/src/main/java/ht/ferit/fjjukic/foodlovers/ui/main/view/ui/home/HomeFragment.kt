package ht.ferit.fjjukic.foodlovers.ui.main.view.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.data.PreferenceManager
import ht.ferit.fjjukic.foodlovers.data.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.data.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.ui.base.RecipeViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.common.ActionListener
import ht.ferit.fjjukic.foodlovers.ui.main.recycler.RecipeAdapter
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.RecipeViewModel
import ht.ferit.fjjukic.foodlovers.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class HomeFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory by instance<RecipeViewModelFactory>()
    private lateinit var viewModel: RecipeViewModel
    private var mAdapter: RecipeAdapter? = null
    private lateinit var btnDifficultyLevelMenu: Button
    private lateinit var btnFoodTypeMenu: Button
    private lateinit var etSearch: EditText
    private lateinit var ivSearch: ImageView

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
                override fun show(id: String, userId: String, imageId: String) {
                    if (userId == FirebaseSource().currentUser()!!.uid) {
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
                            viewModel.deleteRecipe(id, imageId)
                            Toast.makeText(
                                requireContext(),
                                "Recipe successfully deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                        }
                        alertDialog.setButton(
                            AlertDialog.BUTTON_POSITIVE, "Edit"
                        ) { dialog, _ ->
                            navController.navigate(R.id.nav_recipe, Bundle().apply {
                                putString("id", id)
                            }, null)
                            dialog.dismiss()
                        }
                        alertDialog.show()
                    }
                }

                override fun checkRecipe(id: String) {
                    navController.navigate(R.id.nav_show_recipe, Bundle().apply {
                        putString("id", id)
                    }, null)
                }
            })

        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }
        viewModel.getRecipes()
            .observe(viewLifecycleOwner, Observer {
                mAdapter!!.refreshData(it as MutableList<RecipeModel>)
            })

        setMenu(view)
    }

    private fun setMenu(view: View) {
        btnDifficultyLevelMenu = view.findViewById(R.id.btnSelectDifficulty)
        btnFoodTypeMenu = view.findViewById(R.id.btnSelectFoodType)
        etSearch = view.findViewById(R.id.et_search)
        ivSearch = view.findViewById(R.id.iv_search)

        val difficultyLevelMenu = PopupMenu(context, btnDifficultyLevelMenu)
        val foodTypeMenu = PopupMenu(context, btnFoodTypeMenu)
        val foodTypeId = PreferenceManager().getFoodTypeID()
        val difficultyLevelId = PreferenceManager().getDifficultyLevelID()

        viewModel.getDifficultyLevels().observe(viewLifecycleOwner, Observer {
            btnDifficultyLevelMenu.text = getString(R.string.all_levels)
            difficultyLevelMenu.menu.clear()
            difficultyLevelMenu.menu.add(Menu.NONE, 0, 0, "All Levels")
            it.forEachIndexed { index, item ->
                difficultyLevelMenu.menu.add(Menu.CATEGORY_SYSTEM, index + 1, index + 1, item.name)
            }

            if (difficultyLevelId != "0" && it.isNotEmpty() && it.find { type -> type.id == difficultyLevelId } != null) {
                btnDifficultyLevelMenu.text =
                    (it.find { type -> type.id == difficultyLevelId })!!.name
            }
        })

        viewModel.getFoodTypes().observe(viewLifecycleOwner, Observer {
            btnFoodTypeMenu.text = getString(R.string.all_types)
            foodTypeMenu.menu.clear()
            foodTypeMenu.menu.add(Menu.NONE, 0, 0, "All Types")

            it.forEachIndexed { index, item ->
                foodTypeMenu.menu.add(Menu.NONE, index + 1, index + 1, item.name)
            }
            if (foodTypeId != "0" && it.isNotEmpty() && it.find { type -> type.id == foodTypeId } != null) {
                btnFoodTypeMenu.text =
                    (it.find { type -> type.id == foodTypeId })!!.name
            }
        })

        difficultyLevelMenu.setOnMenuItemClickListener {
            btnDifficultyLevelMenu.text = it.title

            if (!it.title.contains("All Levels") && !viewModel.difficultyLevels.value.isNullOrEmpty()) {
                val item =
                    viewModel.difficultyLevels.value!!.first { item -> item.name.contains(it.title) }
                PreferenceManager().setDifficultyLevelID(item.id)
            }
            else{
                PreferenceManager().setDifficultyLevelID("0")
            }
            mAdapter!!.filterData()
            false
        }

        foodTypeMenu.setOnMenuItemClickListener {
            btnFoodTypeMenu.text = it.title

            if (!it.title.contains("All Types") && !viewModel.foodTypes.value.isNullOrEmpty()) {
                val item =
                    viewModel.foodTypes.value!!.first { item -> item.name.contains(it.title) }
                PreferenceManager().setFoodTypeID(item.id)
            }
            else{
                PreferenceManager().setFoodTypeID("0")
            }
            mAdapter!!.filterData()
            false
        }

        btnDifficultyLevelMenu.setOnClickListener {
            difficultyLevelMenu.show()
        }

        btnFoodTypeMenu.setOnClickListener {
            foodTypeMenu.show()
        }

        etSearch.setOnEditorActionListener { text, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mAdapter!!.filterData(text.text.toString())
                requireContext().hideKeyboard(requireView())
            }
            false
        }

        etSearch.addTextChangedListener {
            mAdapter!!.filterData(etSearch.text.toString())
        }

        ivSearch.setOnClickListener{
            mAdapter!!.filterData(etSearch.text.toString())
        }

    }
}

package ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CreateRecipeViewPager(fragment: Fragment, private val fragmentList: List<Fragment>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = fragmentList.size
    override fun createFragment(position: Int) = fragmentList[position]
}
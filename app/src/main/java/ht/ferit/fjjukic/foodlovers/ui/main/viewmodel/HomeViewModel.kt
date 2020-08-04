package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import ht.ferit.fjjukic.foodlovers.data.repository.UserRepository

class HomeViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val user by lazy {
        repository.currentUser()
    }
}
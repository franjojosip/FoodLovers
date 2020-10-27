package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import ht.ferit.fjjukic.foodlovers.data.firebase.FirebaseSource

class HomeViewModel(
    private val firebaseSource: FirebaseSource
) : ViewModel() {

    val user by lazy {
        firebaseSource.currentUser()
    }
}
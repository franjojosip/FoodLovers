package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import android.location.Address
import android.location.Geocoder
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ht.ferit.fjjukic.foodlovers.data.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.data.model.UserModel
import ht.ferit.fjjukic.foodlovers.data.repository.UserRepository
import ht.ferit.fjjukic.foodlovers.ui.common.FirebaseDatabaseCallback
import ht.ferit.fjjukic.foodlovers.utils.startMainActivity
import java.util.*


class UserViewModel(
    private val firebaseSource: FirebaseSource,
    private val userRepository: UserRepository
) : ViewModel() {

    var firebaseUser = firebaseSource.currentUser()
    var currentUser: MutableLiveData<UserModel> = MutableLiveData()
    var location: MutableLiveData<String> = MutableLiveData()

    fun getUser(): MutableLiveData<UserModel> {
        if (currentUser.value == null) {
            userRepository.get(firebaseUser!!.uid, object : FirebaseDatabaseCallback {
                override fun <T : Any> onCallback(value: T?) {
                    if (value != null) {
                        currentUser.value = value as UserModel
                    }
                }
            })
        }
        return currentUser
    }

    fun update() {
        userRepository.update(currentUser.value!!)
    }

    fun logout(view: View) {
        firebaseSource.logout()
        view.context.startMainActivity()
        Toast.makeText(view.context, "Successfully logged out!", Toast.LENGTH_SHORT).show()
    }

    fun insert(user: UserModel) {
        userRepository.insert(user)
    }

    fun setLocation(view: View) {
        val geoCoder = Geocoder(view.context, Locale.getDefault())
        val addressList: List<Address>? =
            geoCoder.getFromLocation(
                currentUser.value!!.latitude.toDouble(), currentUser.value!!.longitude.toDouble(), 1
            )
        if (addressList != null && addressList.isNotEmpty()) {
            location.value = "${addressList[0].countryName}, ${addressList[0].locality}"
        }
    }
}
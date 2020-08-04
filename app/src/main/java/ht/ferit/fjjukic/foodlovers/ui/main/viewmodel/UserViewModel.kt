package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import android.location.Address
import android.location.Geocoder
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ht.ferit.fjjukic.foodlovers.data.model.User
import ht.ferit.fjjukic.foodlovers.data.repository.UserRepository
import ht.ferit.fjjukic.foodlovers.utils.startLoginActivity
import java.util.*


class UserViewModel(private val userRepository: UserRepository
) : ViewModel() {

    var firebaseUser = userRepository.currentUser()
    var user: MutableLiveData<User> = MutableLiveData()
    var location: MutableLiveData<String> = MutableLiveData()

    fun getUser(): LiveData<User>{
        if(firebaseUser != null){
            return userRepository.get(firebaseUser!!.email.toString())
        }
        return MutableLiveData()
    }

    fun update(){
        userRepository.update(user.value!!)
    }

    fun logout(view: View){
        userRepository.logout()
        view.context.startLoginActivity()
        Toast.makeText(view.context, "Successfully logged out!", Toast.LENGTH_SHORT).show()
    }

    fun insert(user: User){
        userRepository.insert(user)
    }

    fun setLocation(view: View) {
        val geoCoder = Geocoder(view.context, Locale.getDefault())
        val addressList: List<Address>? =
            geoCoder.getFromLocation(
                user.value!!.latitude, user.value!!.longitude, 1
            )
        if (addressList != null && addressList.isNotEmpty()) {
            location.value = "${addressList[0].countryName}, ${addressList[0].locality}"
        }
    }
}
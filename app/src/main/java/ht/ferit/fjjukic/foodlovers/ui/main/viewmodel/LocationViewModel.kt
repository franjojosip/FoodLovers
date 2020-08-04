package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import android.location.Address
import android.location.Geocoder
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import ht.ferit.fjjukic.foodlovers.data.model.User
import ht.ferit.fjjukic.foodlovers.data.repository.UserRepository
import java.util.*

class LocationViewModel (private val repository: UserRepository
) : ViewModel() {
    var location: MutableLiveData<String> = MutableLiveData()

    private val firebaseUser: FirebaseUser = repository.currentUser()!!
    var currentUser: MutableLiveData<User> = MutableLiveData()

    fun getUser(): LiveData<User>{
        return repository.get(firebaseUser.email!!)
    }

    fun updateUserLocation(currentLatLng: LatLng) {
        currentUser.value!!.latitude = currentLatLng.latitude
        currentUser.value!!.longitude = currentLatLng.longitude
        repository.update(currentUser.value!!)
    }

    fun setLocation(view: View) {
        val geoCoder = Geocoder(view.context, Locale.getDefault())
        val addressList: List<Address>? =
            geoCoder.getFromLocation(
                currentUser.value!!.latitude, currentUser.value!!.longitude, 1
            )
        if (addressList != null && addressList.isNotEmpty()) {
            location.value = "${addressList[0].countryName}, ${addressList[0].locality}"
        }
    }
}
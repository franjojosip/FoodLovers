package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import android.location.Address
import android.location.Geocoder
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import ht.ferit.fjjukic.foodlovers.data.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.data.model.UserModel
import ht.ferit.fjjukic.foodlovers.data.repository.UserRepository
import ht.ferit.fjjukic.foodlovers.ui.common.FirebaseDatabaseCallback
import java.util.*

class LocationViewModel(
    firebaseSource: FirebaseSource,
    private val repository: UserRepository
) : ViewModel() {
    var location: MutableLiveData<String> = MutableLiveData()

    private val firebaseUser: FirebaseUser = firebaseSource.currentUser()!!
    var currentUser: MutableLiveData<UserModel> = MutableLiveData()

    fun getUser(view: View) {
        repository.get(firebaseUser.uid, object : FirebaseDatabaseCallback {
            override fun <T : Any> onCallback(value: T?) {
                if (value != null) {
                    currentUser.value = value as UserModel
                    setLocation(view)
                }
            }
        })
    }

    fun updateUserLocation(currentLatLng: LatLng) {
        currentUser.value!!.latitude = currentLatLng.latitude.toString()
        currentUser.value!!.longitude = currentLatLng.longitude.toString()
        repository.update(currentUser.value!!)
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
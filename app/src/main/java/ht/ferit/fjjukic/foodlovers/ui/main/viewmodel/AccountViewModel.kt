package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import android.location.Address
import android.location.Geocoder
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import ht.ferit.fjjukic.foodlovers.data.model.User
import ht.ferit.fjjukic.foodlovers.data.repository.UserRepository
import ht.ferit.fjjukic.foodlovers.ui.common.AuthListener
import ht.ferit.fjjukic.foodlovers.utils.startLoginActivity
import ht.ferit.fjjukic.foodlovers.utils.startNavigationActivity
import io.reactivex.disposables.CompositeDisposable
import java.util.*


class AccountViewModel(
private val repository: UserRepository
) : ViewModel() {
    var newEmail: String? = null
    var password: String? = null
    var location: MutableLiveData<String> = MutableLiveData()
    var imagePath: MutableLiveData<String> = MutableLiveData()
    var authListener: AuthListener? = null

    private val disposables = CompositeDisposable()
    private var firebaseUser: FirebaseUser? = repository.currentUser()
    var currentUser: MutableLiveData<User> = MutableLiveData()

    fun changeInfo(view: View) {
        if (!newEmail.isNullOrEmpty()) {
            when {
                currentUser.value!!.username.isEmpty() -> {
                    authListener?.onFailure("Please check your username field!")
                }
                password.isNullOrEmpty() -> {
                    authListener?.onFailure("Please check your password field!")
                }
                newEmail == firebaseUser?.email -> {
                    authListener?.onFailure("The new email is the same as the old one!")
                }
                else -> {
                    val credential: AuthCredential =
                        EmailAuthProvider.getCredential(firebaseUser!!.email!!, password!!)
                    firebaseUser!!.reauthenticate(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                firebaseUser!!.updateEmail(newEmail!!)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            currentUser.value!!.email = newEmail!!
                                            repository.update(currentUser.value!!)
                                            authListener?.onSuccess()
                                            repository.logout()
                                            view.context.startLoginActivity()
                                        } else authListener?.onFailure(task.exception.toString())
                                    }
                            } else authListener?.onFailure(task.exception.toString())
                        }
                }
            }
        } else {
            if(currentUser.value!!.username.isEmpty()){
                authListener?.onFailure("Please check your username field!")
            }
            else{
                repository.update(currentUser.value!!)
                Toast.makeText(view.context, "Successfully updated user info!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun insert(user: User) = repository.insert(user)

    fun get(email: String): LiveData<User> {
        return repository.get(email)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun getUser() = repository.get(firebaseUser!!.email!!)

    fun setImagePath(imageUrl: String) {
        imagePath.value = imageUrl
        currentUser.value!!.imagePath = imageUrl
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

    fun getAll(): LiveData<List<User>> {
        return repository.getAll()
    }
}
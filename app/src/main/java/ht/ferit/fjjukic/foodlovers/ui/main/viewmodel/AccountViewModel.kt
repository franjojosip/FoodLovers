package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import android.location.Address
import android.location.Geocoder
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import ht.ferit.fjjukic.foodlovers.data.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.data.model.UserModel
import ht.ferit.fjjukic.foodlovers.data.repository.UserRepository
import ht.ferit.fjjukic.foodlovers.ui.common.AuthListener
import ht.ferit.fjjukic.foodlovers.ui.common.FirebaseDatabaseCallback
import ht.ferit.fjjukic.foodlovers.utils.startMainActivity
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class AccountViewModel(
    private val firebaseSource: FirebaseSource,
    private val repository: UserRepository
) : ViewModel() {
    var newEmail: String? = null
    var password: String? = null
    var location: MutableLiveData<String> = MutableLiveData()
    var imagePath: MutableLiveData<String> = MutableLiveData()
    var authListener: AuthListener? = null

    private val disposables = CompositeDisposable()
    var firebaseUser: FirebaseUser? = firebaseSource.currentUser()
    var currentUser: MutableLiveData<UserModel> = MutableLiveData()

    fun changeInfo(view: View) {
        if (!newEmail.isNullOrEmpty()) {
            when {
                currentUser.value!!.name.isEmpty() -> {
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
                                    .addOnCompleteListener { innerTask ->
                                        if (innerTask.isSuccessful) {
                                            currentUser.value!!.email = newEmail!!
                                            repository.update(currentUser.value!!)
                                            updateFirebaseDBUser()
                                            authListener?.onSuccess()
                                            firebaseSource.logout()
                                            view.context.startMainActivity()
                                        } else authListener?.onFailure(innerTask.exception.toString())
                                    }
                            } else authListener?.onFailure(task.exception.toString())
                        }
                }
            }
        } else {
            if (currentUser.value!!.name.isEmpty()) {
                authListener?.onFailure("Please check your username field!")
            } else {
                repository.update(currentUser.value!!)
                updateFirebaseDBUser()
                Toast.makeText(view.context, "Successfully updated user info!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun insert(user: UserModel) = repository.insert(user)

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun getUser(view: View): MutableLiveData<UserModel> {
        if (currentUser.value == null) {
            repository.get(firebaseUser!!.uid, object : FirebaseDatabaseCallback {
                override fun <T : Any> onCallback(value: T?) {
                    if (value != null) {
                        currentUser.value = value as UserModel
                        imagePath.value = value.imageUrl
                        setLocation(view)
                    }
                }
            })
        }
        return currentUser
    }

    fun getImagePath(): LiveData<String> {
        return imagePath
    }

    fun setImagePath(imageUrl: String) {
        imagePath.value = imageUrl
        currentUser.value!!.imageUrl = imageUrl
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

    private fun updateFirebaseDBUser() {
        val user = currentUser.value
        val fileName = firebaseUser!!.uid
        val ref = FirebaseStorage.getInstance().getReference("images/$fileName.jpg")
        ref.putFile(imagePath.value!!.toUri()).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                user!!.imageUrl = it.toString()
                repository.update(user)
            }
        }
    }
}
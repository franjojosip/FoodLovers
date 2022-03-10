package ht.ferit.fjjukic.foodlovers.app_common.firebase

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class FirebaseSource(private val preferenceManager: PreferenceManager) {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun login(email: String, password: String): Observable<String> {
        return Observable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful)
                        it.result.user?.let { user ->
                            emitter.onNext(user.uid)
                        }
                    else
                        emitter.onError(Throwable(it.exception?.message))
                }
            }
        }
    }

    fun register(email: String, username: String, password: String): Observable<UserModel> {
        return Observable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        it.result.user?.let { user ->
                            emitter.onNext(
                                UserModel(
                                    userId = user.uid,
                                    name = username,
                                    email = email
                                )
                            )
                        }
                    } else
                        emitter.onError(Throwable(it.exception?.message))
                }
            }
        }
    }

    fun resetPassword(email: String): Observable<Boolean> {
        return Observable.create { emitter ->
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            emitter.onNext(true)
                        } else
                            emitter.onError(Throwable(it.exception?.message))
                    }
                }
        }
    }

    fun reauthenticateUser(
        firebaseUser: FirebaseUser,
        email: String,
        password: String
    ): Observable<Boolean> {
        return Observable.create { emitter ->
            firebaseUser.reauthenticate(EmailAuthProvider.getCredential(email, password))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        emitter.onNext(true)
                    } else emitter.onError(Throwable(task.exception?.message))
                }
        }
    }

    fun updateEmail(
        firebaseUser: FirebaseUser,
        email: String
    ): Observable<Boolean> {
        return Observable.create { emitter ->
            firebaseUser.updateEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emitter.onNext(true)
                } else emitter.onError(Throwable(task.exception?.message))
            }
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        preferenceManager.user = null
    }

    fun currentUser() = firebaseAuth.currentUser

}
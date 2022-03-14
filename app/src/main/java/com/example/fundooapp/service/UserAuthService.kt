package com.example.fundooapp.service

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.fundooapp.api.ApiClient
import com.example.fundooapp.model.Constant
import com.example.fundooapp.model.LoginResponse
import com.example.fundooapp.model.SharedPreference
import com.example.fundooapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAuthService {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storageReference: StorageReference = FirebaseStorage.getInstance().reference
    private var apiClient: ApiClient = ApiClient()

    fun userLogIn(user: User, listener: (AuthListener) -> Unit) {
        loginWithApi(user.email, user.password, listener)
        firebaseAuth.signInWithEmailAndPassword(user.email,user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                listener(AuthListener(true, "Logged in Successfully"))
            }
        }
    }

    fun loginWithApi(email: String, password: String, listener: (AuthListener) -> Unit) {
       apiClient.getService().login(User(email = email, password = password, returnSecureToken = true))
       .enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    listener(AuthListener(true, "Logged in Successfully"))
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val message = t.localizedMessage
                Log.d("UserAuthService", "$message")
            }
        })
    }

    fun registerUser(user: User, listener: (AuthListener) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    storeDataFirestore(user,listener)
                    listener(AuthListener(true, "Register in Successfully"))
                }
            }
    }

    fun storeImageToFireStore(imageUri: Uri, context: Context) {
        val fileRef =
            storageReference.child("users/" + firebaseAuth.currentUser!!.uid + "/profilePicture.jpg")
        val uploadTask = fileRef.putFile(imageUri)
        uploadTask.addOnCompleteListener {
            fileRef.downloadUrl.addOnCompleteListener {
                firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
                    .update(IMG_URL, imageUri.toString())
                    SharedPreference.initSharedPreferences(context)
                    SharedPreference.addString(Constant.USER_PROFILE_PICTURE, imageUri.toString())
            }
        }
    }

    private fun storeDataFirestore(user: User, listener: (AuthListener) -> Unit) {
        val userDB: MutableMap<String, Any> = HashMap()
        userDB[NAME] = user.fullName
        userDB[EMAIL] = user.email
        userDB[USER_NAME] = user.userId
        userDB[PASSWORD] = user.password
        userDB[IMG_URL] = user.imgUrl
        firebaseAuth.currentUser?.let {
            firestore.collection("users").document(it.uid)
                .set(userDB)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        listener(AuthListener(true, "Added data successfully"))
                    } else {
                        Log.d(TAG, "get failed with ", it.exception)
                    }
                }
        }
    }

    fun getDataFromFirestore(context: Context, listener: (AuthListener) -> Unit){
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).get()
            .addOnCompleteListener {
                if (it.result.exists()) {
                    var nameResult = it.result.getString("name").toString()
                    var emailResult = it.result.getString("email").toString()
                    var imageUrl = it.result.getString("img_url")?.toUri().toString()
                    SharedPreference.initSharedPreferences(context)
                    SharedPreference.addString(Constant.USER_NAME, nameResult)
                    SharedPreference.addString(Constant.USER_EMAIL, emailResult)
                    SharedPreference.addString(Constant.USER_PROFILE_PICTURE, imageUrl)
                    listener(AuthListener(true, "Fetched data successfully"))
                } else {
                    Log.d("UserAuthService", "get the data $it")
                }
            }
    }

    fun googleLogIn(idToken: String, listener: (AuthListener) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                listener(AuthListener(true, "Logged in Successfully"))
            }
        }
    }

    companion object {
        const val NAME = "name"
        const val USER_NAME = "userName"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val IMG_URL = "img_url"
    }
}







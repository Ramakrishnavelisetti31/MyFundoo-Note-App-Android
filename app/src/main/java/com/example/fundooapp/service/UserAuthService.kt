package com.example.fundooapp.service

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.fundooapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserAuthService {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
    }

    fun userLogIn(email: String, password: String, listener: (AuthListener) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                listener(AuthListener(true, "Logged in Successfully"))
            }
        }
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


    fun storeImageToFireStore(imageUri: Uri) {
        val fileRef =
            storageReference.child("users/" + firebaseAuth.currentUser!!.uid + "/profilePicture.jpg")
        val uploadTask = fileRef.putFile(imageUri)
        uploadTask.addOnCompleteListener {
            fileRef.downloadUrl.addOnCompleteListener {
                firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
                    .update(IMG_URL, imageUri.toString())
            }
        }
    }

    fun storeDataFirestore(user: User,  listener: (AuthListener) -> Unit) {
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

    fun googleLogIn(idToken: String, listener: (AuthListener) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                listener(AuthListener(true, "Logged in Successfully"))
            }
        }
    }

    companion object {
        val NAME = "name"
        val USER_NAME = "userName"
        val EMAIL = "email"
        val PASSWORD = "password"
        val IMG_URL = "img_url"
    }
}







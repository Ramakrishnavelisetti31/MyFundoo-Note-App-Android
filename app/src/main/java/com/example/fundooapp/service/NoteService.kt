package com.example.fundooapp.service

import android.content.ContentValues
import android.util.Log
import com.example.fundooapp.model.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class NoteService {
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    fun storeNoteFirestore(notes: Notes, listener: (AuthListener) -> Unit) {
        val notesDB: MutableMap<String, Any> = HashMap()
        notesDB[TITLE] = notes.title
        notesDB[CONTENT] = notes.content
        firebaseAuth.currentUser?.let {
            firestore.collection("users").document(it.uid).collection("myNotes").document()
                .set(notesDB)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        listener(AuthListener(true, "Added notes successfully"))
                    } else {
                        Log.d(ContentValues.TAG, "get failed with ", it.exception)
                    }
                }
        }
    }

    companion object {
        val TITLE = "title"
        val CONTENT = "content"
    }
}


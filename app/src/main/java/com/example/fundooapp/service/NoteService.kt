package com.example.fundooapp.service

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

     fun getNotesFromFirestore(): LiveData<MutableList<Notes>> {
         val mutableData = MutableLiveData<MutableList<Notes>>()
         firestore.collection("users").document(firebaseAuth.currentUser!!.uid).collection("myNotes").get()
            .addOnSuccessListener { list ->
                val noteList = mutableListOf<Notes>()
                for (document in list) {
                    val title = document.getString("title")
                    val content = document.getString("content")
                    val notes = Notes(title!!, content!!)
                    noteList.add(notes)
                    Log.d("NoteService", "get the data $noteList")
                }
                mutableData.value = noteList
            }
         return mutableData
    }

    companion object {
        val TITLE = "title"
        val CONTENT = "content"
    }
}


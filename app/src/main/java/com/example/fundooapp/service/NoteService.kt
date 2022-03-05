package com.example.fundooapp.service

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.fundooapp.database.NoteDataBase
import com.example.fundooapp.model.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class NoteService {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var collectionReference: CollectionReference
    private lateinit var documentReference: DocumentReference
    private lateinit var noteDataBase: NoteDataBase

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        collectionReference =
            firestore.collection("users").document(firebaseAuth.currentUser!!.uid).collection("myNotes")
    }

    fun storeNoteFirestore(notes: Notes, context: Context, listener: (AuthListener) -> Unit) {
        firebaseAuth.currentUser?.let {
            documentReference = firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
                .collection("myNotes").document()
            notes.noteId = documentReference.id
            notes.userId = it.uid
            val notesDb = mutableMapOf<String, String>()
                notesDb["title"] = notes.title
                notesDb["content"] = notes.content
                notesDb["noteId"] = notes.noteId
                notesDb["userId"] = notes.userId
            noteDataBase = NoteDataBase(context)
            noteDataBase.saveData(notes)
            documentReference.set(notesDb).addOnCompleteListener {
                    if (it.isSuccessful) {
                        listener(AuthListener(true, "Added notes successfully"))
                        Log.d("NoteService", "get failed with ${notes.userId}, ${notes.noteId}")
                    } else {
                        Log.d(ContentValues.TAG, "get failed with ", it.exception)
                    }
                }
            }
        }

    fun getNotesFromFirestore(noteList: ArrayList<Notes>, context: Context, listener: (AuthListener) -> Unit) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).collection("myNotes")
            .get().addOnSuccessListener { list ->
                noteDataBase = NoteDataBase(context)
                noteDataBase.getData()
                for (document in list) {
                    val title = document.getString("title").toString()
                    val content = document.getString("content").toString()
                    val id = document.id
                    val notes = Notes(title, content, id)
                    noteList.add(notes)
                    listener(AuthListener(true, "Fetch notes successfully"))
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteNotesFromFirestore(noteId: String, context: Context) {
        noteDataBase = NoteDataBase(context)
        noteDataBase.deleteData(noteId)
        collectionReference.document(noteId).delete().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("NoteService", "delete note id is $noteId")
            } else {
                Log.d(ContentValues.TAG, "get failed with ", it.exception)
            }
        }
    }

    fun editNotesFirestore(notes: Notes, context: Context, listener: (AuthListener) -> Unit) {
        documentReference = firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("myNotes").document(notes.noteId)
        val notesDb = mutableMapOf<String, String>()
        notesDb["title"] = notes.title
        notesDb["content"] = notes.content
        noteDataBase = NoteDataBase(context)
        noteDataBase.upDateData(notes)
         documentReference.update(notesDb as Map<String, Any>)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener(AuthListener(true, "Edited notes successfully"))
                } else {
                    Log.d(ContentValues.TAG, "get failed with ", it.exception)
                }
            }
    }

}



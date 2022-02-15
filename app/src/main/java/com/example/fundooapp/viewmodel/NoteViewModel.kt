package com.example.fundooapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.Notes
import com.example.fundooapp.service.AuthListener
import com.example.fundooapp.service.NoteService

class NoteViewModel(val noteService: NoteService): ViewModel() {
    private val _saveNoteStatus = MutableLiveData<AuthListener>()
    val saveNoteStatus =  _saveNoteStatus as LiveData<AuthListener>

    fun addNotes(notes: Notes) {
        noteService.storeNoteFirestore(notes) {
            if (it.status) {
                _saveNoteStatus.value = it
            }
        }
    }

    fun getNotes(): LiveData<MutableList<Notes>> {
        val mutableData = MutableLiveData<MutableList<Notes>>()
        noteService.getNotesFromFirestore().observeForever { userList ->
            mutableData.value = userList
        }
        return mutableData
    }
}

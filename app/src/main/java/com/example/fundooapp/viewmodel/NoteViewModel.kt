package com.example.fundooapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.Notes
import com.example.fundooapp.service.AuthListener
import com.example.fundooapp.service.NoteService

class NoteViewModel(val noteService: NoteService): ViewModel() {
    private val _saveNoteStatus = MutableLiveData<AuthListener>()
    val saveNoteStatus =  _saveNoteStatus as LiveData<AuthListener>

    private val _getNoteStatus = MutableLiveData<AuthListener>()
    val getNoteStatus =  _getNoteStatus as LiveData<AuthListener>

    private val _editNoteStatus = MutableLiveData<AuthListener>()
    val editNoteStatus =  _editNoteStatus as LiveData<AuthListener>

    private val _getArchiveNoteStatus = MutableLiveData<Notes>()
    val getArchiveNoteStatus =  _getArchiveNoteStatus as LiveData<Notes>

    fun addNotes(notes: Notes, context: Context) {
        noteService.storeNoteFirestore(notes, context) {
            if (it.status) {
                _saveNoteStatus.value = it
            }
        }
    }

    fun getNotes(noteList: ArrayList<Notes>, context: Context) {
        noteService.getNotesFromFirestore(noteList, context) {
            if (it.status) {
                _getNoteStatus.value = it
            }
        }
    }

    fun editNotes(notes: Notes, context: Context) {
        noteService.editNotesFirestore(notes, context){
            if(it.status) {
                _editNoteStatus.value = it
            }
        }
    }

    fun archiveNotes(notes: Notes) {
        _getArchiveNoteStatus.value = notes
    }
}


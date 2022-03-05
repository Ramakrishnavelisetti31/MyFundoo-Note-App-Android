package com.example.fundooapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.model.Notes
import com.example.fundooapp.service.NoteService
import com.example.fundooapp.service.UserAuthService
import com.example.fundooapp.viewmodel.NoteViewModel
import com.example.fundooapp.viewmodel.NoteViewModelFactory
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


class EditNoteFragment : Fragment() {
    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText
    private lateinit var goToHome: ImageButton
    private lateinit var editNoteFabButton: FloatingActionButton
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var notes: Notes
    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_note, container, false)
        editTitle = view.findViewById(R.id.edit_title)
        editContent = view.findViewById(R.id.edit_content)
        goToHome = view.findViewById(R.id.button_back)
        editNoteFabButton = view.findViewById(R.id.edit_note_fab_button)
        val data = this.arguments
        notes = data?.getSerializable("note") as Notes
        editTitle.setText(notes.title)
        editContent.setText(notes.content)
        id = notes.noteId
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService())) [NoteViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goToHome.setOnClickListener { goToHome() }
        editNoteFabButton.setOnClickListener { editNoteData() }
    }

    private fun editNoteData() {
        val title = editTitle.text.toString()
        val content = editContent.text.toString()
        val notes = Notes(title = title, content = content, noteId = id)
        noteViewModel.editNotes(notes, requireContext())
        noteViewModel.editNoteStatus.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Error in adding notes ", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToHome() {
        sharedViewModel.setGoToViewNotePage(true)
    }
}
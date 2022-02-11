package com.example.fundooapp.view

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

class AddNoteFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var writeTitle: EditText
    private lateinit var writeContent: EditText
    private lateinit var goToHome: ImageButton
    private lateinit var saveNoteFabButton: FloatingActionButton
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_note, container, false)
        writeTitle = view.findViewById(R.id.write_title)
        writeContent = view.findViewById(R.id.write_content)
        goToHome = view.findViewById(R.id.back_button)
        saveNoteFabButton = view.findViewById(R.id.save_note_fab_button)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService())) [NoteViewModel::class.java]

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goToHome.setOnClickListener { goToHome() }
        saveNoteFabButton.setOnClickListener { createNote() }
    }

    private fun createNote() {
        val title = writeTitle.text.toString()
        val content = writeContent.text.toString()
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(requireContext(), "Both Fields are required", Toast.LENGTH_SHORT).show()
        } else {
            val notes = Notes(title = title, content = content)
            noteViewModel.addNotes(notes)
            noteViewModel.saveNoteStatus.observe(viewLifecycleOwner, Observer {
                if (it.status) {
                    sharedViewModel.setGoToHomePageStatus(true)
                }
                else {
                    Toast.makeText(requireContext(), "Error in adding notes ", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun goToHome() {
        activity?.onBackPressed()
    }

}
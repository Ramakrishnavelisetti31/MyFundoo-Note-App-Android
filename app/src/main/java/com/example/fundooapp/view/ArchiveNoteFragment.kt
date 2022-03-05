package com.example.fundooapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundooapp.R
import com.example.fundooapp.model.Notes
import com.example.fundooapp.service.NoteService
import com.example.fundooapp.viewmodel.NoteViewModel
import com.example.fundooapp.viewmodel.NoteViewModelFactory

class ArchiveNoteFragment : Fragment() {
    private lateinit var noteViewModel: NoteViewModel
    private var archiveList: ArrayList<Notes> = arrayListOf()
    private lateinit var archiveAdapter: ArchiveAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var notes: Notes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_archive_note, container, false)
        recyclerView = view.findViewById(R.id.archive_recycler_view)
        staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.setHasFixedSize(true)
        archiveAdapter = ArchiveAdapter(requireContext(), archiveList)
        recyclerView.adapter = archiveAdapter
        noteViewModel =
            ViewModelProvider(this, NoteViewModelFactory(NoteService()))[NoteViewModel::class.java]
        notes = Notes()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getArchive(notes)
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getArchive(aNotes: Notes) {
        archiveList.add(aNotes)
        archiveAdapter.setArchiveList(archiveList)
        archiveAdapter.notifyDataSetChanged()
    }

}

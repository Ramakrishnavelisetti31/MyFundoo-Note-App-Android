package com.example.fundooapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundooapp.R
import com.example.fundooapp.model.Notes
import com.example.fundooapp.service.NoteService
import com.example.fundooapp.viewmodel.NoteViewModel
import com.example.fundooapp.viewmodel.NoteViewModelFactory
import java.util.*

class ViewNotesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel
    private var noteList: ArrayList<Notes> = arrayListOf()
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var staggeredLinearLayoutManager: StaggeredGridLayoutManager
    private lateinit var notes: Notes
    private lateinit var archiveNoteFragment: ArchiveNoteFragment
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar
    private var page = 0
    private var item = 0
    private var flag = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view =  inflater.inflate(R.layout.fragment_view_notes, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        nestedScrollView = view.findViewById(R.id.scroll_View)
        progressBar = view.findViewById(R.id.progress_bar)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredLinearLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.setHasFixedSize(true)
        noteAdapter = NoteAdapter(requireContext(), noteList)
        recyclerView.adapter = noteAdapter
        notes = Notes()
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService()))[NoteViewModel::class.java]
        archiveNoteFragment = ArchiveNoteFragment()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewNotes(page, item)
        pagination()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun viewNotes(page: Int, limit: Int) {
        if (limit > page) {
            progressBar.visibility = View.GONE
            return
        }
        noteViewModel.getNotes(noteList, requireContext())
        noteViewModel.getNoteStatus.observe(viewLifecycleOwner, Observer {
            noteAdapter.setListData(noteList)
            noteAdapter.notifyDataSetChanged()
        })
    }

    private fun pagination() {
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                page++
                progressBar.visibility = View.VISIBLE
                viewNotes(page, item)
            }
        })
    }

    fun changeView() {
        if (!flag) {
            recyclerView.layoutManager = staggeredLinearLayoutManager
        } else {
            recyclerView.layoutManager = staggeredGridLayoutManager
            flag = true
        }
    }

    fun searchNotes(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                val tempArrayList = ArrayList<Notes>()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                        noteList.forEach {
                            if (it.title.lowercase(Locale.getDefault()).contains(searchText)) {
                                tempArrayList.add(it)
                            } else if (it.content.lowercase(Locale.getDefault()).contains(searchText))
                                tempArrayList.add(it)
                            }
                            noteAdapter.notifyDataSetChanged()
                        } else {
                        tempArrayList.clear()
                        tempArrayList.addAll(noteList)
                        noteAdapter.notifyDataSetChanged()
                }
                return false
                }
            })
    }
}


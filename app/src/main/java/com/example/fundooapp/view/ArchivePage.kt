package com.example.fundooapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundooapp.R
import com.example.fundooapp.model.Notes
import com.example.fundooapp.service.NoteService
import com.example.fundooapp.service.UserAuthService
import com.example.fundooapp.viewmodel.NoteViewModel
import com.example.fundooapp.viewmodel.NoteViewModelFactory
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.imageview.ShapeableImageView

class ArchivePage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var archiveAdapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel
    private var archiveList: ArrayList<Notes> = arrayListOf()
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var staggeredLinearLayoutManager: StaggeredGridLayoutManager
    private lateinit var archiveNotes: Notes
    private var flag = false
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var changeLayoutButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var editProfile: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_page)
        backButton = findViewById(R.id.back)
        searchView = findViewById(R.id.search_note)
        changeLayoutButton = findViewById(R.id.layout_changer)
        editProfile = findViewById(R.id.profile_image)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedViewModel = ViewModelProvider(this, SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]

        recyclerView = findViewById(R.id.view_recycler)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredLinearLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.setHasFixedSize(true)
        archiveAdapter = NoteAdapter(this, archiveList)
        recyclerView.adapter = archiveAdapter
        archiveNotes = Notes()
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService()))[NoteViewModel::class.java]
        searchNote()
        backButton.setOnClickListener { toHome() }
        editProfile.setOnClickListener { openProfileFragment() }
        changeLayoutButton.setOnClickListener { changeView()}
    }

    private fun searchNote() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                archiveAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun changeView() {
        if (flag == true) {
            recyclerView.layoutManager = staggeredGridLayoutManager
            changeLayoutButton.setImageResource(R.drawable.ic_baseline_single_column_24)
            flag = false
        } else if(flag == false) {
            recyclerView.layoutManager = staggeredLinearLayoutManager
            changeLayoutButton.setImageResource(R.drawable.ic_baseline_grid_view_24)
            flag = true
        }
    }

    private fun openProfileFragment() {
        val dialog = ProfileFragment()
        dialog.show(supportFragmentManager, "Open profile fragment")
    }

    private fun toHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}
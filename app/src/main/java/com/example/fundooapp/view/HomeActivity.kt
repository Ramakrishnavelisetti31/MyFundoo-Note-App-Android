package com.example.fundooapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundooapp.R
import com.example.fundooapp.model.Notes
import com.example.fundooapp.service.NoteService
import com.example.fundooapp.service.UserAuthService
import com.example.fundooapp.viewmodel.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel
    private var noteList: ArrayList<Notes> = arrayListOf()
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var staggeredLinearLayoutManager: StaggeredGridLayoutManager
    private lateinit var notes: Notes
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar
    private var flag = false
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var changeLayoutButton: ImageButton
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var addNoteFabButton: FloatingActionButton
    private lateinit var editProfile: ShapeableImageView
    private lateinit var networkConnectivity: NetworkConnectivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.tool_bar)
        addNoteFabButton = findViewById(R.id.add_note_fab_button)
        editProfile = findViewById(R.id.toolbar_profile_image)
        changeLayoutButton = findViewById(R.id.recycler_layout_changer)
        searchView = findViewById(R.id.search)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open, R.string.menu_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        sharedViewModel = ViewModelProvider(this, SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]
        networkConnectivity = NetworkConnectivity(this)

        recyclerView = findViewById(R.id.recycler_view)
        nestedScrollView = findViewById(R.id.scroll_View)
        progressBar = findViewById(R.id.progress_bar)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredLinearLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.setHasFixedSize(true)
        noteAdapter = NoteAdapter(this, noteList)
        recyclerView.adapter = noteAdapter
        notes = Notes()
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService())) [NoteViewModel::class.java]
        observeViews()
        isNetworkAvailable()
        searchNote()
        viewNotes()
        pagination()
        navigationMenu()
        editProfile.setOnClickListener { openProfileFragment() }
        addNoteFabButton.setOnClickListener { sharedViewModel.setGoToAddNotePage(true) }
        changeLayoutButton.setOnClickListener { changeView()}
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun viewNotes() {
        noteViewModel.getNotes(noteList, this)
        noteViewModel.getNoteStatus.observe(this, Observer {
            noteAdapter.setListData(noteList)
            noteAdapter.notifyDataSetChanged()
        })
        if (noteList.size == noteList.size) {
            progressBar.visibility = View.GONE
        }
    }

    private fun pagination() {
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                progressBar.visibility = View.VISIBLE
                viewNotes()
            }
        })
    }

    private fun searchNote() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                    noteAdapter.filter.filter(newText)
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

    private fun goToAddNoteFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.write_note_fragment, AddNoteFragment()).addToBackStack(null).commit()
    }

    private fun observeViews() {
        sharedViewModel.goToAddNotePage.observe(this, Observer {
            if (it == true) {
                goToAddNoteFragment()
            }
        })
        sharedViewModel.goToArchivePageStatus.observe(this, Observer {
            if (it == true) {
                val intent = Intent(this, ArchivePage::class.java)
                startActivity(intent)
                drawerLayout.closeDrawers()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        actionBarDrawerToggle.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    private fun navigationMenu() {
        navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId) {
                R.id.nav_notes -> drawerLayout.closeDrawers()
                R.id.nav_archive -> sharedViewModel.setGoToArchivePageStatus(true)
            }
            true
        }
    }

    private fun openProfileFragment() {
        val dialog = ProfileFragment()
        dialog.show(supportFragmentManager, "Open profile fragment")
    }

    private fun isNetworkAvailable() {
        networkConnectivity.observe(this, Observer { isAvailable ->
            if (isAvailable) {
                Toast.makeText(this, "Online", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Offline", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
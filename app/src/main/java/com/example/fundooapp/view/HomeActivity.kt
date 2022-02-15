package com.example.fundooapp.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.service.UserAuthService
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var addNoteFabButton: FloatingActionButton
    private lateinit var changeLayoutButton: ImageButton
    private lateinit var editProfile: ShapeableImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.tool_bar)
        addNoteFabButton = findViewById(R.id.add_note_fab_button)
        changeLayoutButton = findViewById(R.id.recycler_layout_changer)
        editProfile = findViewById(R.id.toolbar_profile_image)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open, R.string.menu_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        sharedViewModel = ViewModelProvider(this, SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]
        navigationMenu()
        editProfile.setOnClickListener { openProfileFragment() }
        addNoteFabButton.setOnClickListener { sharedViewModel.setGoToAddAndEditNotePage(true) }
        observeViews()
    }

    private fun navigationMenu() {
        navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId) {
                R.id.nav_notes -> sharedViewModel.setGoToViewNotePage(true)
                R.id.nav_archive -> sharedViewModel.setGoToArchiveNotePage(true)
            }
            true
        }
    }

    private fun goToAddAndEditNote() {
        supportFragmentManager.beginTransaction().replace(R.id.write_note_fragment, AddAndUpdateNoteFragment()).commit()
    }

    private fun goToViewNoteFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.note_view_fragment, ViewNotesFragment()).commit()
        drawerLayout.closeDrawers()
    }

    private fun goToArchiveNoteFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.note_view_fragment, ArchiveNoteFragment()).commit()
        drawerLayout.closeDrawers()
    }

    private fun observeViews() {
        sharedViewModel.goToAddAndEditNotePage.observe(this, Observer {
            if (it == true) {
                goToAddAndEditNote()
            }
        })
        sharedViewModel.goToViewNotePage.observe(this, Observer {
            if (it == true) {
                goToViewNoteFragment()
            }
        })
        sharedViewModel.goToArchiveNotePage.observe(this, Observer {
            if (it == true) {
                goToArchiveNoteFragment()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        actionBarDrawerToggle.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    private fun openProfileFragment() {
        val dialog = ProfileFragment()
        dialog.show(supportFragmentManager, "Open profile fragment")
    }
}
package com.example.fundooapp.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fundooapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var addNoteFabButton: FloatingActionButton
    private lateinit var editProfile: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        addNoteFabButton = findViewById(R.id.add_note_fab_button)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.tool_bar)
        editProfile = findViewById(R.id.toolbar_profile_image)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.menu_open,
            R.string.menu_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        editProfile.setOnClickListener { openProfileFragment() }
        addNoteFabButton.setOnClickListener { goToCreateNote() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        actionBarDrawerToggle.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    private fun goToCreateNote() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerNote, AddNoteFragment()).addToBackStack(null).commit()
    }

    private fun openProfileFragment() {
        val dialog = ProfileFragment()
        dialog.show(supportFragmentManager, "Open profile fragment")
    }
}
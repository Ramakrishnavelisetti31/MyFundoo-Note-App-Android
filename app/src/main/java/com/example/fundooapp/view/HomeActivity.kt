package com.example.fundooapp.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundooapp.R
import com.example.fundooapp.model.Notes
import com.example.fundooapp.service.NoteService
import com.example.fundooapp.viewmodel.NoteViewModel
import com.example.fundooapp.viewmodel.NoteViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class HomeActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var changeLayoutButton: ImageButton

    private lateinit var editProfile: ShapeableImageView
    private lateinit var addNoteFabButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteList: MutableList<Notes>
    private lateinit var myAdapter: MyAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.tool_bar)
        changeLayoutButton = findViewById(R.id.recycler_layout_changer)
        editProfile = findViewById(R.id.toolbar_profile_image)
        addNoteFabButton = findViewById(R.id.add_note_fab_button)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open, R.string.menu_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)
        noteList = mutableListOf()
        myAdapter = MyAdapter(noteList)
        recyclerView.adapter = myAdapter
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService())) [NoteViewModel::class.java]

        editProfile.setOnClickListener { openProfileFragment() }
        addNoteFabButton.setOnClickListener { goToCreateNote() }
        displayNotes()
    }

    private fun goToCreateNote() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerNote, AddNoteFragment()).addToBackStack(null).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        actionBarDrawerToggle.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    private fun displayNotes() {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).collection("myNotes")
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.d("Firestore error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val data = dc.document.toObject(Notes::class.java)
                            data.id = dc.document.id
                            noteList.add(data)
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun openProfileFragment() {
        val dialog = ProfileFragment()
        dialog.show(supportFragmentManager, "Open profile fragment")
    }
}
package com.example.fundooapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fundooapp.R


class ViewReminderNote : AppCompatActivity() {

    private lateinit var title: TextView
    private lateinit var content: TextView
    private lateinit var go: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reminder_note)

        title = findViewById(R.id.reminder_title)
        content = findViewById(R.id.reminder_content)
        go = findViewById(R.id.go)

        onNewIntent(intent)
        go.setOnClickListener { goHome() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val extras = intent.extras
        if (extras!!.containsKey(AlarmReceiver.TITLE)) {
            val noteTitle = extras.getString(AlarmReceiver.TITLE)
            val noteContent = extras.getString(AlarmReceiver.MESSAGE)
            title.text = noteTitle
            content.text = noteContent
        }
    }

    private fun goHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}
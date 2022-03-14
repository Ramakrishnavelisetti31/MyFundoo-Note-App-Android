package com.example.fundooapp.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import java.util.*

class AddNoteFragment : Fragment() {
    private lateinit var writeTitle: EditText
    private lateinit var writeContent: EditText
    private lateinit var goToHome: ImageButton
    private lateinit var saveNoteFabButton: FloatingActionButton
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var openReminder: ImageButton
    private lateinit var showReminderDialog: Dialog
    private lateinit var saveReminder: Button
    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var alarmManager: AlarmManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_note, container, false)
        writeTitle = view.findViewById(R.id.write_edit_title)
        writeContent = view.findViewById(R.id.write_content)
        goToHome = view.findViewById(R.id.back_button)
        saveNoteFabButton = view.findViewById(R.id.save_note_fab_button)
        openReminder = view.findViewById(R.id.reminder_add)
        showReminderDialog = Dialog(requireContext())
        showReminderDialog.setContentView(R.layout.remainder_layout)
        saveReminder = showReminderDialog.findViewById(R.id.reminder_Save)
        datePicker = showReminderDialog.findViewById(R.id.datePicker)
        timePicker = showReminderDialog.findViewById(R.id.timePicker)

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService())) [NoteViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goToHome.setOnClickListener { goToHome() }
        saveNoteFabButton.setOnClickListener { createNote() }
        openReminder.setOnClickListener { showReminderDialog.show() }
        saveReminder.setOnClickListener {
            createNote()
            setReminder()
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun setReminder() {
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val title = writeTitle.text.toString()
        val message = writeContent.text.toString()
        intent.putExtra(AlarmReceiver.TITLE, title)
        intent.putExtra(AlarmReceiver.MESSAGE, message)
        val time = getTime()
        val pendingIntent =  PendingIntent.getBroadcast(
            requireContext(),
            Notification.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, time,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
        Toast.makeText(requireContext(), "Reminder is added", Toast.LENGTH_SHORT).show()
    }

    private fun getTime(): Long {
        val minute = timePicker.minute
        val hour = timePicker.hour
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun createNote() {
        val title = writeTitle.text.toString()
        val content = writeContent.text.toString()
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(requireContext(), "Both Fields are required", Toast.LENGTH_SHORT).show()
        } else {
            val notes = Notes(title = title, content = content)
            noteViewModel.addNotes(notes, requireContext())
            noteViewModel.saveNoteStatus.observe(viewLifecycleOwner, Observer {
                if (it.status) {
                    Toast.makeText(requireContext(), "NoteAdded ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error in adding notes ", Toast.LENGTH_SHORT).show()
                }
            })
            home()
        }
    }

    private fun home() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }

    private fun goToHome() {
        home()
    }

}
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


class EditNoteFragment : Fragment() {
    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText
    private lateinit var goToHome: ImageButton
    private lateinit var editNoteFabButton: FloatingActionButton
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var notes: Notes
    private lateinit var id: String
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
        val view = inflater.inflate(R.layout.fragment_edit_note, container, false)
        editTitle = view.findViewById(R.id.edit_title)
        editContent = view.findViewById(R.id.edit_content)
        goToHome = view.findViewById(R.id.button_back)
        editNoteFabButton = view.findViewById(R.id.edit_note_fab_button)
        openReminder = view.findViewById(R.id.add_reminder)
        showReminderDialog = Dialog(requireContext())
        showReminderDialog.setContentView(R.layout.remainder_layout)
        saveReminder = showReminderDialog.findViewById(R.id.reminder_Save)
        datePicker = showReminderDialog.findViewById(R.id.datePicker)
        timePicker = showReminderDialog.findViewById(R.id.timePicker)
        val data = this.arguments
        notes = data?.getSerializable("note") as Notes
        editTitle.setText(notes.title)
        editContent.setText(notes.content)
        id = notes.noteId
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService())) [NoteViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goToHome.setOnClickListener { goToHome() }
        editNoteFabButton.setOnClickListener { editNoteData() }
        openReminder.setOnClickListener { showReminderDialog.show() }
        saveReminder.setOnClickListener {
            editNoteData()
            setReminder()
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
     fun setReminder() {
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val title = notes.title
        val message = notes.content
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

    private fun editNoteData() {
        val title = editTitle.text.toString()
        val content = editContent.text.toString()
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(requireContext(), "Both Fields are required", Toast.LENGTH_SHORT).show()
        } else {
            val notes = Notes(title = title, content = content, noteId = id)
            noteViewModel.editNotes(notes, requireContext())
            noteViewModel.editNoteStatus.observe(viewLifecycleOwner, Observer {
                if (it.status) {
                    Toast.makeText(requireContext(), "NoteEdited ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error in adding notes ", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }

}
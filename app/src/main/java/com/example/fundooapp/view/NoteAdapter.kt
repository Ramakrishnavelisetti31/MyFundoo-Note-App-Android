package com.example.fundooapp.view
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.R
import com.example.fundooapp.model.Notes
import com.example.fundooapp.service.NoteService
import kotlin.random.Random


class NoteAdapter(private val context: Context,
                  private var noteList: ArrayList<Notes>): RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {

    private lateinit var noteService: NoteService
    private lateinit var bundle: Bundle
    private lateinit var archiveNoteFragment: ArchiveNoteFragment
    private lateinit var viewNotesFragment: ViewNotesFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notes_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteAdapter.MyViewHolder, position: Int) {
        val notes: Notes = noteList[position]
        holder.title.text = notes.title
        holder.content.text = notes.content
        holder.id = notes.noteId
        holder.content.setBackgroundColor(holder.itemView.resources.getColor(getRandomColor(), null))
        onClickMenu(notes, holder, context, position)
        onCardClick(notes, holder)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onClickMenu(notes: Notes, holder: MyViewHolder, context: Context, position: Int) {
        holder.noteMenuButton.setOnClickListener {
            val popupMenu = PopupMenu(context, holder.noteMenuButton)
            popupMenu.menuInflater.inflate(R.menu.note_menu, popupMenu.menu)
            popupMenu.gravity = Gravity.END
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                archiveNoteFragment = ArchiveNoteFragment()
                viewNotesFragment = ViewNotesFragment()
                when(it.itemId) {
                    R.id.archive_note -> {
                        if (notes.archive) {

                        }
                        val aNote = noteList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, noteList.size)
                        archiveNoteFragment.getArchive(aNote)
                    }
                    R.id.delete_note -> {
                        val removeNote = noteList.removeAt(position)
                        deleteNotes(removeNote)
                    }
                }
                return@OnMenuItemClickListener true
            })
            popupMenu.show()
        }
    }

    private fun onCardClick(notes: Notes, holder: MyViewHolder) {
        holder.cardNote.setOnClickListener {
            bundle = Bundle()
            bundle.putSerializable("note", notes)
            val editNoteFragment = EditNoteFragment()
            editNoteFragment.arguments = bundle
        val activity = it.context as AppCompatActivity
        activity.supportFragmentManager.beginTransaction().add(R.id.write_note_fragment, editNoteFragment).addToBackStack(null).commit()
        }
    }

    private fun getRandomColor(): Int {
        val colorCode = arrayListOf<Int>()
        colorCode.add(R.color.blue)
        colorCode.add(R.color.yellow)
        colorCode.add(R.color.colorPrimary)
        colorCode.add(R.color.notGreen)
        colorCode.add(R.color.greenLight)
        colorCode.add(R.color.red)
        colorCode.add(R.color.gray)
        colorCode.add(R.color.skyBlue)
        colorCode.add(R.color.lightPurple)
        colorCode.add(R.color.pink)
        colorCode.add(R.color.lightGreen)
        colorCode.add(R.color.backGround)

        val random = Random
        val number: Int = random.nextInt(colorCode.size)
        return colorCode[number]
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun setListData(data: ArrayList<Notes>) {
        noteList = data
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteNotes(notes: Notes) {
        noteService = NoteService()
       noteService.deleteNotesFromFirestore(notes.noteId, context)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var id: String = ""
        val title: TextView = itemView.findViewById(R.id.note_title)
        val content: TextView = itemView.findViewById(R.id.note_content)
        val noteMenuButton: ImageButton = itemView.findViewById(R.id.note_menu_button)
        val cardNote: CardView = itemView.findViewById(R.id.note_card)
    }

}
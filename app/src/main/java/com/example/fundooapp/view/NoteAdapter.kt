package com.example.fundooapp.view
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.R
import com.example.fundooapp.model.Notes
import com.example.fundooapp.service.NoteService
import java.util.*
import kotlin.random.Random


class NoteAdapter(private val context: Context,
                  private var noteList: ArrayList<Notes>): RecyclerView.Adapter<NoteAdapter.MyViewHolder>(), Filterable {

    private lateinit var noteService: NoteService
    private var bundle = Bundle()
    private var allNotes = arrayListOf<Notes>().apply {
        addAll(noteList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notes_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val notes: Notes = allNotes[position]
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
                when(it.itemId) {
                    R.id.archive_note -> {
                        if (notes.archive) {
                            val aNote = allNotes.removeAt(position)
                        }
                    }
                    R.id.delete_note -> {
                        val removeNote = allNotes.removeAt(position)
                        deleteNotes(removeNote)
                    }
                }
                return@OnMenuItemClickListener true
            })
            popupMenu.show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteNotes(notes: Notes) {
        noteService = NoteService()
        noteService.deleteNotesFromFirestore(notes.noteId, context)
        notifyDataSetChanged()
    }

    private fun onCardClick(notes: Notes, holder: MyViewHolder) {
        holder.cardNote.setOnClickListener {
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
        return allNotes.size
    }

    fun setListData(data: ArrayList<Notes>) {
        allNotes = data
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var id: String = ""
        val title: TextView = itemView.findViewById(R.id.note_title)
        val content: TextView = itemView.findViewById(R.id.note_content)
        val noteMenuButton: ImageButton = itemView.findViewById(R.id.note_menu_button)
        val cardNote: CardView = itemView.findViewById(R.id.note_card)
    }

    override fun getFilter(): Filter {
        return myFilter
    }

    var myFilter: Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults? {
            val filteredList: ArrayList<Notes> = arrayListOf()
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(allNotes)
            } else {
               for (notes in allNotes) {
                   if (notes.title.lowercase(Locale.ROOT).contains(charSequence.toString()
                           .lowercase(Locale.getDefault()))) {
                       filteredList.add(notes)
                   }
               }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }
        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
            allNotes.clear()
            allNotes.addAll(filterResults.values as Collection<Notes>)
            notifyDataSetChanged()
        }
    }
}
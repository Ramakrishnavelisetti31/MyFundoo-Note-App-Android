package com.example.fundooapp.view
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.R
import com.example.fundooapp.model.Notes


class MyAdapter(private val context: Context): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var noteList = mutableListOf<Notes>()
    fun setListData(data: MutableList<Notes>) {
        noteList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notes_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val notes: Notes = noteList[position]
        holder.title.text = notes.title
        holder.content.text = notes.content
        viewNoteMenu(context, holder)

        holder.cardNote.setOnClickListener {

        }
    }

    private fun viewNoteMenu(context: Context, holder: MyViewHolder) {
        holder.noteMenuButton.setOnClickListener {
            val popMenu = PopupMenu(context, holder.noteMenuButton)
            popMenu.menuInflater.inflate(R.menu.note_menu, popMenu.menu)
            popMenu.gravity = Gravity.END
        }
    }

    override fun getItemCount(): Int {
       return noteList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.note_title)
        val content: TextView = itemView.findViewById(R.id.note_content)
        val noteMenuButton: ImageButton = itemView.findViewById(R.id.note_menu_button)
        val cardNote: CardView = itemView.findViewById(R.id.note_card)
    }

}
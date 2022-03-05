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
import kotlin.random.Random

class ArchiveAdapter(private val context: Context,
                     private var archiveList: ArrayList<Notes>): RecyclerView.Adapter<ArchiveAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notes_layout, parent, false)
        return ArchiveAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArchiveAdapter.MyViewHolder, position: Int) {
        val notes: Notes = archiveList[position]
        holder.title.text = notes.title
        holder.content.text = notes.content
        holder.id = notes.noteId
        holder.content.setBackgroundColor(holder.itemView.resources.getColor(getRandomColor(), null))
        onClickMenu(notes, holder, context, position)
    }

    private fun onClickMenu(notes: Notes, holder: ArchiveAdapter.MyViewHolder, context: Context, position: Int) {
        holder.noteMenuButton.setOnClickListener {
            val popupMenu = PopupMenu(context, holder.noteMenuButton)
            popupMenu.menuInflater.inflate(R.menu.archive_menu, popupMenu.menu)
            popupMenu.gravity = Gravity.END
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.archive_note -> {
                        if (notes.archive) {

                        }
                    }
                }
                return@OnMenuItemClickListener true
            })
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return archiveList.size
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

    fun setArchiveList(data: ArrayList<Notes>) {
        archiveList = data
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var id: String = ""
        val title: TextView = itemView.findViewById(R.id.note_title)
        val content: TextView = itemView.findViewById(R.id.note_content)
        val noteMenuButton: ImageButton = itemView.findViewById(R.id.note_menu_button)
        val cardNote: CardView = itemView.findViewById(R.id.note_card)
    }

}
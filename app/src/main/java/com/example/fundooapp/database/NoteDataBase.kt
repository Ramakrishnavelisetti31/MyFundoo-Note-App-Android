package com.example.fundooapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.fundooapp.model.Notes

class NoteDataBase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val db: SQLiteDatabase = this.writableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE $TABLE_NAME (" +
                "$TITLE TEXT," +
                "$CONTENT TEXT," +
                "$FIRE_STORE_NOTE_ID TEXT," +
                "$FIRE_STORE_USER_ID TEXT)")
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun saveData(notes: Notes) {
        val values = ContentValues()
        values.put(TITLE, notes.title)
        values.put(CONTENT, notes.content)
        values.put(FIRE_STORE_NOTE_ID, notes.noteId)
        values.put(FIRE_STORE_USER_ID, notes.userId)
        db.insert(TABLE_NAME, null, values)
//        db.close()
    }

    fun getData(): Cursor? {
        Log.d("NotedataBase", "data is ${db.rawQuery("SELECT * FROM $TABLE_NAME", null)}")
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun upDateData(notes: Notes) {
        val values = ContentValues()
        values.put(TITLE, notes.title)
        values.put(CONTENT, notes.content)
        db.update(TABLE_NAME, values, "$FIRE_STORE_NOTE_ID = ?", arrayOf(notes.noteId))
    }

    fun deleteData(id: String) {
            db.delete(TABLE_NAME, "$FIRE_STORE_NOTE_ID = ?", arrayOf(id))
    }

    companion object {
        const val DATABASE_NAME = "NoteDataSql"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "Note"
        const val FIRE_STORE_USER_ID = "user_id"
        const val FIRE_STORE_NOTE_ID = "note_id"
        const val TITLE = "title"
        const val CONTENT = "content"
        const val ONLINE = "online"
    }
}
package com.litovkin.notesapp.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.litovkin.notesapp.domain.entity.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDatabaseDao(): NoteDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: NoteDatabase? = null
        private const val DB_NAME = "NoteDatabase"

        fun getInstance(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context = context,
                    klass = NoteDatabase::class.java,
                    name = DB_NAME
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
package com.litovkin.notesapp.data.repository

import com.litovkin.notesapp.data.data_source.NoteDatabaseDao
import com.litovkin.notesapp.domain.entity.Note
import com.litovkin.notesapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDatabaseDao
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
      return  noteDao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        return noteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        return  noteDao.deleteNote(note)
    }
}
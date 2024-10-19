package com.litovkin.notesapp.domain.usecase

import com.litovkin.notesapp.domain.entity.InvalidNoteException
import com.litovkin.notesapp.domain.entity.Note
import com.litovkin.notesapp.domain.repository.NoteRepository
import javax.inject.Inject
import kotlin.jvm.Throws

class InsertNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("Название заметки не может быть пустым")
        }
        if (note.content.isBlank()) {
            throw InvalidNoteException("Текст заметки не может быть пустым")
        }
        repository.insertNote(note)
    }
}
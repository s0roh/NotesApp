package com.litovkin.notesapp.domain.usecase

import com.litovkin.notesapp.domain.entity.Note
import com.litovkin.notesapp.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int): Note? = repository.getNoteById(id)
}
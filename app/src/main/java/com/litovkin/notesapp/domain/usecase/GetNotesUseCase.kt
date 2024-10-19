package com.litovkin.notesapp.domain.usecase

import com.litovkin.notesapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    operator fun invoke() = repository.getNotes().map { notes ->
        notes.reversed()
    }
}
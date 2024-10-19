package com.litovkin.notesapp.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litovkin.notesapp.domain.entity.Note
import com.litovkin.notesapp.domain.usecase.DeleteNoteUseCase
import com.litovkin.notesapp.domain.usecase.GetNotesUseCase
import com.litovkin.notesapp.domain.usecase.InsertNoteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val insertNoteUseCase: InsertNoteUseCase
) : ViewModel() {

    private var recentlyDeletedNote: Note? = null

    val screenState: StateFlow<NotesScreenState> = getNotesUseCase()
        .map { NotesScreenState(notes = it) }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            NotesScreenState(emptyList())
        )

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase(note)
            recentlyDeletedNote = note
        }
    }

    fun restoreNote() {
        viewModelScope.launch {
            insertNoteUseCase(recentlyDeletedNote ?: return@launch)
            recentlyDeletedNote = null
        }
    }
}
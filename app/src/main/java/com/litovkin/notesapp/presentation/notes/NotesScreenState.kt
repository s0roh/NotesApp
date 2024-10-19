package com.litovkin.notesapp.presentation.notes

import com.litovkin.notesapp.domain.entity.Note

data class NotesScreenState(
    val notes: List<Note>,
){
    val hasNotes: Boolean
        get() = notes.isNotEmpty()
}
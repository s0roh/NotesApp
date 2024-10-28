package com.litovkin.notesapp.presentation.add_edit_note

import android.net.Uri

data class AddEditNoteScreenState(
    val title: String = "",
    val content: String = "",
    val imageUri: Uri? = Uri.EMPTY,
    val showDiscardDialog: Boolean = false
)

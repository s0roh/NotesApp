package com.litovkin.notesapp.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey val id: Int? = null,
    val title: String = "",
    val content: String = "",
    val imageUri: String? = null
)

class InvalidNoteException(message: String) : Exception(message)

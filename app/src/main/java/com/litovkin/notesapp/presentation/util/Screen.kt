package com.litovkin.notesapp.presentation.util

sealed class Screen(val route: String) {
    data object NotesScreen : Screen(ROUTE_NOTES)

    data object AddEditNoteScreen : Screen(ROUTE_ADD_EDIT_NOTES) {
        private const val ROUTE_FOR_ARGS = "add_edit_note_screen"

        fun getRouteWithArgs(noteId: Int): String {
            return "$ROUTE_FOR_ARGS/$noteId"
        }
    }

    companion object {

        const val KEY_NOTE_ID = "note_id"

        const val ROUTE_NOTES = "notes_screen"
        const val ROUTE_ADD_EDIT_NOTES = "add_edit_note_screen/{$KEY_NOTE_ID}"
    }
}
package com.litovkin.notesapp.presentation.add_edit_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litovkin.notesapp.domain.entity.InvalidNoteException
import com.litovkin.notesapp.domain.entity.Note
import com.litovkin.notesapp.domain.usecase.GetNoteByIdUseCase
import com.litovkin.notesapp.domain.usecase.InsertNoteUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class AddEditNoteViewModel @Inject constructor(
    private val noteId: Int,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(AddEditNoteScreenState())
    val screenState = _screenState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        if (noteId != -1) {
            viewModelScope.launch {
                getNoteByIdUseCase(noteId)?.also { note ->
                    currentNoteId = note.id
                    _screenState.value = _screenState.value.copy(
                        title = note.title,
                        content = note.content
                    )
                }
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _screenState.value = _screenState.value.copy(title = newTitle)
    }

    fun onContentChange(newContent: String) {
        _screenState.value = _screenState.value.copy(content = newContent)
    }

    fun saveNote() {
        viewModelScope.launch {
            try {
                insertNoteUseCase(
                    Note(
                        id = currentNoteId,
                        title = _screenState.value.title,
                        content = _screenState.value.content
                    )
                )
                _eventFlow.emit(UiEvent.SaveNote)
            } catch (e: InvalidNoteException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        message = e.message ?: "Не возможно сохранить заметку"
                    )
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}
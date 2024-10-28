package com.litovkin.notesapp.presentation.add_edit_note

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
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
import java.io.File
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
                        content = note.content,
                        imageUri = note.imageUri?.let { Uri.parse(it) } ?: Uri.EMPTY
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

    fun onImageUriChange(newImageUri: Uri?) {
        _screenState.value = _screenState.value.copy(imageUri = newImageUri ?: Uri.EMPTY)
    }

    fun onDiscardDialogDismiss() {
        _screenState.value = _screenState.value.copy(showDiscardDialog = false)
    }

    fun onDiscardDialogShow() {
        _screenState.value = _screenState.value.copy(showDiscardDialog = true)
    }

    fun saveNote() {
        viewModelScope.launch {
            try {
                insertNoteUseCase(
                    Note(
                        id = currentNoteId,
                        title = _screenState.value.title,
                        content = _screenState.value.content,
                        imageUri = _screenState.value.imageUri.toString()
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

     fun launchCamera(
        context: Context,
        cameraLauncher: ActivityResultLauncher<Uri>,
        onImageUriCreated: (Uri) -> Unit
    ) {
        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempFile
        )
        onImageUriCreated(uri)
        cameraLauncher.launch(uri)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}
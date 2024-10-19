package com.litovkin.notesapp.presentation.add_edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.litovkin.notesapp.R
import com.litovkin.notesapp.getApplicationComponent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteContent(
    navController: NavController,
    noteId: Int
) {
    val component = getApplicationComponent()
        .getAddEditNoteComponentFactory()
        .create(noteId)
    val viewModel: AddEditNoteViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val showDiscardDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.popBackStack()
                }

                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    if (showDiscardDialog.value) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog.value = false },
            title = { Text(stringResource(R.string.alert_dialog_title)) },
            text = { Text(stringResource(R.string.alert_dialog_text)) },
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onSecondary,
            textContentColor = MaterialTheme.colorScheme.onSecondary,
            confirmButton = {
                Button(
                    onClick = {
                        showDiscardDialog.value = false
                        navController.popBackStack()
                    }
                ) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(onClick = { showDiscardDialog.value = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    viewModel.saveNote()
                }
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary,
                    actionColor = MaterialTheme.colorScheme.secondary
                )
            }
        },
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                ),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (screenState.value.title.isBlank() || screenState.value.content.isBlank()) {
                                showDiscardDialog.value = true
                            } else {
                                viewModel.saveNote()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            OutlinedTextField(
                value = screenState.value.title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text(text = stringResource(R.string.title)) },
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    selectionColors = TextSelectionColors(
                        handleColor = MaterialTheme.colorScheme.secondary,
                        backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                    )
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = screenState.value.content, //
                onValueChange = { viewModel.onContentChange(it) },
                label = { Text(text = stringResource(R.string.text)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    selectionColors = TextSelectionColors(
                        handleColor = MaterialTheme.colorScheme.secondary,
                        backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                    )
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}
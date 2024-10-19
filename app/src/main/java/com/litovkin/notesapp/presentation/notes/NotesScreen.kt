package com.litovkin.notesapp.presentation.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.litovkin.notesapp.R
import com.litovkin.notesapp.domain.entity.InvalidNoteException
import com.litovkin.notesapp.getApplicationComponent
import com.litovkin.notesapp.presentation.notes.components.NoteIconWithText
import com.litovkin.notesapp.presentation.notes.components.NoteItem
import com.litovkin.notesapp.presentation.notes.components.NoteSearchBar
import com.litovkin.notesapp.presentation.util.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController
) {
    val component = getApplicationComponent()
    val viewModel: NotesViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState()
    val scope = rememberCoroutineScope()

    val textFieldState = remember { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    NoteSearchBar(
                        query = textFieldState.value,
                        onQueryChange = { newQuery -> textFieldState.value = newQuery },
                        onClearQuery = { textFieldState.value = "" }
                    )
                },
                scrollBehavior = scrollBehavior
            )

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
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    navController.navigate(Screen.AddEditNoteScreen.getRouteWithArgs(-1))
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        }
    ) { padding ->
        val currentState = screenState.value

        when (currentState.hasNotes) {
            true -> {
                val filteredNotes = currentState.notes.filter {
                    it.title.contains(textFieldState.value, ignoreCase = true)
                }

                if (filteredNotes.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 92.dp
                        )
                    ) {
                        items(
                            items = filteredNotes,
                            key = { it.id ?: throw InvalidNoteException("Note ID cannot be null") }
                        ) { note ->
                            val dismissState =
                                rememberSwipeToDismissBoxState(
                                    positionalThreshold = { it * 0.7f },
                                    confirmValueChange = {
                                        if (it == SwipeToDismissBoxValue.EndToStart) {
                                            viewModel.deleteNote(note)
                                            scope.launch {
                                                snackbarHostState.currentSnackbarData?.dismiss()
                                                val result = snackbarHostState.showSnackbar(
                                                    message = "Заметка удалена.",
                                                    actionLabel = "Вернуть",
                                                    duration = SnackbarDuration.Short
                                                )
                                                if (result == SnackbarResult.ActionPerformed) {
                                                    viewModel.restoreNote()
                                                }
                                            }
                                        }
                                        true
                                    }
                                )
                            SwipeToDismissBox(
                                modifier = Modifier.animateItem(),
                                state = dismissState,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(end = 8.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Text(
                                            text = stringResource(R.string.delete),
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            ) {
                                NoteItem(
                                    note = note,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            note.id?.let {
                                                navController.navigate(
                                                    Screen.AddEditNoteScreen.getRouteWithArgs(
                                                        note.id
                                                    )
                                                )
                                            }
                                        }
                                )
                            }
                        }
                    }
                } else {
                    NoteIconWithText(
                        icon = Icons.Default.Search,
                        iconDescription = "Search Icon",
                        text = stringResource(R.string.nothing_found)
                    )
                }
            }

            false -> {
                NoteIconWithText(
                    icon = Icons.AutoMirrored.Filled.Note,
                    iconDescription = "Empty Notes Icon",
                    text = stringResource(R.string.place_for_notes)
                )
            }
        }
    }
}

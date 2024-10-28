package com.litovkin.notesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.litovkin.notesapp.presentation.add_edit_note.AddEditNoteContent
import com.litovkin.notesapp.presentation.notes.NotesScreen
import com.litovkin.notesapp.presentation.ui.theme.NotesAppTheme
import com.litovkin.notesapp.presentation.util.Screen
import com.litovkin.notesapp.presentation.util.Screen.Companion.KEY_NOTE_ID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditNoteScreen.route,
                            arguments = listOf(
                                navArgument(name = KEY_NOTE_ID) {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            val noteId = it.arguments?.getInt(KEY_NOTE_ID)
                            AddEditNoteContent(
                                navController = navController,
                                noteId = noteId ?: -1
                            )
                        }
                    }
                }
            }
        }
    }
}
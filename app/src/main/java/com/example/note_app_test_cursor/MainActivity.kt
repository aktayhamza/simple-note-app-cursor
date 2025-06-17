package com.example.note_app_test_cursor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.note_app_test_cursor.data.NoteDatabase
import com.example.note_app_test_cursor.ui.NoteViewModel
import com.example.note_app_test_cursor.ui.NoteViewModelFactory
import com.example.note_app_test_cursor.ui.screens.*
import com.example.note_app_test_cursor.ui.theme.NoteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "note_database"
        ).build()

        setContent {
            NoteAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showSplash by remember { mutableStateOf(true) }
                    
                    if (showSplash) {
                        SplashScreen(onSplashFinished = { showSplash = false })
                    } else {
                        val navController = rememberNavController()
                        val viewModel: NoteViewModel = viewModel(
                            factory = NoteViewModelFactory(database.noteDao())
                        )

                        NavHost(
                            navController = navController,
                            startDestination = "notes"
                        ) {
                            composable("notes") {
                                NotesScreen(
                                    notes = viewModel.allNotes.collectAsState().value,
                                    onNoteClick = { note ->
                                        navController.navigate("edit/${note.id}")
                                    },
                                    onAddClick = {
                                        navController.navigate("edit")
                                    },
                                    onDeleteClick = { note ->
                                        viewModel.deleteNote(note)
                                    },
                                    onFavoriteClick = { note ->
                                        viewModel.toggleFavorite(note)
                                    },
                                    navController = navController
                                )
                            }

                            composable("favorites") {
                                NotesScreen(
                                    notes = viewModel.favoriteNotes.collectAsState().value,
                                    onNoteClick = { note ->
                                        navController.navigate("edit/${note.id}")
                                    },
                                    onAddClick = {
                                        navController.navigate("edit")
                                    },
                                    onDeleteClick = { note ->
                                        viewModel.deleteNote(note)
                                    },
                                    onFavoriteClick = { note ->
                                        viewModel.toggleFavorite(note)
                                    },
                                    navController = navController
                                )
                            }

                            composable(
                                route = "edit/{noteId}",
                                arguments = listOf(
                                    navArgument("noteId") {
                                        type = NavType.IntType
                                    }
                                )
                            ) { backStackEntry ->
                                val noteId = backStackEntry.arguments?.getInt("noteId")
                                val note = viewModel.allNotes.collectAsState().value.find { it.id == noteId }
                                NoteEditScreen(
                                    note = note,
                                    onSaveClick = { title, content ->
                                        if (note != null) {
                                            viewModel.updateNote(note.copy(title = title, content = content))
                                        } else {
                                            viewModel.addNote(title, content)
                                        }
                                        navController.popBackStack()
                                    },
                                    onBackClick = {
                                        navController.popBackStack()
                                    }
                                )
                            }

                            composable("edit") {
                                NoteEditScreen(
                                    onSaveClick = { title, content ->
                                        viewModel.addNote(title, content)
                                        navController.popBackStack()
                                    },
                                    onBackClick = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
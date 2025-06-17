package com.example.note_app_test_cursor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.note_app_test_cursor.data.Note
import com.example.note_app_test_cursor.ui.components.BottomNavBar
import com.example.note_app_test_cursor.ui.components.NoteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: (Note) -> Unit,
    onFavoriteClick: (Note) -> Unit,
    navController: NavController,
    isFavoritesScreen: Boolean = false
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Not Ekle")
            }
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isFavoritesScreen) "Favorilenmiş bir notunuz bulunmamaktadır" else "Henüz notunuz yok",
                        style = MaterialTheme.typography.titleLarge
                    )
                    if (!isFavoritesScreen) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onAddClick) {
                            Text("Not Ekle")
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(notes) { note ->
                    NoteCard(
                        note = note,
                        onNoteClick = onNoteClick,
                        onDeleteClick = onDeleteClick,
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }
        }
    }
} 
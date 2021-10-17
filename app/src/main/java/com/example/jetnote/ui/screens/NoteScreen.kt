package com.example.jetnote.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetnote.domain.model.ColorModel
import com.example.jetnote.domain.model.NoteModel
import com.example.jetnote.routing.Screen
import com.example.jetnote.ui.components.AppDrawer
import com.example.jetnote.ui.components.Note
import com.example.jetnote.ui.components.TopAppBar
import com.example.jetnote.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
private fun NotesList(
    notes: List<NoteModel>,
    onNoteCheckedChange: (NoteModel) -> Unit,
    onNoteClick: (NoteModel) -> Unit
) {
    LazyColumn() {
        items(count = notes.size) { noteIndex ->
            val note = notes[noteIndex]
            Note(
                note = note,
                onNoteCheckedChange = onNoteCheckedChange,
                onNoteClick = onNoteClick,
            )
        }
    }
}

@Composable
fun NotesScreen(viewModel: MainViewModel) {
    val notes: List<NoteModel> by viewModel
        .notesNotInTrash
        .observeAsState(listOf())

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "JetNotes",
                icon = Icons.Filled.List,
                onIconClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.Notes,
                closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        content = {
//            NotesList(
//                notes = listOf(
//                    NoteModel(1, "Note1", "Content 1", null, ColorModel(1, "pink", "#eba5ab")),
//                    NoteModel(2, "Note2", "Content 2", false, ColorModel(1, "Violet", "#aea4c7")),
//                    NoteModel(3, "Note3", "Content 3", true, ColorModel(3, "Cyan", "#2acaea"))
//                ),
//                onNoteCheckedChange = {},
//                onNoteClick = {},
//            )
            if (notes.isNotEmpty()) {
                NotesList(
                    notes = notes,
                    onNoteCheckedChange = { viewModel.onNoteCheckedChange(it) },
                    onNoteClick = { viewModel.onNoteClick(it) }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewNoteClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Create a new note button"
                    )
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun NotesListPreview() {
    NotesList(
        notes = listOf(
            NoteModel(1, "Note1", "Content 1", null, ColorModel(1, "pink", "#eba5ab")),
            NoteModel(2, "Note2", "Content 2", false, ColorModel(1, "Violet", "#aea4c7")),
            NoteModel(3, "Note3", "Content 3", true, ColorModel(3, "Cyan", "#2acaea"))
        ),
        onNoteCheckedChange = {},
        onNoteClick = {}
    )
}
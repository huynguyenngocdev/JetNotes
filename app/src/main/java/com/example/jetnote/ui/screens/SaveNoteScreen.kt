package com.example.jetnote.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetnote.R
import com.example.jetnote.domain.model.ColorModel
import com.example.jetnote.domain.model.NEW_NOTE_ID
import com.example.jetnote.domain.model.NoteModel
import com.example.jetnote.routing.JetNotesRouter
import com.example.jetnote.routing.Screen
import com.example.jetnote.ui.components.NoteColor
import com.example.jetnote.util.fromHex
import com.example.jetnote.viewmodel.MainViewModel
import kotlinx.coroutines.launch

//@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColorPicker(
    colors: List<ColorModel>,
    onColorSelect: (ColorModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Color picker",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
//            stickyHeader(
//                content = {
//                    Text(
//                        text = "Color picker",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(8.dp)
//                    )
//                }
//            )

            items(colors.size) { itemIndex ->
                val color = colors[itemIndex]
                ColorItem(
                    color = color,
                    onColorSelect = onColorSelect
                )
            }
        }
    }
}

@Composable
private fun PickedColor(color: ColorModel) {
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Picked color",
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        NoteColor(
            color = Color.fromHex(color.hex),
            size = 40.dp,
            border = 1.dp,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun ColorItem(
    color: ColorModel,
    onColorSelect: (ColorModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onColorSelect(color) })
    ) {
        NoteColor(
            modifier = Modifier.padding(10.dp),
            color = Color.fromHex(color.hex),
            size = 80.dp,
            border = 2.dp
        )
        Text(
            text = color.name,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun NoteCheckOption(
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Can note checked off?",
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked, onCheckedChange = onCheckChange,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun ContentTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(text = label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        )
    )
}

@Composable
private fun SaveNoteSTopAppBar(
    isEditingMode: Boolean,
    onBackClick: () -> Unit,
    onSaveNoteClick: () -> Unit,
    onOpenColorPickerClick: () -> Unit,
    onDeleteNoteClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Save Note",
                color = MaterialTheme.colors.onPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Save Note Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = onSaveNoteClick) {
                Icon(
                    imageVector = Icons.Default.Check,
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "Save Note"
                )
            }

            //Open color picker action icon
            IconButton(onClick = onOpenColorPickerClick) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_baseline_color_lens_24
                    ),
                    contentDescription = "Open Color Picker Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            // Delete action icon (show only in editing mode)
            if (isEditingMode) {
                IconButton(onClick = onDeleteNoteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete NOte Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
private fun SaveNoteContent(
    note: NoteModel,
    onNoteChange: (NoteModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ContentTextField(
            label = "Title",
            text = note.title,
            onTextChange = { newTitle -> onNoteChange.invoke(note.copy(title = newTitle)) }
        )

        ContentTextField(
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(top = 16.dp),
            label = "Body",
            text = note.content,
            onTextChange = { newContent -> onNoteChange.invoke(note.copy(content = newContent)) }
        )

        val canBeCheckedOff: Boolean = note.isCheckedOff != null

        NoteCheckOption(
            isChecked = canBeCheckedOff,
            onCheckChange = { canBeCheckedOffNewValue ->
                val isCheckedOff: Boolean? = if (canBeCheckedOffNewValue) false else null

                onNoteChange.invoke(note.copy(isCheckedOff = isCheckedOff))
            }
        )

        PickedColor(color = note.color)
    }
}

@ExperimentalMaterialApi
@Composable
fun SaveNoteScreen(viewModel: MainViewModel) {

    val noteEntry: NoteModel by viewModel.noteEntry.observeAsState(NoteModel())
    val colors: List<ColorModel> by viewModel.colors.observeAsState(listOf())
    val bottomDrawerState: BottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val moveNoteToTrashDialogShowState: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler(onBack = {
        if (bottomDrawerState.isOpen) {
            coroutineScope.launch { bottomDrawerState.close() }
        } else {
            JetNotesRouter.navigateTo(Screen.Notes)
        }
    })

    Scaffold(
        topBar = {
            val isEditingMode: Boolean = noteEntry.id != NEW_NOTE_ID
            SaveNoteSTopAppBar(
                isEditingMode = isEditingMode,
                onBackClick = { JetNotesRouter.navigateTo(Screen.Notes) },
                onSaveNoteClick = { viewModel.saveNote(noteEntry) },
                onOpenColorPickerClick = {
                    coroutineScope.launch { bottomDrawerState.open() }
                },
                onDeleteNoteClick = { moveNoteToTrashDialogShowState.value = true })
        },
        content = {
            BottomDrawer(
                drawerState = bottomDrawerState,
                drawerContent = {
                    ColorPicker(colors = colors,
                        onColorSelect = { color ->
                            val newNoteEntry = noteEntry.copy(color = color)
                            viewModel.onNoteEntryChange(newNoteEntry)
                        })
                },
                content = {
                    SaveNoteContent(
                        note = noteEntry,
                        onNoteChange = { updateNoteEntry ->
                            viewModel.onNoteEntryChange(updateNoteEntry)
                        }
                    )
                }
            )

            if (moveNoteToTrashDialogShowState.value) {
                AlertDialog(
                    onDismissRequest = { moveNoteToTrashDialogShowState.value = false },
                    title = {
                        Text(
                            text = "Move note to the trash?"
                        )
                    },
                    text = {
                        Text(text = "Are you sure you want to move this note to trash?")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.moveNoteToTrash(noteEntry)
                        }) {
                            Text(text = "Confirm")
                        }
                    }
                )
            }
        }
    )
}
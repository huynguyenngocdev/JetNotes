package com.example.jetnote.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetnote.domain.model.NoteModel
import com.example.jetnote.util.fromHex

@ExperimentalMaterialApi
@Composable
fun Note(
    note: NoteModel,
    onNoteCheckedChange: (NoteModel) -> Unit = {},
    onNoteClick: (NoteModel) -> Unit = {},
    isSelected: Boolean = false
) {
    val background = if (isSelected) Color.LightGray else MaterialTheme.colors.surface
    val backgroundShape: Shape = RoundedCornerShape(4.dp)

    Row(
        modifier = Modifier
            .padding(8.dp)
            .shadow(1.dp, backgroundShape)
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .background(color = background, shape = backgroundShape)
            .clickable {
                onNoteClick.invoke(note)
            }
    ) {
        NoteColor(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp, end = 16.dp),
            color = Color.fromHex(note.color.hex),
            size = 40.dp,
            border = 2.dp
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = note.title, maxLines = 1,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.15.sp
                )
            )
            Text(
                text = note.content, maxLines = 1,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.25.sp
                )
            )
        }
        if (note.isCheckedOff != null) {
            Checkbox(
                checked = note.isCheckedOff,
                onCheckedChange = { isChecked ->
                    val newNote = note.copy(isCheckedOff = isChecked)
                    onNoteCheckedChange.invoke(newNote)
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),

                )
        }
    }
}
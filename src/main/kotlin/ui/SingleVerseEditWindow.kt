package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogWindow
import entity.bible.Reference

@Composable
fun SingleVerseEditWindow(
    isOpen: Boolean,
    onClose: () -> Unit,
    reference: Reference,
) {
    if (isOpen) DialogWindow(
        onCloseRequest = onClose,
        title = "Editing ${reference.shortRef}",
        resizable = false,
    ) {
        MaterialTheme {
            Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
                var verseContent by remember { mutableStateOf(reference.verse.text) }
                OutlinedTextField(verseContent, onValueChange = { verseContent = it }, modifier = Modifier.weight(1f))

                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton({}) { Text("Save") }
                    TextButton(onClose) { Text("Cancel") }
                }
            }
        }
    }
}
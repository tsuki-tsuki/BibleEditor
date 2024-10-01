package ui

import androidx.compose.foundation.layout.*
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
    onSave: (String) -> Unit,
) {
    if (isOpen) DialogWindow(
        onCloseRequest = onClose,
        title = "Editing ${reference.shortRef}",
        resizable = false,
    ) {
        MaterialTheme {
            Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
                var verseContent by remember(reference) { mutableStateOf(reference.verse.text) }
                OutlinedTextField(
                    verseContent,
                    onValueChange = { verseContent = it },
                    enabled = reference.isValid,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                )

                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(
                        onClick = { onSave(verseContent) },
                        enabled = reference.isValid && verseContent != reference.verse.text,
                    ) { Text("Save") }
                    TextButton(onClose) { Text("Cancel") }
                }
            }
        }
    }
}
package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
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
            Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize().padding(10.dp)) {
                var verseContent by remember(reference) { mutableStateOf(reference.verse.text) }
                OutlinedTextField(
                    verseContent,
                    onValueChange = { verseContent = it },
                    enabled = reference.isValid,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                )

                var keepOpen by remember { mutableStateOf(false) }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 5.dp).clickable(role = Role.Checkbox) { keepOpen = !keepOpen },
                ) {
                    Checkbox(keepOpen, onCheckedChange = null)
                    Text("Keep dialog open after save")
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TextButton(
                        onClick = {
                            onSave(verseContent)
                            if (!keepOpen) onClose()
                        },
                        enabled = reference.isValid && verseContent != reference.verse.text,
                    ) { Text("Save") }
                    TextButton(onClose) { Text("Cancel") }
                }
            }
        }
    }
}
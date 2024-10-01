package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import entity.bible.Bible
import entity.bible.Book
import entity.bible.Chapter
import entity.bible.Verse
import ui.component.Picker

@Preview
@Composable
fun MainWindow(
    // UI control
    onCloseRequest: () -> Unit,
    onSingleEditRequest: (Verse) -> Unit,
    onLoadBibleRequest: () -> Unit,
    onSaveBibleRequest: () -> Unit,

    // Contents
    bibleContent: Bible?,
    selectedBook: Book,
    onSelectBook: (Book) -> Unit,
    selectedChapter: Chapter,
    onSelectChapter: (Chapter) -> Unit,
) {
    Window(onCloseRequest = onCloseRequest, title = "Bible Editor") {
        MaterialTheme {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Button(onClick = onLoadBibleRequest) { Text("Load") }
                        Button(onClick = onSaveBibleRequest, enabled = false) { Text("Save") }
                    }

                    Text(
                        """--Info--
                            |Name: ${bibleContent?.name.orEmpty()}
                            |Copyright: ${bibleContent?.info?.rights.orEmpty()}""".trimMargin(),
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    var bookPicker by remember { mutableStateOf(false) }
                    Picker(
                        expanded = bookPicker,
                        onExpandedChange = { bookPicker = it },
                        label = "Book",
                        items = bibleContent?.books.orEmpty(),
                        selectedItem = selectedBook,
                        onItemSelected = onSelectBook,
                        itemValue = { it.name },
                        modifier = Modifier.weight(.5f),
                    )

                    var chapterPicker by remember { mutableStateOf(false) }
                    Picker(
                        expanded = chapterPicker,
                        onExpandedChange = { chapterPicker = it },
                        label = "Chapter",
                        items = selectedBook.chapters,
                        selectedItem = selectedChapter,
                        onItemSelected = onSelectChapter,
                        itemValue = { it.numberString },
                        modifier = Modifier.weight(.4f),
                    )
                }

                LazyColumn {
                    items(selectedChapter.verses) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.clickable { onSingleEditRequest(it) },
                        ) {
                            Text("${it.number}".padStart(2).padEnd(3), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(it.text)
                        }
                    }
                }
            }
        }
    }
}
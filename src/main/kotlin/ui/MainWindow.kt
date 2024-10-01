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
    // State
    isUpdated: Boolean,

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
    val title = "Bible Editor".let { if (isUpdated) "$it*" else it }
    Window(onCloseRequest = onCloseRequest, title = title) {
        MaterialTheme {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Button(onClick = onLoadBibleRequest) { Text("Load") }
                        Button(onClick = onSaveBibleRequest, enabled = isUpdated) { Text("Save") }
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
                    val bookList = bibleContent?.books.orEmpty()
                    Picker(
                        expanded = bookList.isNotEmpty() && bookPicker,
                        onExpandedChange = { bookPicker = it },
                        label = "Book",
                        items = bookList,
                        selectedItem = selectedBook,
                        onItemSelected = onSelectBook,
                        itemValue = { it.name },
                        modifier = Modifier.weight(.5f),
                    )

                    var chapterPicker by remember { mutableStateOf(false) }
                    val chapterList = selectedBook.chapters
                    Picker(
                        expanded = chapterList.isNotEmpty() && chapterPicker,
                        onExpandedChange = { chapterPicker = it },
                        label = "Chapter",
                        items = chapterList,
                        selectedItem = selectedChapter,
                        onItemSelected = onSelectChapter,
                        itemValue = { if (it.number > 0) it.number.toString() else "(Chapter)" },
                        modifier = Modifier.weight(.4f),
                    )
                }

                LazyColumn {
                    items(selectedChapter.verses) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.fillMaxWidth().clickable { onSingleEditRequest(it) },
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text("${it.number}".padStart(3), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(it.text)
                        }
                    }
                }
            }
        }
    }
}
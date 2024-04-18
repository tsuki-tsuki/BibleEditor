import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastJoinToString
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import bible.Bible
import bible.Book
import bible.Chapter
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.adaptivity.xmlutil.serialization.XML
import kotlin.io.path.Path
import kotlin.io.path.reader

@Preview
@Composable
fun App() {
    val text by remember { mutableStateOf("Hello, World!") }
    var showPicker by remember { mutableStateOf(false) }
    var chosenFilePath by remember { mutableStateOf("") }
    var bibleContent by remember { mutableStateOf<Bible?>(null) }

    val coroutineScope = rememberCoroutineScope { Dispatchers.IO }

    LaunchedEffect(chosenFilePath) {
        coroutineScope.launch {
            if (chosenFilePath.isNotEmpty()) {
                bibleContent = XML.decodeFromString<Bible>(
                    Path(chosenFilePath).reader()
                        .useLines(Sequence<String>::toList)
                        .fastJoinToString("")
                )
            }
        }
    }

    var bookPicker by remember { mutableStateOf(false) }
    var selectedBook by remember { mutableStateOf<Book?>(null) }

    var chapterPicker by remember { mutableStateOf(false) }
    var selectedChapter by remember { mutableStateOf<Chapter?>(null) }

    MaterialTheme {
        Column {
            Button(onClick = { showPicker = true }) { Text(text) }

            Text("load $chosenFilePath, get ${bibleContent?.name.orEmpty()}")

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.fillMaxWidth(.3f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(selectedBook?.name ?: "(Book)", modifier = Modifier.weight(1f))
                        IconButton(onClick = { bookPicker = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "select book")
                        }
                    }
                    DropdownMenu(expanded = bookPicker, onDismissRequest = { bookPicker = false }) {
                        bibleContent?.books?.forEach {
                            DropdownMenuItem(onClick = {
                                bookPicker = false
                                selectedBook = it
                            }) { Text(it.name) }
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxWidth(.5f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(selectedChapter?.number?.toString() ?: "(Chapter)", modifier = Modifier.weight(1f))
                        IconButton(onClick = { chapterPicker = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "select chapter")
                        }
                    }
                    DropdownMenu(expanded = chapterPicker, onDismissRequest = { chapterPicker = false }) {
                        selectedBook?.chapters?.forEach {
                            DropdownMenuItem(onClick = {
                                chapterPicker = false
                                selectedChapter = it
                            }) { Text(it.number.toString()) }
                        }
                    }
                }
            }

            LazyColumn {
                items(selectedChapter?.verses.orEmpty()) {
                    Text("${it.number}. ${it.text}")
                }
            }
        }

        FilePicker(show = showPicker, fileExtensions = listOf("xml")) { file ->
            showPicker = false
            chosenFilePath = file?.path.orEmpty()
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

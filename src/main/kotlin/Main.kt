import androidx.compose.runtime.*
import androidx.compose.ui.util.fastJoinToString
import androidx.compose.ui.window.application
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import entity.bible.*
import nl.adaptivity.xmlutil.serialization.XML
import ui.MainWindow
import ui.SingleVerseEditWindow
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() = application {
    // UI control
    var showEditWindow by remember { mutableStateOf(false) }
    var showPicker by remember { mutableStateOf(false) }

    // App state
    var chosenFilePath by remember { mutableStateOf("") }
    val bibleContent by remember(chosenFilePath) {
        derivedStateOf {
            runCatching {
                Path(chosenFilePath)
                    .useLines { it.toList() }
                    .fastJoinToString("")
                    .let { XML.decodeFromString<Bible>(it) }
            }.getOrNull()
        }
    }
    var selectedBook by remember(chosenFilePath) { mutableStateOf(Book.Placeholder) }
    var selectedChapter by remember(chosenFilePath, selectedBook) { mutableStateOf(Chapter.Placeholder) }
    var selectedVerse by remember(chosenFilePath, selectedBook, selectedChapter) { mutableStateOf(Verse.Placeholder) }

    // Control dialog
    FilePicker(show = showPicker, fileExtensions = listOf("xml")) { file ->
        showPicker = false
        file?.also { chosenFilePath = it.path }
    }

    // Windows
    MainWindow(
        onCloseRequest = ::exitApplication,
        onSingleEditRequest = {
            showEditWindow = true
            selectedVerse = it
        },
        onLoadBibleRequest = { showPicker = true },
        onSaveBibleRequest = { TODO("save") },
        bibleContent = bibleContent,
        selectedBook = selectedBook,
        onSelectBook = { selectedBook = it },
        selectedChapter = selectedChapter,
        onSelectChapter = { selectedChapter = it }
    )
    SingleVerseEditWindow(
        isOpen = showEditWindow,
        onClose = { showEditWindow = false },
        reference = Reference(selectedBook, selectedChapter, selectedVerse),
    )
}

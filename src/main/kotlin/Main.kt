import androidx.compose.runtime.*
import androidx.compose.ui.window.application
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import entity.bible.*
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.XmlBufferedWriter
import nl.adaptivity.xmlutil.XmlWriter
import nl.adaptivity.xmlutil.serialization.XML
import ui.MainWindow
import ui.SingleVerseEditWindow
import kotlin.io.path.Path
import kotlin.io.path.useLines
import kotlin.io.path.writeText

fun main() = application {
    // App state
    var chosenFilePath by remember { mutableStateOf("") }
    var bibleContent by remember(chosenFilePath) { mutableStateOf<Bible?>(null) }
    var selectedBook by remember(chosenFilePath) { mutableStateOf(Book.Placeholder) }
    var selectedChapter by remember(chosenFilePath) { mutableStateOf(Chapter.Placeholder) }
    var selectedVerse by remember(chosenFilePath) { mutableStateOf(Verse.Placeholder) }
    val currentReference by derivedStateOf { Reference(selectedBook, selectedChapter, selectedVerse) }
    var isUpdated by remember { mutableStateOf(false) }

    // UI control
    var showEditWindow by remember { mutableStateOf(false) }
    var showPicker by remember { mutableStateOf(false) }

    // Load Bible content from file
    LaunchedEffect(chosenFilePath) {
        bibleContent = runCatching {
            Path(chosenFilePath)
                .useLines { it.joinToString("") }
                .let { XML.decodeFromString<Bible>(it) }
        }.getOrNull()
    }

    // Control dialog
    FilePicker(show = showPicker, fileExtensions = listOf("xml")) { file ->
        showPicker = false
        file?.also { chosenFilePath = it.path }
    }

    // Windows
    MainWindow(
        isUpdated = isUpdated,
        onCloseRequest = ::exitApplication,
        onSingleEditRequest = {
            showEditWindow = true
            selectedVerse = it
        },
        onLoadBibleRequest = { showPicker = true },
        onSaveBibleRequest = {
            bibleContent?.also { content ->
                val outputString = XML.encodeToString(content)
                Path(chosenFilePath).writeText(outputString)
                isUpdated = false
            }
        },
        bibleContent = bibleContent,
        selectedBook = selectedBook,
        onSelectBook = {
            selectedBook = it
            selectedChapter = Chapter.Placeholder
            selectedVerse = Verse.Placeholder
        },
        selectedChapter = selectedChapter,
        onSelectChapter = {
            selectedChapter = it
            selectedVerse = Verse.Placeholder
        }
    )
    SingleVerseEditWindow(
        isOpen = showEditWindow,
        onClose = { showEditWindow = false },
        reference = currentReference,
        onSave = { nextText ->
            bibleContent = bibleContent?.updateContent(currentReference, nextText)
            // refresh content
            currentReference.also { (book, chapter, verse) ->
                selectedBook = bibleContent?.books?.find { it.number == book.number } ?: book
                selectedChapter = selectedBook.chapters.find { it.number == chapter.number } ?: chapter
                selectedVerse = selectedChapter.verses.find { it.number == verse.number } ?: verse
            }
            isUpdated = true
        }
    )
}

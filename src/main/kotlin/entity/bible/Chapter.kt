package entity.bible

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("CHAPTER")
data class Chapter(
    @SerialName("cnumber")
    val number: Int,
    @XmlElement(true)
    val verses: List<Verse>,
) {
    companion object {
        val Placeholder = Chapter(0, emptyList())
    }

    fun updateContent(reference: Reference, newContent: String): Chapter {
        val targetVerseId = verses.indexOfFirst { it.number == reference.verse.number }
        val targetVerse = verses[targetVerseId]
        val updatedVerse = targetVerse.updateContent(newContent)
        val updatedVerses = verses.mapIndexed { index, verse ->
            if (index == targetVerseId) updatedVerse else verse
        }
        return copy(verses = updatedVerses)
    }
}

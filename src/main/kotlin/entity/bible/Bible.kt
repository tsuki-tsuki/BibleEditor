package entity.bible

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("XMLBIBLE")
data class Bible(
    @SerialName("biblename")
    val name: String = "",
    private val type: String = "x-bible",
    @XmlSerialName(
        value = "noNamespaceSchemaLocation",
        namespace = "http://www.w3.org/2001/XMLSchema-instance",
        prefix = "xsi",
    )
    private val schemaLocation: String = "zef2005.xsd",
    @XmlElement(true)
    val info: BibleInfo,
    @XmlElement(true)
    val books: List<Book>,
) {
    fun updateContent(reference: Reference, newContent: String): Bible {
        val targetBookId = books.indexOfFirst { it.number == reference.book.number }
        val targetBook = books[targetBookId]
        val updatedBook = targetBook.updateContent(reference, newContent)
        val updatedBooks = books.mapIndexed { index, book ->
            if (index == targetBookId) updatedBook else book
        }
        return copy(books = updatedBooks)
    }
}

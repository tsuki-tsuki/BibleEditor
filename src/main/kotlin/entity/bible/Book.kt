package entity.bible

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("BIBLEBOOK")
data class Book(
    @SerialName("bname")
    val name: String,
    @SerialName("bnumber")
    val number: Int,
    @SerialName("bsname")
    val shortName: String = name.take(3),
    @XmlElement(true)
    val chapters: List<Chapter>,
) {
    companion object {
        val Placeholder = Book("(Book)", 0, chapters = emptyList())
    }
}

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
}

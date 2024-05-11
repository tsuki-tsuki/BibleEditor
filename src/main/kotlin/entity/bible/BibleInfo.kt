package entity.bible

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("INFORMATION")
data class BibleInfo(
    @XmlElement(true)
    val rights: String,
    @XmlElement(true)
    val format: String,
)

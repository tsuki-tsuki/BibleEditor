package bible

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@SerialName("VERS")
data class Verse(
    @SerialName("vnumber")
    val number: Int,
    @XmlValue
    val text: String,
)

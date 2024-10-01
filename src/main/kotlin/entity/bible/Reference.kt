@file:Suppress("unused")
package entity.bible

data class Reference(
    val book: Book,
    val chapter: Chapter,
    val verse: Verse,
) {
    val shortRef: String get() = "${book.shortName} ${chapter.number.format()}:${verse.number.format()}"
    val longRef: String get() = "${book.name} ${chapter.number.format()}:${verse.number.format()}"
    val isValid: Boolean get() = chapter.number > 0 && verse.number > 0
}

private fun Int.format() = takeIf { it > 0 }?.toString() ?: "-"

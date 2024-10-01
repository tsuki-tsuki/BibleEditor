package entity.bible

data class Reference(
    val book: Book,
    val chapter: Chapter,
    val verse: Verse,
) {
    val shortRef: String get() = "${book.shortName} ${chapter.numberString}:${verse.number}"
    val longRef: String get() = "${book.name} ${chapter.numberString}:${verse.number}"
}

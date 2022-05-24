package tambola.extensions

fun <T> List<List<T>>.transpose(): List<List<T>> = when (this) {
    listOf<List<T>>() -> listOf()
    else -> {
        val sizeOfFirstRow = this.first().size
        (0 until sizeOfFirstRow).map { this.map { columnValue -> columnValue[it] } }
    }
}

fun <T, R> List<T>.fillIfNot(predicate: (T) -> Boolean, elementsToFill: List<R>, default: R): List<R> =
    this.fold(Pair(elementsToFill, listOf<R>())) { acc, curr ->
        if (predicate(curr)) Pair(acc.first, acc.second.plus(default))
        else Pair(acc.first.drop(1), acc.second.plus(acc.first.first()))
    }.second
package tambola

typealias Template = List<Boolean>
typealias Elements = List<Int?>
typealias Ticket = List<Elements>

object TicketGenerator {
    fun generateRandomRowTemplate(numberOfColumns: Int = 9, nonEmptyColumns: Int = 5): Template {
        val emptyColumns = numberOfColumns - nonEmptyColumns
        return List(nonEmptyColumns) { true }.plus(List(emptyColumns) { false }).shuffled()
    }

    fun generateRandomThirdRowTemplate(
        first: Template,
        second: Template,
        numberOfColumns: Int = 9,
        nonEmptyColumns: Int = 5
    ): Template {
        val fixedRowTemplates = first.zip(second).map {
            when (it) {
                Pair(false, false) -> true
                else -> false
            }
        }
        val remainingNonEmptyColumns = nonEmptyColumns - fixedRowTemplates.count { it }
        val remainingEmptyColumns = numberOfColumns - nonEmptyColumns

        val remainingRowTemplateValues =
            List(remainingNonEmptyColumns) { true }.plus(List(remainingEmptyColumns) { false }).shuffled()

        return fixedRowTemplates.fold(Pair(remainingRowTemplateValues, listOf<Boolean>())) { acc, curr ->
            when (curr) {
                true -> Pair(acc.first, acc.second.plus(true))
                else -> Pair(acc.first.drop(1), acc.second.plus(acc.first.first()))
            }
        }.second
    }

    fun fillColumnTemplates(
        column: Template,
        range: IntRange
    ): Elements {
        val numberOfElementsToFill = column.count { it }
        val elementsToFill =
            IntRange(1, numberOfElementsToFill).fold(Pair<Int, List<Int>>(range.first, listOf())) { acc, curr ->
                val nextElement: Int = IntRange(
                    acc.first,
                    (range.last - numberOfElementsToFill + curr)
                ).random()

                Pair(nextElement.inc(), acc.second.plus(nextElement))
            }.second

        return column.fold(Pair(elementsToFill, listOf<Int?>())) { acc, curr ->
            when (curr) {
                false -> Pair(acc.first, acc.second.plus(null))
                else -> Pair(acc.first.drop(1), acc.second.plus(acc.first.first()))
            }
        }.second
    }

    fun fillRowTemplates(
        rowTemplates: List<Template>,
        numberOfColumns: Int = 9,
        numberOfRows: Int = 3
    ): Ticket {
        val columnTemplates: List<Template> =
            (0 until numberOfColumns).map { rowTemplates.map { rowTemplate -> rowTemplate[it] } }

        val columnValues = columnTemplates.mapIndexed { index, columnTemplate ->
            fillColumnTemplates(
                columnTemplate,
                IntRange(0 + (10 * index), 9 + (10 * index))
            )
        }

        return (0 until numberOfRows).map { columnValues.map { columnValue -> columnValue[it] } }
    }

    fun generate(
        numberOfColumns: Int = 9,
        nonEmptyColumns: Int = 5
    ): Ticket {
        val firstRowTemplate = generateRandomRowTemplate(numberOfColumns, nonEmptyColumns)
        val secondRowTemplate = generateRandomRowTemplate(numberOfColumns, nonEmptyColumns)
        val thirdRowTemplate =
            generateRandomThirdRowTemplate(firstRowTemplate, secondRowTemplate, numberOfColumns, nonEmptyColumns)

        return fillRowTemplates(listOf(firstRowTemplate, secondRowTemplate, thirdRowTemplate), numberOfColumns, 3)
    }

    fun print(ticket: Ticket) {
        ticket.map { it.map { element -> element?.toString() ?: "__" } }.forEach { println(it) }
    }

}
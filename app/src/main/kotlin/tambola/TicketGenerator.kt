package tambola

import tambola.extensions.fillIfNot
import tambola.extensions.transpose

typealias Template = List<Boolean>
typealias Elements = List<Int?>
typealias Ticket = List<Elements>

object TicketGenerator {
    private val ranges: List<IntRange> = listOf(
        IntRange(1, 9),
        IntRange(10, 19),
        IntRange(20, 29),
        IntRange(30, 39),
        IntRange(40, 49),
        IntRange(50, 59),
        IntRange(60, 69),
        IntRange(70, 79),
        IntRange(80, 90)
    )

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

        return fixedRowTemplates.fillIfNot({ it }, remainingRowTemplateValues, true)
    }

    fun fillColumnTemplates(
        column: Template,
        range: IntRange
    ): Elements {
        val numberOfElementsToFill = column.count { it }
        val elementsToFill = range.shuffled().take(numberOfElementsToFill).sorted()

        return column.fillIfNot({ !it }, elementsToFill, null)
    }

    fun fillRowTemplates(
        rowTemplates: List<Template>
    ): Ticket {
        return rowTemplates.transpose().mapIndexed { index, columnTemplate ->
            fillColumnTemplates(
                columnTemplate,
                ranges[index]
            )
        }.transpose()
    }

    fun generate(
        numberOfColumns: Int = 9,
        nonEmptyColumns: Int = 5
    ): Ticket {
        val firstRowTemplate = generateRandomRowTemplate(numberOfColumns, nonEmptyColumns)
        val secondRowTemplate = generateRandomRowTemplate(numberOfColumns, nonEmptyColumns)
        val thirdRowTemplate =
            generateRandomThirdRowTemplate(firstRowTemplate, secondRowTemplate, numberOfColumns, nonEmptyColumns)

        return fillRowTemplates(listOf(firstRowTemplate, secondRowTemplate, thirdRowTemplate))
    }

    fun print(ticket: Ticket) {
        ticket.map { it.map { element -> element?.toString() ?: "__" } }.forEach { println(it) }
    }
}
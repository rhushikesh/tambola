package tambola

import tambola.extensions.fillIfNot
import tambola.extensions.transpose

sealed interface ElementTemplate
object Fill : ElementTemplate
object Empty : ElementTemplate

typealias Template = List<ElementTemplate>
typealias Element = Int?
typealias Elements = List<Element>
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

    private val validTemplate = listOf(
        listOf(Fill, Fill, Fill, Fill, Fill, Empty, Empty, Empty, Empty),
        listOf(Empty, Empty, Empty, Empty, Fill, Fill, Fill, Fill, Fill),
        listOf(Fill, Fill, Fill, Fill, Fill, Empty, Empty, Empty, Empty)
    )

    fun generateRandomRowTemplate() =
        validTemplate.shuffled().transpose().shuffled().transpose()

    fun fillColumnTemplates(
        column: Template,
        range: IntRange
    ): Elements {
        val numberOfElementsToFill = column.count { it is Fill }
        val elementsToFill = range.shuffled().take(numberOfElementsToFill).sorted()

        return column.fillIfNot({ it is Empty }, elementsToFill, null)
    }

    fun fillRowTemplates(
        rowTemplates: List<Template>
    ): Ticket {
        return rowTemplates
            .transpose()
            .zip(ranges)
            .map { pair -> fillColumnTemplates(pair.first, pair.second) }
            .transpose()
    }

    fun generate(): Ticket {
        return fillRowTemplates(generateRandomRowTemplate())
    }

    fun print(ticket: Ticket) {
        ticket.map { it.map { element -> element?.toString() ?: "__" } }.forEach { println(it) }
    }
}
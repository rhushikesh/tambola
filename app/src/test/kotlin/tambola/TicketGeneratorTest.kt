package tambola

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.checkAll
import tambola.TicketGenerator.generate
import tambola.extensions.transpose

class TicketGeneratorTest : DescribeSpec({
    describe("TicketGenerator") {
        val ticketArb = arbitrary {
            generate()
        }
        context("generate ticket") {
            checkAll(100, ticketArb) { ticket ->
                val transposedTicket = ticket.transpose()
                it("should generate ticket with 3 rows") {
                    ticket.size shouldBe 3
                }
                it("should generate ticket with 9 columns") {
                    transposedTicket.size shouldBe 9
                }
                it("15 of the cells should have unique numbers ranging from 1-90") {
                    val cellsWithValues = ticket.flatten().filterNotNull().toSet()

                    cellsWithValues.size shouldBe 15
                    cellsWithValues.all { IntRange(1, 90).contains(it) } shouldBe true
                }
                it("no row should have more than 5 cells filled") {
                    ticket.all { row -> row.filterNotNull().size == 5 } shouldBe true
                }
                it("every column must have at least 1 cell filled") {
                    transposedTicket.all { column -> column.filterNotNull().isNotEmpty() } shouldBe true
                }
                it(
                    "The columns have rules on which numbers can be in them, in order:" +
                            "1-9, 10-19, 20-29, 30-39, 40-49, 50-59, 60-69, 70-79, 80-90"
                ) {
                    val ranges = listOf(
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
                    transposedTicket.zip(ranges)
                        .all { pair -> pair.first.filterNotNull().all { pair.first.contains(it) } } shouldBe true
                }
                it("numbers in every column are in ascending order from top to bottom") {
                    transposedTicket.all { column ->
                        val cellWithValues = column.filterNotNull()
                        cellWithValues == cellWithValues.sorted()
                    } shouldBe true
                }
            }
        }
    }
})
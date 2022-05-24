package tambola

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import tambola.TicketGenerator.fillColumnTemplates
import tambola.TicketGenerator.fillRowTemplates
import tambola.TicketGenerator.generate

class TicketGeneratorTest : DescribeSpec({
    describe("TicketGenerator") {
        context("fillColumnTemplates") {
            it("should not fill Empty values") {
                fillColumnTemplates(listOf(Empty, Empty), IntRange(1, 10)) shouldBe listOf(null, null)
            }

            it("should fill Fill values in ascending order") {
                fillColumnTemplates(listOf(Fill, Fill), IntRange(1, 2)) shouldBe listOf(1, 2)
            }

            it("should fill Fill values with random values from given range in ascending order ") {
                val givenRange = IntRange(1, 10)
                val columnElements = fillColumnTemplates(listOf(Fill, Empty, Fill, Fill), givenRange)

                val columnElementsWithValues = columnElements.filterNotNull()

                columnElementsWithValues.size shouldBe 3
                columnElementsWithValues.count {
                    givenRange.contains(it)
                } shouldBe 3

                columnElementsWithValues[0] shouldBeLessThan columnElementsWithValues[1]
                columnElementsWithValues[1] shouldBeLessThan columnElementsWithValues[2]
            }
        }

        context("fillRowTemplates") {
            it("should not fill Empty values") {
                fillRowTemplates(listOf(listOf(Empty, Empty), listOf(Empty, Empty))) shouldBe listOf(
                    listOf(
                        null,
                        null
                    ), listOf(null, null)
                )
            }

            it("should fill Fill values in ascending order") {
                val rows = fillRowTemplates(listOf(listOf(Fill, Fill), listOf(Fill, Fill)))

                IntRange(0, 9).contains(rows[0][0]) shouldBe true
                IntRange(0, 9).contains(rows[1][0]) shouldBe true

                IntRange(10, 19).contains(rows[0][1]) shouldBe true
                IntRange(10, 19).contains(rows[1][1]) shouldBe true

                rows[0][0]!! shouldBeLessThan rows[1][0]!!
                rows[0][1]!! shouldBeLessThan rows[1][1]!!
            }
        }

        context("print ticket") {
            TicketGenerator.print(generate())
        }
    }
})
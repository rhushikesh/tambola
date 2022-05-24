package tambola.extensions

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ListExtensionsKtTest : DescribeSpec({
    describe("ListExtensions") {
        context("transpose") {
            it("should transpose emptyList as emptyList") {
                listOf<List<Boolean>>().transpose() shouldBe listOf()
            }

            it("should transpose nested list") {
                listOf(listOf(1, 2, 3), listOf(4, 5, 6)).transpose() shouldBe listOf(
                    listOf(1, 4),
                    listOf(2, 5),
                    listOf(3, 6)
                )
            }
        }
        context("fillIfNot") {
            it("should fill default value if predicate is true") {
                listOf(true, true, true).fillIfNot({ it }, listOf(), 2) shouldBe listOf(2, 2, 2)
            }

            it("should fill from elementToFill if predicate is false") {
                listOf(false, true, false).fillIfNot({ it }, listOf(5, 3), 2) shouldBe listOf(5, 2, 3)
            }
        }
    }
})
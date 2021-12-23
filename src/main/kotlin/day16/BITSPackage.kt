package day16

sealed class BITSPackage {
    abstract val header: BITSHeader
    abstract val value: Long

    open val packages: List<BITSPackage> = emptyList()

    val versionSum: Int
        get() = header.version + packages.sumOf { it.versionSum }

    data class LiteralValue(override val header: BITSHeader, override val value: Long) : BITSPackage()

    data class Sum(override val header: BITSHeader, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = packages.sumOf { it.value }
    }

    data class Product(override val header: BITSHeader, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = packages.map { it.value }.fold(-1L) { acc, item ->
                if (acc == -1L) item else acc * item
            }
    }

    data class Minimum(override val header: BITSHeader, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = packages.map { it.value }.minOf { it }
    }

    data class Maximum(override val header: BITSHeader, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = packages.map { it.value }.maxOf { it }
    }

    data class GreaterThan(override val header: BITSHeader, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = if (packages.first().value > packages[1].value) 1 else 0
    }

    data class LessThan(override val header: BITSHeader, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = if (packages.first().value < packages[1].value) 1 else 0
    }

    data class EqualTo(override val header: BITSHeader, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = if (packages.first().value == packages[1].value) 1 else 0
    }
}
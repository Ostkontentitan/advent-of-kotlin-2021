package day16

sealed class BITSPackage {
    abstract val header: Header
    abstract val value: Long

    open val packages: List<BITSPackage> = emptyList()

    val versionSum: Int
        get() = header.version + packages.sumOf { it.versionSum }

    data class LiteralValue(override val header: Header, override val value: Long) : BITSPackage()

    data class Sum(override val header: Header, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = packages.sumOf { it.value }
    }

    data class Product(override val header: Header, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = packages.map { it.value }.fold(0L) { acc, item ->
                if (acc == 0L) item else acc * item
            }
    }

    data class Minimum(override val header: Header, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = packages.map { it.value }.minOf { it }
    }

    data class Maximum(override val header: Header, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = packages.map { it.value }.maxOf { it }
    }

    data class GreaterThan(override val header: Header, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = if (packages.first().value > packages[1].value) 1 else 0
    }

    data class LessThan(override val header: Header, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = if (packages.first().value < packages[1].value) 1 else 0
    }

    data class EqualTo(override val header: Header, override val packages: List<BITSPackage>) : BITSPackage() {
        override val value: Long
            get() = if (packages.first().value == packages[1].value) 1 else 0
    }
}
package day17

import readInput

fun puzzle() {
    val (targetX, targetY) = readInput(17).first().extractTargetArea()
    val launcher = ProbeLauncher()
    val report = launcher.calculateHittingShots(targetX, targetY)
    println("Probe launcher report: $report")
}

fun String.extractTargetArea() =
    replace("target area: x=", "")
        .replace(" y=", "")
        .split(",")
        .map {
            val (start, end) = it.split("..")
            start.toLong() .. end.toLong()
        }

package day17

class ProbeLauncher {

    fun calculateHittingShots(xRange: LongRange, yRange: LongRange): Report {

        val findings = mutableListOf<Long>()

        for (x in 1..xRange.last) {
            for (y in -10_000..10_000) {
                var projectile = Projectile(0, 0, x, y.toLong())
                var highestPosY = Long.MIN_VALUE
                for (reps in 1..1000) {
                    projectile = performStepOn(projectile)
                    if(projectile.posY > highestPosY) {
                        highestPosY = projectile.posY
                    }

                    if (xRange.contains(projectile.posX) && yRange.contains(projectile.posY)) {
                        findings.add(highestPosY)
                        break
                    }
                }
            }
        }

        return Report(findings.maxByOrNull { it }!!, findings.count().toLong())
    }

    private fun performStepOn(projectile: Projectile): Projectile {
        val newPosX = projectile.posX + projectile.velocityX
        val newPosY = projectile.posY + projectile.velocityY

        val newVelocityX = if (projectile.velocityX == 0L) {
            0L
        } else if (projectile.velocityX > 0L) {
            projectile.velocityX - 1
        } else if (projectile.velocityX < 0L) {
            projectile.velocityX + 1
        } else {
            throw IllegalStateException("Unhandled velocity case. ")
        }
        val newVelocityY = projectile.velocityY - 1

        return Projectile(newPosX, newPosY, newVelocityX, newVelocityY)
    }

    data class Report(val maxY: Long, val possibleShotsCount: Long)
}

data class Projectile(val posX: Long, val posY: Long, val velocityX: Long, val velocityY: Long)
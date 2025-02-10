package com.kzkv.pso.service

import com.kzkv.pso.config.PsoParams.ALPHA
import com.kzkv.pso.config.PsoParams.ITERATIONS_N
import com.kzkv.pso.config.PsoParams.PARTICLES_N
import com.kzkv.pso.config.PsoParams.W
import com.kzkv.pso.data.Obstacle
import com.kzkv.pso.data.Particle
import com.kzkv.pso.data.Vector
import kotlin.math.pow

class ParticleSwarmPathPlanner(
    private val start: Vector,
    private val goal: Vector,
    private val obstacles: List<Obstacle>,
) {
    private val particles = List(PARTICLES_N) {
        Particle(start, Vector(-1.0, 1.0), start)
    }.toMutableList()

    private var bestGlobalPosition = start.copy()
    private var bestGlobalPath = ArrayList<Vector>()

    fun findOptimalPath(): List<Vector> {
        bestGlobalPath.add(start)
        var w = W
        repeat(ITERATIONS_N) {
            particles.forEachIndexed { i, particle ->
                particle.move(bestGlobalPosition, w)

                if (Vector.getDistance(particle.position, goal) < Vector.getDistance(particle.bestPosition, goal)
                    && isNotPointIntersectsObstacle(particle.position)) {
                    particle.bestPosition = particle.position.copy()
                }

                if (Vector.getDistance(particle.bestPosition, goal) < Vector.getDistance(bestGlobalPosition, goal)
                    && isNotLineIntersectsObstacle(bestGlobalPath.last(), particle.bestPosition)) {
                    bestGlobalPosition = particle.bestPosition.copy()
                    if (bestGlobalPath.lastIndex > 0 && isNotLineIntersectsObstacle(bestGlobalPath[bestGlobalPath.lastIndex - 1], particle.bestPosition)) {
                        bestGlobalPath.removeLast()
                    }
                    bestGlobalPath.add(bestGlobalPosition)
                }
            }
            w *= ALPHA
        }
        if (isNotLineIntersectsObstacle(bestGlobalPath.last(), goal)) {
            bestGlobalPath.add(goal)
            return bestGlobalPath
        } else {
            return emptyList()
        }
    }

    private fun isNotPointIntersectsObstacle(point: Vector): Boolean {
        return obstacles.none { obstacle ->
            Vector.getDistance(point, obstacle.center) < obstacle.radius
        }
    }

    private fun isNotLineIntersectsObstacle(startPoint: Vector, endPoint: Vector): Boolean {
        return obstacles.none { obstacle ->
            val ab = Vector(startPoint, endPoint)
            val ap = Vector(startPoint, obstacle.center)
            val abxap = Vector.vektor(ab, ap)
            val isProject = (Vector.scalar(ap, ab) / ab.length().pow(2)) in 0.0..1.0
            val distance = abxap.length() / ab.length()
            isProject && distance < obstacle.radius
        }
    }

}

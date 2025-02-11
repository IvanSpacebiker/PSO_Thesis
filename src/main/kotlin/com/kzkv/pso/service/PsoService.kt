package com.kzkv.pso.service

import com.kzkv.pso.config.PsoParams.ALPHA
import com.kzkv.pso.config.PsoParams.ITERATIONS_N
import com.kzkv.pso.config.PsoParams.PARTICLES_N
import com.kzkv.pso.config.PsoParams.W
import com.kzkv.pso.entity.Obstacle
import com.kzkv.pso.data.PSOData
import com.kzkv.pso.entity.Particle
import com.kzkv.pso.data.Vector
import org.springframework.stereotype.Service
import kotlin.math.pow

@Service
class PsoService(private val start: Vector, private val goal: Vector, private val obstacles: List<Obstacle>) {
    private var route : List<Vector> = emptyList()
    private var restartCounter = 0
    private var time = 0L

    fun findRouteWithMetrics() : List<Vector> {
        val startTime = System.currentTimeMillis()
        while (true) {
            route = findOptimalRoute()
            if (route.isNotEmpty()) {
                break
            }
            restartCounter++
            if (restartCounter >= 100) {
                println("Bad parameters")
                return emptyList()
            }
        }
        time = System.currentTimeMillis() - startTime
        return route
    }

    private fun findOptimalRoute(): List<Vector> {
        val particles = List(PARTICLES_N) { Particle(start, Vector(-1.0, 1.0), start) }
        var bestGlobalPosition = start.copy()
        val bestGlobalRoute = arrayListOf(start)
        var inertia = W

        repeat(ITERATIONS_N) {
            particles.forEachIndexed { i, particle ->
                particle.move(bestGlobalPosition, inertia)

                if (Vector.getDistance(particle.position, goal) < Vector.getDistance(particle.bestPosition, goal)
                    && isNotPointIntersectsObstacle(particle.position)) {
                    particle.bestPosition = particle.position.copy()
                }

                if (Vector.getDistance(particle.bestPosition, goal) < Vector.getDistance(bestGlobalPosition, goal)
                    && isNotLineIntersectsObstacle(bestGlobalRoute.last(), particle.bestPosition)) {
                    bestGlobalPosition = particle.bestPosition.copy()
                    if (bestGlobalRoute.lastIndex > 0 && isNotLineIntersectsObstacle(bestGlobalRoute[bestGlobalRoute.lastIndex - 1], particle.bestPosition)) {
                        bestGlobalRoute.removeLast()
                    }
                    bestGlobalRoute.add(bestGlobalPosition)
                }
            }
            inertia *= ALPHA
        }
        if (isNotLineIntersectsObstacle(bestGlobalRoute.last(), goal)) {
            bestGlobalRoute.add(goal)
            return bestGlobalRoute
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

    override fun toString(): String {
        val string = StringBuilder()
        string.append("Route:\n")
        route.forEach { string.append(it.toString()).append("\n") }
        string.append("\nTotal time: ${time}ms\n")
        string.append("Number of restarts: $restartCounter")
        return string.toString()
    }

    fun createPSOData(): PSOData {
        return PSOData(obstacles, route, start, goal, restartCounter, time)
    }

}

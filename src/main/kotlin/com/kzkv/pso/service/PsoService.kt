package com.kzkv.pso.service

import com.kzkv.pso.data.ParticleParams
import com.kzkv.pso.data.Vector
import com.kzkv.pso.entity.Obstacle
import com.kzkv.pso.entity.Particle
import org.springframework.stereotype.Service
import kotlin.math.pow

@Service
class PsoService(
    private val jsonService: JsonService,
    private val obstacleService: ObstacleService
) {
    private var route : List<Vector> = emptyList()
    private var obstacles : List<Obstacle> = emptyList()

    fun findRoute(params: ParticleParams): List<Vector> {
        var restarts = 0
        obstacles = obstacleService.readObstacles()
        while (restarts < 100) {
            val start = params.endpoints.first()
            val goal = params.endpoints.last()
            val particles = List(params.numberOfParticles) { Particle(start, Vector(-1.0, 1.0), start) }
            var bestGlobalPosition = start.copy()
            val bestGlobalRoute = arrayListOf(start)
            var inertia = params.w

            repeat(params.numberOfIterations) {
                particles.forEachIndexed { i, particle ->
                    particle.move(bestGlobalPosition, inertia, params)

                    if (Vector.getDistance(particle.position, goal) < Vector.getDistance(particle.bestPosition, goal)
                        && isNotPointIntersectsObstacle(particle.position)
                    ) {
                        particle.bestPosition = particle.position.copy()
                    }

                    if (Vector.getDistance(particle.bestPosition, goal) < Vector.getDistance(bestGlobalPosition, goal)
                        && isNotLineIntersectsObstacle(bestGlobalRoute.last(), particle.bestPosition)
                    ) {
                        bestGlobalPosition = particle.bestPosition.copy()
                        if (bestGlobalRoute.lastIndex > 0 && isNotLineIntersectsObstacle(
                                bestGlobalRoute[bestGlobalRoute.lastIndex - 1],
                                particle.bestPosition
                            )
                        ) {
                            bestGlobalRoute.removeLast()
                        }
                        bestGlobalRoute.add(bestGlobalPosition)
                    }
                }
                inertia *= params.alpha
            }
            if (isNotLineIntersectsObstacle(bestGlobalRoute.last(), goal)) {
                bestGlobalRoute.add(goal)
                route = bestGlobalRoute
                jsonService.writeRoute(route)
                return route
            }
            restarts++
        }
        return route
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

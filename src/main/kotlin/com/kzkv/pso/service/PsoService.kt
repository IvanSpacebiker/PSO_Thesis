package com.kzkv.pso.service

import com.kzkv.pso.data.ObstacleParams
import com.kzkv.pso.data.Params
import com.kzkv.pso.data.Vector
import com.kzkv.pso.entity.Obstacle
import com.kzkv.pso.entity.Particle
import org.springframework.stereotype.Service
import kotlin.math.pow

@Service
class PsoService(private val statisticService: StatisticService) {
	private var route: ArrayList<Vector> = arrayListOf()
	var obstacles: List<Obstacle> = listOf()

	fun startPSO(params: Params): List<Vector> {
		val particleParams = params.particleParams
        val start = particleParams.endpoints.first()
		val goal = particleParams.endpoints.last()
		val particles = List(particleParams.numberOfParticles) { Particle(start, Vector(-1.0, 1.0), start) }
		var bestGlobalPosition = start.copy()
		val bestGlobalRoute = arrayListOf(start)
		var inertia = particleParams.w
		val objectRadius = particleParams.radius

		val startTime = System.nanoTime()
		for(i in 0 until particleParams.numberOfIterations) {
			if (isNotLineIntersectsObstacle(bestGlobalRoute.last(), goal, objectRadius)) {
				bestGlobalRoute.add(goal)
				route = if (route.isEmpty() || !isRouteValid(route, objectRadius)) bestGlobalRoute else getShortestRoute(route, bestGlobalRoute)
				break
			}
			particles.forEach { particle ->
				particle.move(bestGlobalPosition, inertia, particleParams)
				particle.getParticleBestPosition(goal, objectRadius, obstacles)

				if (shouldUpdateBestGlobal(particle, bestGlobalPosition, goal, bestGlobalRoute, objectRadius)) {
					bestGlobalPosition = particle.bestPosition.copy()
					optimizeBestRoute(bestGlobalRoute, bestGlobalPosition, objectRadius)
					bestGlobalRoute.add(bestGlobalPosition)
				}
			}
			inertia *= particleParams.alpha
		}
		val endTime = System.nanoTime()
		statisticService.addStats(startTime, endTime, route, getObstacleDensity(params.obstacleParams))
		return route
	}

	fun startPSO(params: Params, radius: Double): List<Vector> {
		val particleParams = params.particleParams
		val start = particleParams.endpoints.first()
		val goal = particleParams.endpoints.last()
		val particles = List(particleParams.numberOfParticles) { Particle(start, Vector(-1.0, 1.0), start) }
		var bestGlobalPosition = start.copy()
		val bestGlobalRoute = arrayListOf(start)
		var inertia = particleParams.w

		val startTime = System.nanoTime()
		for(i in 0 until particleParams.numberOfIterations) {
			if (isNotLineIntersectsObstacle(bestGlobalRoute.last(), goal, radius)) {
				bestGlobalRoute.add(goal)
				route = if (route.isEmpty() || !isRouteValid(route, radius)) bestGlobalRoute else getShortestRoute(route, bestGlobalRoute)
				break
			}
			particles.forEach { particle ->
				particle.move(bestGlobalPosition, inertia, particleParams)
				particle.getParticleBestPosition(goal, radius, obstacles)

				if (shouldUpdateBestGlobal(particle, bestGlobalPosition, goal, bestGlobalRoute, radius)) {
					bestGlobalPosition = particle.bestPosition.copy()
					optimizeBestRoute(bestGlobalRoute, bestGlobalPosition, radius)
					bestGlobalRoute.add(bestGlobalPosition)
				}
			}
			inertia *= particleParams.alpha
		}
		val endTime = System.nanoTime()
		statisticService.addStats(startTime, endTime, route, getObstacleDensity(params.obstacleParams))
		return route
	}

	private fun isRouteValid(route: List<Vector>, radius: Double) : Boolean {
		for (i in 0..route.size - 2) {
			if (!isNotLineIntersectsObstacle(route[i], route[i+1], radius)) return false
		}
		return true
	}

	private fun getShortestRoute(r1: ArrayList<Vector>, r2: ArrayList<Vector>) : ArrayList<Vector> {
		var r1Length = 0.0
		var r2Length = 0.0
		for (i in 0..r1.size - 2) {
			r1Length += (r1[i] - r1[i+1]).length()
		}
		for (i in 0..r2.size - 2) {
			r2Length += (r2[i] - r2[i+1]).length()
		}
		return if (r1Length <= r2Length) r1 else r2
	}

	private fun shouldUpdateBestGlobal(particle: Particle, bestGlobalPosition: Vector, goal: Vector, bestGlobalRoute: List<Vector>, radius: Double)
	: Boolean {
		return Vector.getDistance(particle.bestPosition, goal) < Vector.getDistance(bestGlobalPosition, goal) &&
				isNotLineIntersectsObstacle(bestGlobalRoute.last(), particle.bestPosition, radius)
	}

	private fun optimizeBestRoute(bestGlobalRoute: MutableList<Vector>, bestGlobalPosition: Vector, radius: Double) {
		if (bestGlobalRoute.size > 1 && isNotLineIntersectsObstacle(
				bestGlobalRoute[bestGlobalRoute.lastIndex - 1],
				bestGlobalPosition,
				radius
			)
		) {
			bestGlobalRoute.removeLast()
		}
	}

	private fun isNotLineIntersectsObstacle(startPoint: Vector, endPoint: Vector, radius: Double): Boolean {
		return obstacles.none { obstacle ->
			val ab = endPoint - startPoint
			val ap = obstacle.center - startPoint
			val abxap = Vector.vektor(ab, ap)
			val isProject = (Vector.scalar(ap, ab) / ab.length().pow(2)) in 0.0..1.0
			val distance = abxap.length() / ab.length()
			(ab != ap) && isProject && (distance - radius) < obstacle.radius
		}
	}

	fun clearRoute() {
		route = arrayListOf()
	}

	private fun getObstacleDensity(obstacleParams: ObstacleParams, samples: Int = 10_000): Double {
		if (obstacles.isEmpty() || samples <= 0) return 0.0

		var insideCount = 0
		repeat(samples) {
			val point = Vector(obstacleParams.endpoints.first(), obstacleParams.endpoints.last())

			if (obstacles.any { obstacle -> Vector.getDistance(obstacle.center, point) <= obstacle.radius }) {
				insideCount++
			}
		}

		return insideCount.toDouble() / samples
	}


}

package com.kzkv.pso.service

import com.kzkv.pso.data.ParticleParams
import com.kzkv.pso.data.Vector
import com.kzkv.pso.entity.Obstacle
import com.kzkv.pso.entity.Particle
import org.springframework.stereotype.Service
import kotlin.math.PI
import kotlin.math.pow

@Service
class PsoService {
	var route: ArrayList<Vector> = arrayListOf()
	var obstacles: List<Obstacle> = listOf()
	var visibleObstacles: List<Obstacle> = listOf()

	fun startPSO(params: ParticleParams): List<Vector> {
        val start = params.endpoints.first()
		val goal = params.endpoints.last()
		val particles = List(params.numberOfParticles) { Particle(start, Vector(-1.0, 1.0), start) }
		var bestGlobalPosition = start.copy()
		val bestGlobalRoute = arrayListOf(start)
		var inertia = params.w
		visibleObstacles = obstacles.filter { it.visible }

		repeat(params.numberOfIterations) {
			particles.forEach { particle ->
				particle.move(bestGlobalPosition, inertia, params)
				particle.getParticleBestPosition(goal, visibleObstacles)

				if (shouldUpdateBestGlobal(particle, bestGlobalPosition, goal, bestGlobalRoute)) {
					bestGlobalPosition = particle.bestPosition.copy()
					optimizeBestRoute(bestGlobalRoute, bestGlobalPosition)
					bestGlobalRoute.add(bestGlobalPosition)
				}
			}
			inertia *= params.alpha
		}
		if (isNotLineIntersectsObstacle(bestGlobalRoute.last(), goal)) {
			bestGlobalRoute.add(goal)
			route = if (route.isEmpty() || !isRouteValid(route)) bestGlobalRoute else getShortestRoute(route, bestGlobalRoute)
		}
		updateObstacleVisibility(route[0], route[1], PI / 4)

		return route
	}

	private fun isRouteValid(route: List<Vector>) : Boolean {
		for (i in 0..route.size - 2) {
			if (!isNotLineIntersectsObstacle(route[i], route[i+1])) return false
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

	private fun shouldUpdateBestGlobal(particle: Particle, bestGlobalPosition: Vector, goal: Vector, bestGlobalRoute: List<Vector>)
	: Boolean {
		return Vector.getDistance(particle.bestPosition, goal) < Vector.getDistance(bestGlobalPosition, goal) &&
				isNotLineIntersectsObstacle(bestGlobalRoute.last(), particle.bestPosition)
	}

	private fun optimizeBestRoute(bestGlobalRoute: MutableList<Vector>, bestGlobalPosition: Vector) {
		if (bestGlobalRoute.size > 1 && isNotLineIntersectsObstacle(
				bestGlobalRoute[bestGlobalRoute.lastIndex - 1],
				bestGlobalPosition
			)
		) {
			bestGlobalRoute.removeLast()
		}
	}

	private fun isNotLineIntersectsObstacle(startPoint: Vector, endPoint: Vector): Boolean {
		return visibleObstacles.none { obstacle ->
			val ab = endPoint - startPoint
			val ap = obstacle.center - startPoint
			val abxap = Vector.vektor(ab, ap)
			val isProject = (Vector.scalar(ap, ab) / ab.length().pow(2)) in 0.0..1.0
			val distance = abxap.length() / ab.length()
			(ab != ap) && isProject && distance < obstacle.radius
		}
	}

	fun clearRoute() {
		route = arrayListOf()
	}

	private fun updateObstacleVisibility(start: Vector, goal: Vector, fovAngle: Double) {
		obstacles.forEach { it.visible = false }

		val obstaclesInFOV = obstacles.filter { obstacle ->
			val toObstacle = obstacle.center - start
			val viewDirection = goal - start
			val angle = Vector.angleBetween(viewDirection.normalized(), toObstacle.normalized())

			angle <= fovAngle / 2
		}.sortedBy { Vector.getDistance(it.center, start) }

		for (obstacle in obstaclesInFOV) {
			if (isNotLineIntersectsObstacle(start, obstacle.center)) obstacle.visible = true
		}
	}

}

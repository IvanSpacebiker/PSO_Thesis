package com.kzkv.pso.entity

import com.kzkv.pso.data.ParticleParams
import com.kzkv.pso.data.Vector
import kotlin.random.Random


class Particle(private var position: Vector, private var velocity: Vector, var bestPosition: Vector) {
	fun move(bestGlobalPosition: Vector, inertia: Double, params: ParticleParams) {
		val newVelocity = velocity * inertia +
				(bestPosition - position) * params.c1 * Random.nextDouble() +
				(bestGlobalPosition - position) * params.c1 * Random.nextDouble()
		position += newVelocity
	}

	fun getParticleBestPosition(goal: Vector, obstacles: List<Obstacle>) {
		if (Vector.getDistance(this.position, goal) < Vector.getDistance(this.bestPosition, goal)
			&& isNotPointIntersectsObstacle(this.position, obstacles)
		) {
			this.bestPosition = this.position.copy()
		}
	}

	private fun isNotPointIntersectsObstacle(point: Vector, obstacles: List<Obstacle>): Boolean {
		return obstacles.none { obstacle ->
			Vector.getDistance(point, obstacle.center) < obstacle.radius
		}
	}
}

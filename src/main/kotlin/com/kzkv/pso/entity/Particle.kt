package com.kzkv.pso.entity

import com.kzkv.pso.data.ParticleParams
import com.kzkv.pso.data.Vector
import kotlin.random.Random


class Particle(var position: Vector, private var velocity: Vector, var bestPosition: Vector) {
	fun move(bestGlobalPosition: Vector, inertia: Double, params: ParticleParams) {
		val newVelocity = velocity * inertia +
				(bestPosition - position) * params.c1 * Random.nextDouble() +
				(bestGlobalPosition - position) * params.c1 * Random.nextDouble()
		position += newVelocity
	}
}

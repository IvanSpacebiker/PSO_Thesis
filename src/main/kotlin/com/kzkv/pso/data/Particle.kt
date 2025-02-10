package com.kzkv.pso.data

import com.kzkv.pso.config.PsoParams.C1
import com.kzkv.pso.config.PsoParams.C2
import kotlin.random.Random


data class Particle(var position: Vector, var velocity: Vector, var bestPosition: Vector) {
	fun move(bestGlobalPostion: Vector, w: Double) {
		val newVelocity = velocity * w +
				(bestPosition - position) * C1 * Random.nextDouble() +
				(bestGlobalPostion - position) * C2 * Random.nextDouble()
		position += newVelocity
	}
}

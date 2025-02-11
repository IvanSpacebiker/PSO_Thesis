package com.kzkv.pso.entity

import com.kzkv.pso.config.PsoParams.C1
import com.kzkv.pso.config.PsoParams.C2
import com.kzkv.pso.data.Vector
import kotlin.random.Random


class Particle(var position: Vector, private var velocity: Vector, var bestPosition: Vector) {
	fun move(bestGlobalPostion: Vector, w: Double) {
		val newVelocity = velocity * w +
				(bestPosition - position) * C1 * Random.nextDouble() +
				(bestGlobalPostion - position) * C2 * Random.nextDouble()
		position += newVelocity
	}
}

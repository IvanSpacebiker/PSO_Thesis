package com.kzkv.pso.data

import kotlin.math.min
import kotlin.random.Random


data class Obstacle(val center: Vector, val radius: Double) {
	constructor(x: Double, y: Double, z: Double, radius: Double) : this(Vector(x, y, z), radius)
	constructor(from: Vector, until: Vector, endpoints: List<Vector>) : this(
		Random.nextDouble(from.x, until.x),
		Random.nextDouble(from.y, until.y),
		Random.nextDouble(from.z, until.z),
		Random.nextDouble(
			min(Vector.getDistance(endpoints.first(), from), Vector.getDistance(endpoints.last(), until)) / 2.0
		)
	)
	companion object {
		fun createLocation(n: Int, endpoints: List<Vector>): List<Obstacle> {
			val gap = Vector(1.0, 1.0, 1.0)
			return List(n) {
				Obstacle(endpoints.first() + gap, endpoints.last() - gap, endpoints)
			}
		}
	}
}

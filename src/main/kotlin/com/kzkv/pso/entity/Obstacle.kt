package com.kzkv.pso.entity

import com.kzkv.pso.data.Vector
import kotlin.math.min
import kotlin.random.Random


data class Obstacle(
	var center: Vector,
	var radius: Double,
	val startPoint: Vector,
	val endPoint: Vector,
	val speed: Double
) {
	constructor(x: Double, y: Double, z: Double, radius: Double, startPoint: Vector, endPoint: Vector, speed: Double) :
			this(Vector(x, y, z), radius, startPoint, endPoint, speed)

	constructor(x: Double, y: Double, z: Double, radius: Double) :
			this(Vector(x, y, z), radius, Vector(x, y, z), Vector(x, y, z), 0.0)

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

	private var progress = 0.0
	private var forward = true

	fun move() {
		val delta = speed / 100.0
		progress = if (forward) progress + delta else progress - delta

		if (progress >= 1.0) {
			progress = 1.0
			forward = false
		} else if (progress <= 0.0) {
			progress = 0.0
			forward = true
		}
		this.center = startPoint * (1 - progress) + endPoint * progress
	}
}

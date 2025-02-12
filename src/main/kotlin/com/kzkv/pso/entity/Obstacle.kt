package com.kzkv.pso.entity

import com.kzkv.pso.data.Vector
import kotlin.random.Random


data class Obstacle(
	var center: Vector,
	var radius: Double,
	val startPoint: Vector,
	val endPoint: Vector,
	val speed: Double
) {
	constructor() : this(Vector(), 0.0, Vector(), Vector(), 0.0)
	constructor(x: Double, y: Double, z: Double, radius: Double, startPoint: Vector, endPoint: Vector, speed: Double) :
			this(Vector(x, y, z), radius, startPoint, endPoint, speed)

	constructor(x: Double, y: Double, z: Double, radius: Double) :
			this(Vector(x, y, z), radius, Vector(x, y, z), Vector(x, y, z), 0.0)

	constructor(endpoints: List<Vector>) : this(
		Random.nextDouble(endpoints.first().x, endpoints.last().x),
		Random.nextDouble(endpoints.first().y, endpoints.last().y),
		Random.nextDouble(endpoints.first().z, endpoints.last().z),
		Random.nextDouble(Vector(1.0, 1.0, 1.0).length() / 2.0)
	)

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

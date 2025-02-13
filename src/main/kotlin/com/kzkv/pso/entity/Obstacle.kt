package com.kzkv.pso.entity

import com.kzkv.pso.data.ObstacleParams
import com.kzkv.pso.data.Vector
import java.util.UUID
import kotlin.random.Random


data class Obstacle(
	val id : String,
	var center: Vector,
	var radius: Double,
	var startPoint: Vector,
	var endPoint: Vector,
	var speed: Double
) {
	constructor() : this(UUID.randomUUID().toString(), Vector(), 0.0, Vector(), Vector(), 0.0)
	constructor(params: ObstacleParams) : this() {
		val vector = Vector(params.endpoints.first(), params.endpoints.last())
		this.center = vector
		this.radius = Random.nextDouble(Vector(1.0, 1.0, 1.0).length() / 2.0)
		this.startPoint = vector
		this.endPoint = vector
		this.speed = 0.0
		if (params.isMoving) {
			this.endPoint += Vector.random() * 5.0
			this.speed = Random.nextDouble(10.0)
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

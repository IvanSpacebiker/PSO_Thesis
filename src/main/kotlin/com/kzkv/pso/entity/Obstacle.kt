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
) {
	constructor() : this(UUID.randomUUID().toString(), Vector(), 0.0, Vector(), Vector())
	constructor(params: ObstacleParams) : this() {
		val vector = Vector(params.endpoints.first(), params.endpoints.last())
		this.center = vector
		this.radius = Random.nextDouble(Vector(1.0, 1.0, 1.0).length() / 2.0)
		this.startPoint = vector
		this.endPoint = vector
	}

}

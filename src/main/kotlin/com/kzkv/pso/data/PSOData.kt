package com.kzkv.pso.data

data class PSOData(
	val obstacles: List<Obstacle>,
	val route: List<Vector>,
	val start: Vector,
	val goal: Vector,
	val restarts: Int,
	val time: Long
)

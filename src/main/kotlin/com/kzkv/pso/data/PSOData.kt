package com.kzkv.pso.data

import com.kzkv.pso.entity.Obstacle

data class PSOData(
	val obstacles: List<Obstacle>,
	val route: List<Vector>,
	val start: Vector,
	val goal: Vector,
	val restarts: Int,
	val time: Long
)

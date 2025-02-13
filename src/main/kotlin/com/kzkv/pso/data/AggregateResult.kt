package com.kzkv.pso.data

import com.kzkv.pso.entity.Obstacle

data class AggregateResult(
	val obstacles: List<Obstacle>,
	val route: List<Vector>,
)

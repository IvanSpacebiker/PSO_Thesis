package com.kzkv.pso.data

data class ObstacleParams(
	val numberOfObstacles : Int,
	val endpoints : List<Vector>,
	val isMoving : Boolean
)

package com.kzkv.pso.service

import com.kzkv.pso.data.ObstacleParams
import com.kzkv.pso.entity.Obstacle
import org.springframework.stereotype.Service

@Service
open class ObstacleService(private val jsonService: JsonService) {
	private var obstacles : List<Obstacle> = emptyList()

	fun readObstacles() : List<Obstacle> {
		obstacles = jsonService.readObstacles()
		return obstacles
	}

	fun createObstacles(params: ObstacleParams) : List<Obstacle> {
		obstacles = List(params.numberOfObstacles) { Obstacle(params.endpoints) }
		jsonService.writeObstacles(obstacles)
		return obstacles
	}
}
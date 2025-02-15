package com.kzkv.pso.service

import com.kzkv.pso.data.Params
import com.kzkv.pso.entity.Obstacle
import org.springframework.stereotype.Service

@Service
open class ObstacleService(private val jsonService: JsonService, private val psoService: PsoService) {

	fun readObstacles() : List<Obstacle> {
		psoService.obstacles = jsonService.readObstacles()
		return psoService.obstacles
	}

	fun createObstacles(params: Params) : List<Obstacle> {
		psoService.obstacles = List(params.obstacleParams.numberOfObstacles) {
			Obstacle(params.obstacleParams)
		}
		return psoService.obstacles
	}

}
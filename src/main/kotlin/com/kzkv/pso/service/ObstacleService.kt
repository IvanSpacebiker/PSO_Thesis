package com.kzkv.pso.service

import com.kzkv.pso.data.Params
import com.kzkv.pso.data.Statistic
import com.kzkv.pso.entity.Obstacle
import org.springframework.stereotype.Service

@Service
open class ObstacleService(
	private val jsonService: JsonService,
	private val psoService: PsoService,
	private val statisticService: StatisticService
) {

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

	private fun createObstacles(params: Params, amount: Int) : List<Obstacle> {
		psoService.obstacles = List(amount) {
			Obstacle(params.obstacleParams)
		}
		return psoService.obstacles
	}

	fun test(params: Params) : List<Statistic> {
		statisticService.clearStats()
		for (i in 10..1000 step 10) {
			createObstacles(params, i)
			psoService.startPSO(params.particleParams)
			psoService.clearRoute()
		}
		return statisticService.writeStatistic()
	}

}
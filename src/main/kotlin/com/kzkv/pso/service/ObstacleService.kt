package com.kzkv.pso.service

import com.kzkv.pso.config.ObstacleHandler
import com.kzkv.pso.data.Params
import com.kzkv.pso.data.ParticleParams
import com.kzkv.pso.entity.Obstacle
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Service
open class ObstacleService(
	private val jsonService: JsonService,
	private val obstacleHandler: ObstacleHandler,
	private val psoService: PsoService
) {
	private val executor = Executors.newScheduledThreadPool(1)
	private var moving = false

	fun readObstacles() : List<Obstacle> {
		psoService.obstacles = jsonService.readObstacles()
		return psoService.obstacles
	}

	fun createObstacles(params: Params) : List<Obstacle> {
		psoService.obstacles = List(params.obstacleParams.numberOfObstacles) { Obstacle(params.obstacleParams) }
		jsonService.writeObstacles(psoService.obstacles)
		if (params.obstacleParams.isMoving) startMovingObstacles(params.particleParams)
		return psoService.obstacles
	}

	@Async
	open fun startMovingObstacles(params: ParticleParams) {
		if (moving) return
		moving = true
		executor.scheduleAtFixedRate({
			if (!moving) return@scheduleAtFixedRate
			psoService.obstacles.forEach { it.move() }
			obstacleHandler.broadcastObstacles(psoService.obstacles, psoService.findRoute(params))
		}, 0, 40, TimeUnit.MILLISECONDS)
	}

	fun stopMovingObstacles() {
		moving = false
	}
}
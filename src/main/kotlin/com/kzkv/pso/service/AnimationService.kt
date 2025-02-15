package com.kzkv.pso.service

import com.kzkv.pso.config.ObstacleHandler
import com.kzkv.pso.data.ParticleParams
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Service
open class AnimationService(
	private val psoService: PsoService,
	private val obstacleHandler: ObstacleHandler
) {
	private val executor = Executors.newScheduledThreadPool(1)
	private var moving = false

	@Async
	open fun startAnimation(params: ParticleParams) {
		if (moving) return
		moving = true
		executor.scheduleAtFixedRate({
			if (!moving) return@scheduleAtFixedRate
			psoService.obstacles.forEach { it.move() }
			obstacleHandler.broadcastObjects(psoService.obstacles, psoService.startPSO(params))
		}, 0, 40, TimeUnit.MILLISECONDS)
	}

	fun stopAnimation() {
		moving = false
	}
}
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
	private var startPointMoving = false
	private lateinit var params: ParticleParams

	@Async
	open fun startAnimation(params: ParticleParams) {
		setParams(params)
		if (moving) return
		moving = true
		executor.scheduleAtFixedRate({
			if (!moving) return@scheduleAtFixedRate
			if (startPointMoving) this.params.endpoints[0] = this.params.endpoints[0] + (psoService.route[1] - psoService.route[0]) * 0.02
			psoService.obstacles.forEach { it.move() }
			obstacleHandler.broadcastObjects(psoService.obstacles, psoService.startPSO(this.params))
		}, 0, 40, TimeUnit.MILLISECONDS)
	}

	fun stopAnimation() {
		moving = false
	}

	fun moveStartPoint() {
		startPointMoving = !startPointMoving
	}

	private fun setParams(params: ParticleParams) {
		psoService.clearRoute()
		this.params = params
	}
}
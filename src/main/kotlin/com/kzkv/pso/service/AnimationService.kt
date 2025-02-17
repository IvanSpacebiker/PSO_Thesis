package com.kzkv.pso.service

import com.kzkv.pso.config.WebSocketHandler
import com.kzkv.pso.data.ParticleParams
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

@Service
open class AnimationService(
	private val psoService: PsoService,
	private val webSocketHandler: WebSocketHandler
) {
	private val executor = Executors.newScheduledThreadPool(1)
	private var moving = false
	private var startPointMoving = false
	private lateinit var params: ParticleParams
	private var scheduledTask: ScheduledFuture<*>? = null

	@Async
	open fun startAnimation(params: ParticleParams) {
		setParams(params)
		if (moving) stopAnimation()
		moving = true
		scheduledTask = executor.scheduleAtFixedRate({
			if (!moving) return@scheduleAtFixedRate
			if (startPointMoving) this.params.endpoints[0] = this.params.endpoints[0] + (psoService.route[1] - psoService.route[0]) * 0.02
			psoService.obstacles.forEach { it.move() }
			webSocketHandler.broadcastObjects(psoService.obstacles, psoService.startPSO(this.params))
		}, 0, 40, TimeUnit.MILLISECONDS)
	}

	fun stopAnimation() {
		scheduledTask?.cancel(false)
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
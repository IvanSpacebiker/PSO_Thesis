package com.kzkv.pso.service

import com.kzkv.pso.config.WebSocketHandler
import com.kzkv.pso.data.ParticleParams
import com.kzkv.pso.data.Vector
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

@Service
open class AnimationService(
	private val psoService: PsoService,
	private val webSocketHandler: WebSocketHandler,
	private val quadcopterService: QuadcopterService,
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
			psoService.obstacles.forEach { it.move() }
			val route = psoService.startPSO(this.params)
			if (startPointMoving) {
				if (route.isEmpty()) quadcopterService.setTarget(params.endpoints.last()) else quadcopterService.setTarget(route[1])
				quadcopterService.updateQuadcopter(0.040)
				val state = quadcopterService.getQuadcopterState()
				this.params.endpoints[0] = Vector(state.x, state.y, state.z)
			}
			webSocketHandler.broadcastObjects(psoService.obstacles, route)
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
		val state = quadcopterService.getQuadcopterState()
		state.x = params.endpoints.first().x
		state.y = params.endpoints.first().y
		state.z = params.endpoints.first().z
	}
}
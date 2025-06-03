package com.kzkv.pso.service

import com.kzkv.pso.config.WebSocketHandler
import com.kzkv.pso.data.Params
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

@Service
open class AnimationService(
	private val psoService: PsoService,
	private val webSocketHandler: WebSocketHandler,
	private val statisticService: StatisticService,
) {
	private val executor = Executors.newScheduledThreadPool(1)
	private var isStarted = false
	private lateinit var params: Params
	private var scheduledTask: ScheduledFuture<*>? = null

	@Async
	open fun startAnimation(params: Params) {
		setParams(params)
		if (isStarted) return
		isStarted = true
		statisticService.clearStats()
		scheduledTask = executor.scheduleAtFixedRate({
			if (!isStarted) return@scheduleAtFixedRate
			psoService.obstacles.forEach { it.move() }
			webSocketHandler.broadcastObjects(psoService.obstacles, psoService.startPSO(this.params))
		}, 0, 40, TimeUnit.MILLISECONDS)
	}

	fun stopAnimation() {
		statisticService.writeStatistic()
		scheduledTask?.cancel(false)
		isStarted = false
	}

	private fun setParams(params: Params) {
		psoService.clearRoute()
		this.params = params
	}
}
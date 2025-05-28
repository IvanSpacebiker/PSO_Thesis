package com.kzkv.pso.controller

import com.kzkv.pso.data.Params
import com.kzkv.pso.data.Statistic
import com.kzkv.pso.entity.Obstacle
import com.kzkv.pso.service.ObstacleService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("obstacle")
class ObstacleController(
	private val obstacleService: ObstacleService,
) {

	@PostMapping
	fun createObstacles(@RequestBody params: Params) : ResponseEntity<List<Obstacle>> {
		return ResponseEntity.ok(obstacleService.createObstacles(params))
	}

	@PostMapping("test")
	fun runTest(@RequestBody params: Params) : ResponseEntity<List<Statistic>> {
		return ResponseEntity.ok(obstacleService.test(params))
	}

	@PostMapping("test-radius")
	fun runTestRadius(@RequestBody params: Params) : ResponseEntity<List<Statistic>> {
		return ResponseEntity.ok(obstacleService.testRadius(params))
	}

}
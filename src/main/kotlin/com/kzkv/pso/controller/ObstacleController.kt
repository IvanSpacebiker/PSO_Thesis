package com.kzkv.pso.controller

import com.kzkv.pso.data.Params
import com.kzkv.pso.entity.Obstacle
import com.kzkv.pso.service.JsonService
import com.kzkv.pso.service.ObstacleService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("obstacle")
class ObstacleController(
	private val obstacleService: ObstacleService,
	private val jsonService: JsonService
) {

	@GetMapping("/json")
	fun readObstacles() : ResponseEntity<List<Obstacle>> {
		return ResponseEntity.ok(obstacleService.readObstacles())
	}

	@PostMapping("/json")
	fun writeObstacles(@RequestBody obstacles: List<Obstacle>) : ResponseEntity<List<Obstacle>> {
		return ResponseEntity.ok(jsonService.writeObstacles(obstacles))
	}

	@PostMapping
	fun createObstacles(@RequestBody params: Params) : ResponseEntity<List<Obstacle>> {
		return ResponseEntity.ok(obstacleService.createObstacles(params))
	}

}
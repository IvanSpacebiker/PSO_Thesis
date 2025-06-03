package com.kzkv.pso.controller

import com.kzkv.pso.data.Statistic
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
@RequestMapping("json")
class JsonController(
	private val obstacleService: ObstacleService,
	private val jsonService: JsonService
) {

	@GetMapping("obstacle")
	fun readObstacles() : ResponseEntity<List<Obstacle>> {
		return ResponseEntity.ok(obstacleService.readObstacles())
	}

	@PostMapping("obstacle")
	fun writeObstacles(@RequestBody obstacles: List<Obstacle>) : ResponseEntity<List<Obstacle>> {
		return ResponseEntity.ok(jsonService.writeObstacles(obstacles))
	}

	@PostMapping("statistic")
	fun writeStatistics(@RequestBody statistics: List<Statistic>) : ResponseEntity<List<Statistic>> {
		return ResponseEntity.ok(jsonService.writeStatistic(statistics))
	}

}
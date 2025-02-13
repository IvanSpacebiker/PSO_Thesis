package com.kzkv.pso.controller

import com.kzkv.pso.data.Params
import com.kzkv.pso.entity.Obstacle
import com.kzkv.pso.service.ObstacleService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("obstacle")
class ObstacleController(private val obstacleService: ObstacleService) {

	@GetMapping
	fun readObstacles() : ResponseEntity<List<Obstacle>> {
		return ResponseEntity.ok(obstacleService.readObstacles())
	}

//	@PostMapping
//	fun writeObstacles(@RequestBody params: ObstacleParams) : ResponseEntity<List<Obstacle>> {
//		return ResponseEntity.ok(obstacleService.createObstacles(params))
//	}

	@PostMapping
	fun startProcess(@RequestBody params: Params) : ResponseEntity<List<Obstacle>> {
		return ResponseEntity.ok(obstacleService.createObstacles(params))
	}

}
package com.kzkv.pso.controller

import com.kzkv.pso.data.QuadcopterParams
import com.kzkv.pso.service.QuadcopterService
import com.kzkv.pso.data.Vector
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/quadcopter")
class QuadcopterController(private val quadcopterService: QuadcopterService) {

	@PostMapping("/initialize")
	fun initialize(@RequestBody params: QuadcopterParams): String {
		quadcopterService.initializeQuadcopter(params.radius, params.gravity, params.mass)
		return "Quadcopter initialized with radius=${params.radius}, gravity=${params.gravity}, mass=${params.mass}"
	}

	@PostMapping("/setTarget")
	fun setTarget(@RequestBody target: Vector): String {
		quadcopterService.setTarget(target)
		return "Target set to x=${target.x}, y=${target.y}, z=${target.z}"
	}

	@PostMapping("/update")
	fun update(@RequestParam dt: Double): String {
		quadcopterService.updateQuadcopter(dt)
		return "Quadcopter updated with dt=$dt"
	}

	@GetMapping("/state")
	fun getState() = quadcopterService.getQuadcopterState()
}
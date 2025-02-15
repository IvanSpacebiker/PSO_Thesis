package com.kzkv.pso.controller

import com.kzkv.pso.data.ParticleParams
import com.kzkv.pso.service.AnimationService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
class AnimationController(private val animationService: AnimationService) {

	@PostMapping("start")
	fun startAnimation(@RequestBody params: ParticleParams) : ResponseEntity<Unit> {
		return ResponseEntity.ok(animationService.startAnimation(params))
	}

	@PostMapping("stop")
	fun startAnimation() : ResponseEntity<Unit> {
		return ResponseEntity.ok(animationService.stopAnimation())
	}

}
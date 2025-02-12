package com.kzkv.pso.controller

import com.kzkv.pso.data.ParticleParams
import com.kzkv.pso.data.Vector
import com.kzkv.pso.service.PsoService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("pso")
class PsoController(private val psoService: PsoService) {
	@PostMapping
	fun findRoute(@RequestBody params: ParticleParams): ResponseEntity<List<Vector>> {
		return ResponseEntity.ok(psoService.findRoute(params))
	}
}
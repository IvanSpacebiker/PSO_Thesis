package com.kzkv.pso.service

import com.kzkv.pso.data.Vector
import com.kzkv.pso.entity.Quadcopter
import org.springframework.stereotype.Service


@Service
class QuadcopterService {
	private lateinit var quadcopter: Quadcopter

	fun initializeQuadcopter(radius: Double, gravity: Double, mass: Double) {
		quadcopter = Quadcopter(radius, gravity, mass)
	}

	fun updateQuadcopter(dt: Double) {
		quadcopter.update(dt)
	}

	fun setTarget(target: Vector) {
		quadcopter.target = target
	}

	fun getQuadcopterState() = quadcopter.state
}
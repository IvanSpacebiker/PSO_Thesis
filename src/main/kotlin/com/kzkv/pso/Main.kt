package com.kzkv.pso

import com.kzkv.pso.config.PsoParams.IS_READ
import com.kzkv.pso.service.JsonService
import com.kzkv.pso.service.PSO


fun main() {
	val jsonService = JsonService()
	val endpoints = jsonService.readEndpoints()
	val obstacles = jsonService.getObstacles(IS_READ, 1000)
	val pso = PSO(endpoints.first(), endpoints.last(), obstacles)
	jsonService.writeStatistics(pso, 1)
}


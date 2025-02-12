package com.kzkv.pso.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kzkv.pso.entity.Obstacle
import com.kzkv.pso.data.Vector
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths

@Service
class JsonService {
	private val mapper = jacksonObjectMapper()
	private val resourcesPath = Paths.get("src/main/resources").toString()
	private val routeJson = File("$resourcesPath/route.json")
	private val obstaclesJson = File("$resourcesPath/obstacles.json")
	private val statisticsJson = File("$resourcesPath/statistics.json")

	fun readObstacles() : List<Obstacle> {
		return mapper.readValue(obstaclesJson)
	}

	fun writeObstacles(obstacles: List<Obstacle>) : List<Obstacle> {
		mapper.writerWithDefaultPrettyPrinter().writeValue(obstaclesJson, obstacles)
		return obstacles
	}

	fun writeRoute(route: List<Vector>): List<Vector> {
		mapper.writerWithDefaultPrettyPrinter().writeValue(routeJson.bufferedWriter(), route)
		return route
	}

//	fun writeStatistics(pso: PsoService, n: Int) {
//		val psoList = arrayListOf<PSOData>()
//		for (i in 0..< n) {
//			pso.findRouteWithMetrics()
//			psoList.add(pso.createPSOData())
//		}
//		mapper.writerWithDefaultPrettyPrinter().writeValue(statisticsJson.bufferedWriter(), psoList)
//	}
}
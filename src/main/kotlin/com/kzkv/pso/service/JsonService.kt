package com.kzkv.pso.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kzkv.pso.data.Obstacle
import com.kzkv.pso.data.PSOData
import com.kzkv.pso.data.Vector
import java.io.File
import java.nio.file.Paths

class JsonService {
	val mapper = jacksonObjectMapper()
	private val resourcesPath = Paths.get("src/main/resources").toString()
	private val routeJson = File("$resourcesPath/route.json")
	private val endpointsJson = File("$resourcesPath/endpoints.json")
	private val obstaclesJson = File("$resourcesPath/obstacles.json")
	private val statisticsJson = File("$resourcesPath/statistics.json")

	fun getObstacles(isRead: Boolean, n: Int) : List<Obstacle> {
		if (isRead) {
			return mapper.readValue(obstaclesJson)
		}
		val obstacles = Obstacle.createLocation(n, readEndpoints())
		mapper.writerWithDefaultPrettyPrinter().writeValue(obstaclesJson, obstacles)
		return obstacles
	}

	fun readEndpoints() : List<Vector> {
		return mapper.readValue(endpointsJson)
	}

	fun writeRoute(route: List<Vector>) {
		mapper.writerWithDefaultPrettyPrinter().writeValue(routeJson.bufferedWriter(), route)
	}

	fun writeStatistics(pso: PSO, n: Int) {
		val psoList = arrayListOf<PSOData>()
		for (i in 0..< n) {
			pso.findRouteWithMetrics()
			psoList.add(pso.createPSOData())
		}
		mapper.writerWithDefaultPrettyPrinter().writeValue(statisticsJson.bufferedWriter(), psoList)
	}
}
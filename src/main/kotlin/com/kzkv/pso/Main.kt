package com.kzkv.pso

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kzkv.pso.data.Obstacle
import com.kzkv.pso.data.Vector
import com.kzkv.pso.service.ParticleSwarmPathPlanner
import java.io.File
import java.nio.file.Paths


fun main() {
	val mapper = jacksonObjectMapper()

	val resourcesPath = Paths.get("src/main/resources").toString()
	val pathJson = File("$resourcesPath/path.json").bufferedWriter()
	val endpointsJson = File("$resourcesPath/endpoints.json")
	val obstaclesJson = File("$resourcesPath/obstacles.json")

	val endpoints : List<Vector> = mapper.readValue(endpointsJson)

	var obstacles : List<Obstacle> = mapper.readValue(obstaclesJson)
	if (obstacles.isEmpty()) {
		obstacles = Obstacle.createLocation(1000, endpoints)
		mapper.writerWithDefaultPrettyPrinter().writeValue(obstaclesJson, obstacles)
	}

	var path = emptyList<Vector>()
	var restartCounter = 0
	val startTime = System.currentTimeMillis()
	while (path.isEmpty()) {
		path = ParticleSwarmPathPlanner(endpoints.first(), endpoints.last(), obstacles).findOptimalPath()
		if (path.isEmpty()) {
			restartCounter++
			println("No path found")
		}
		if (restartCounter >= 100) {
			println("Bad parameters")
			break
		}
	}
	val endTime = System.currentTimeMillis()
	mapper.writerWithDefaultPrettyPrinter().writeValue(pathJson, path)
	println("Optimized Path:")
	path.forEach { println(it) }
	println("\nTotal Time: ${endTime - startTime}ms")
	println("\nNumber of restarts: $restartCounter")
}


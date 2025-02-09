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

	val obstacles : List<Obstacle> = List(500) {
		Obstacle(Vector(1.0, 1.0, 1.0), Vector(9.0, 9.0, 9.0), endpoints)
	}
	mapper.writerWithDefaultPrettyPrinter().writeValue(obstaclesJson, obstacles)

	val startTime = System.currentTimeMillis()
	val path = ParticleSwarmPathPlanner(endpoints.first(), endpoints.last(), obstacles).findOptimalPath()
	val endTime = System.currentTimeMillis()

	if (path.isEmpty()) {
		println("No path found")
	} else {
		mapper.writerWithDefaultPrettyPrinter().writeValue(pathJson, path)
		println("Optimized Path:")
		path.forEach { println(it) }
		println("\nTotal Time: ${endTime - startTime}ms")
	}
}


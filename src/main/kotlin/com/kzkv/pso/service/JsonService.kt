package com.kzkv.pso.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kzkv.pso.data.Statistic
import com.kzkv.pso.entity.Obstacle
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths

@Service
class JsonService {
	private val mapper = jacksonObjectMapper()
	private val resourcesPath = Paths.get("src/main/resources").toString()
	private val obstaclesJson = File("$resourcesPath/obstacles.json")
	private val statisticsJson = File("$resourcesPath/statistics.json")

	fun readObstacles() : List<Obstacle> {
		return mapper.readValue(obstaclesJson)
	}

	fun writeObstacles(obstacles: List<Obstacle>) : List<Obstacle> {
		mapper.writerWithDefaultPrettyPrinter().writeValue(obstaclesJson.bufferedWriter(), obstacles)
		return obstacles
	}

	fun writeStatistic(statistics: List<Statistic>) : List<Statistic> {
		mapper.writerWithDefaultPrettyPrinter().writeValue(statisticsJson.bufferedWriter(), statistics)
		return statistics
	}

}
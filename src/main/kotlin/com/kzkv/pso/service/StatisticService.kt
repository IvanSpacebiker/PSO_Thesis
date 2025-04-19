package com.kzkv.pso.service

import com.kzkv.pso.data.Statistic
import com.kzkv.pso.data.Vector
import org.springframework.stereotype.Service

@Service
class StatisticService(private val jsonService: JsonService) {

	private val statistics : ArrayList<Statistic> = arrayListOf()
	private var iterationIndex = 0

	fun addStats(startTime: Long, endTime: Long, route: List<Vector>): Statistic {
		val calcTime = endTime - startTime
		var pathLength = 0.0
		for (i in 0..route.size - 2) {
			pathLength += (route[i] - route[i+1]).length()
		}
		val stats = Statistic(iterationIndex, startTime, calcTime, pathLength)
		statistics.add(stats)
		iterationIndex++
		return stats
	}

	fun clearStats() {
		iterationIndex = 0
		statistics.clear()
	}

	fun writeStatistic() : List<Statistic> {
		jsonService.writeStatistic(statistics)
		return statistics
	}

}

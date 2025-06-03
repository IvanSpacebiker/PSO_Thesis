package com.kzkv.pso.service

import com.kzkv.pso.data.Statistic
import com.kzkv.pso.data.Vector
import org.springframework.stereotype.Service
import java.util.concurrent.Executors

@Service
class StatisticService(private val jsonService: JsonService) {

	private val statistics : ArrayList<Statistic> = arrayListOf()
	private var iterationIndex = 0
	private val executor = Executors.newSingleThreadExecutor()

	fun addStats(startTime: Long, endTime: Long, route: List<Vector>, obstacleDensity: Double): Statistic {
		val calcTime = endTime - startTime
		var pathLength = 0.0
		for (i in 0..route.size - 2) {
			pathLength += (route[i] - route[i+1]).length()
		}
		val stats = Statistic(iterationIndex, startTime, calcTime, pathLength, obstacleDensity)
		synchronized(statistics) {
			statistics.add(stats)
		}
		iterationIndex++
		return stats
	}

	fun clearStats() {
		iterationIndex = 0
		synchronized(statistics) {
			statistics.clear()
		}
	}

	fun writeStatistic(): List<Statistic> {
		val snapshot: List<Statistic>
		synchronized(statistics) {
			snapshot = statistics.toList()
		}
		executor.submit {
			jsonService.writeStatistic(snapshot)
		}
		return snapshot
	}
}

package com.kzkv.pso.entity

class PDController(private val kp: Double, private val kd: Double) {
    private var previousError = 0.0
    
    fun compute(error: Double, dt: Double): Double {
        val derivative = (error - previousError) / dt
        previousError = error
        return kp * error + kd * derivative
    }
}
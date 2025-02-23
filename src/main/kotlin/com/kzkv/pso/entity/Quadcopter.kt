package com.kzkv.pso.entity

import com.kzkv.pso.data.State
import com.kzkv.pso.data.Vector
import kotlin.math.cos
import kotlin.math.sin

class Quadcopter(val radius: Double, private val gravity: Double, private val mass: Double) {
    var state = State()
    var target = Vector()
    private val positionPD_X = PDController(0.5, 1.0)
    private val positionPD_Y = PDController(0.5, 0.75)
    private val altitudePD = PDController(0.5, 0.75)
    private val rollPD = PDController(3.0, 0.55)
    private val pitchPD = PDController(3.0, 0.55)
    private val yawPD = PDController(3.0, 0.65)

    fun update(dt: Double) {
        val fx = positionPD_X.compute(target.x - state.x, dt)
        val fy = positionPD_Y.compute(target.y - state.y, dt)
        val fz = altitudePD.compute(target.z - state.z, dt) + mass * gravity

        val rollControl = rollPD.compute(0.0 - state.roll, dt)
        val pitchControl = pitchPD.compute(0.0 - state.pitch, dt)
        val yawControl = yawPD.compute(0.0 - state.yaw, dt)

        state.rollRate += rollControl * dt
        state.pitchRate += pitchControl * dt
        state.yawRate += yawControl * dt

        state.roll += state.rollRate * dt
        state.pitch += state.pitchRate * dt
        state.yaw += state.yawRate * dt

        val cosYaw = cos(state.yaw)
        val sinYaw = sin(state.yaw)

        state.vx += (fx * cosYaw - fy * sinYaw) / mass * dt
        state.vy += (fx * sinYaw + fy * cosYaw) / mass * dt
        state.vz += (fz - mass * gravity) / mass * dt

        state.x += state.vx * dt
        state.y += state.vy * dt
        state.z += state.vz * dt
    }

}
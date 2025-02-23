package com.kzkv.pso.data

data class State(
	var x: Double,
	var y: Double,
	var z: Double,
	var vx: Double,
	var vy: Double,
	var vz: Double,
	var roll: Double,
	var pitch: Double,
	var yaw: Double,
	var rollRate: Double,
	var pitchRate: Double,
	var yawRate: Double
) {
	constructor() : this (0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
}
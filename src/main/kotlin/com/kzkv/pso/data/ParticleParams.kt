package com.kzkv.pso.data

data class ParticleParams(
	val w : Double,
	val alpha : Double,
	val c1 : Double,
	val c2 : Double,
	val numberOfParticles : Int,
	val numberOfIterations : Int,
	val endpoints : ArrayList<Vector>,
	val radius : Double
)
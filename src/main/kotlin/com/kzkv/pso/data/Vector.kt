package com.kzkv.pso.data

import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

data class Vector(val x: Double, val y: Double, val z: Double) {
	constructor() : this(0.0, 0.0, 0.0)
	constructor(from: Double, until: Double) : this(
		Random.nextDouble(from, until),
		Random.nextDouble(from, until),
		Random.nextDouble(from, until),
	)
	constructor(v1: Vector, v2: Vector) : this(
		Random.nextDouble(v1.x, v2.x),
		Random.nextDouble(v1.y, v2.y),
		Random.nextDouble(v1.z, v2.z),
	)

	companion object {
		fun getDistance(o1: Vector, o2: Vector) : Double {
			return sqrt((o1.x - o2.x).pow(2.0) + (o1.y - o2.y).pow(2.0) + (o1.z - o2.z).pow(2.0))
		}

		fun scalar(o1: Vector, o2: Vector) : Double {
			return o1.x * o2.x + o1.y * o2.y + o1.z * o2.z
		}

		fun vektor(o1: Vector, o2: Vector) : Vector {
			return Vector(
				o1.y * o2.z - o1.z * o2.y,
				o1.z * o2.x - o1.x * o2.z,
				o1.x * o2.y - o1.y * o2.x
			)
		}

		fun random() : Vector {
			return Vector(-1.0, 1.0)
		}

		fun angleBetween(v1: Vector, v2: Vector): Double {
			val dotProduct = scalar(v1, v2)
			val lengths = v1.length() * v2.length()

			return if (lengths != 0.0) acos(dotProduct / lengths) else 0.0
		}
	}

	fun length() : Double {
		return sqrt(x.pow(2.0) + y.pow(2.0) + z.pow(2.0))
	}

	fun normalized(): Vector {
		val length = this.length()
		return if (length != 0.0) Vector(x / length, y / length, z / length) else Vector(0.0, 0.0, 0.0)
	}


	operator fun plus(other: Vector) : Vector {
		return Vector(x + other.x, y + other.y, z + other.z)
	}

	operator fun minus(other: Vector) : Vector {
		return Vector(x - other.x, y - other.y, z - other.z)
	}

	operator fun times(number: Double) : Vector {
		return Vector(x * number, y * number, z * number)
	}
}

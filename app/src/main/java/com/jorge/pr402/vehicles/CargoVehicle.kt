package com.jorge.pr402.vehicles

/**
 * Clase abstracta que representa un vehículo de carga, hereda de la clase [Vehicle].
 *
 * @property maxLoad Carga máxima que puede transportar el vehículo de carga.
 */
abstract class CargoVehicle(
    wheels: Int, engine: String, seats: Int, color: String, model: String,
    val maxLoad: Int
) : Vehicle(wheels, engine, seats, color, model)
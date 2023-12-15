package com.jorge.pr402.vehicles

/**
 * Clase que representa una furgoneta, hereda de la clase [CargoVehicle].
 * Si recibe más de 6 ruedas [wheels] lo establece a 6.
 */
class Van(
    wheels: Int, engine: String, seats: Int, color: String, model: String, maxLoad: Int
) : CargoVehicle(if (wheels > 6) 6 else wheels, engine, seats, color, model, maxLoad)
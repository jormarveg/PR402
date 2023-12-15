package com.jorge.pr402.vehicles

/**
 * Clase que representa un tráiler, hereda de la clase [CargoVehicle].
 * Si recibe menos de 6 ruedas [wheels] lo establece a 6.
 */
class Trailer(
    wheels: Int, engine: String, seats: Int, color: String, model: String, maxLoad: Int
) : CargoVehicle(if (wheels < 6) 6 else wheels, engine, seats, color, model, maxLoad)
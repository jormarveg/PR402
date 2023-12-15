package com.jorge.pr402.vehicles

/**
 * Clase que representa una moto, hereda de la clase [Vehicle].
 * Si recibe más de dos asientos [seats] lo establece a 2.
 */
class Motorbike(wheels: Int, engine: String, seats: Int, color: String, model: String) :
    Vehicle(wheels, engine, if (seats > 2) 2 else seats, color, model)
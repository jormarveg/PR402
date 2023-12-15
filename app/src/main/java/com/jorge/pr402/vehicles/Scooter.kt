package com.jorge.pr402.vehicles

/**
 * Clase que representa un patinete.
 * No tiene asientos y por tanto no recibe [seats].
 * Llama al constructor padre [Vehicle] con 0 asientos.
 */
class Scooter(wheels: Int, engine: String, color: String, model: String) :
    Vehicle(wheels, engine, 0, color, model)
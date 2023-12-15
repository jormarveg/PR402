package com.jorge.pr402.vehicles

/**
 * Clase abstracta que representa un vehículo genérico.
 * Las propiedades tienen los getter public para poder mostrarlos.
 *
 * @property wheels Número de ruedas del vehículo.
 * @property engine Tipo de motor del vehículo.
 * @property seats Número de asientos del vehículo.
 * @property color Color del vehículo.
 * @property model Modelo del vehículo.
 */
abstract class Vehicle(
    val wheels: Int, val engine: String, val seats: Int,
    val color: String, val model: String
)
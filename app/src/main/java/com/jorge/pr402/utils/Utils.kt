package com.jorge.pr402.utils

import com.jorge.pr402.vehicles.Car
import com.jorge.pr402.vehicles.Motorbike
import com.jorge.pr402.vehicles.Scooter
import com.jorge.pr402.vehicles.Trailer
import com.jorge.pr402.vehicles.Van
import com.jorge.pr402.vehicles.Vehicle
import java.lang.NumberFormatException

/**
 * Comprueba si el [text] proporcionado es una cantidad válida.
 * @param onValid Lambda que se ejecutará si la conversión es exitosa, pasando el valor entero.
 * @param onError Lambda que se ejecutará si hay un error, pasando un mensaje descriptivo.
 */
fun checkIfValidQuantity(
    text: String, onValid: (Int) -> Unit,
    onError: (String) -> Unit
) {
    try {
        val intValue = text.toInt()
        if (intValue > 0) onValid(intValue)
        else onError("Debe ser un número mayor a 0.")
    } catch (e: NumberFormatException) {
        onError("Solo puedes introducir números enteros.")
    }
}

/**
 * Añade un [vehicle] al array de [vehicles] en el primer lugar en
 * el que haya un null.
 *
 * @return `true` si se añadió con éxito, `false` si no hay espacio disponible.
 */
fun addVehicle(vehicles: Array<Vehicle?>, vehicle: Vehicle): Boolean {
    for (i in vehicles.indices) {
        if (vehicles[i] == null) {
            vehicles[i] = vehicle
            return true
        }
    }
    return false // no hay espacio
}

/**
 * Elimina un vehículo del array de [vehicles] en la posición [index].
 * @return Vehículo eliminado o `null` si el índice no es válido.
 */
fun removeVehicle(vehicles: Array<Vehicle?>, index: Int): Vehicle? {
    return if (index in vehicles.indices) {
        val vehicle = vehicles[index]
        vehicles[index] = null
        vehicle
    } else {
        null // índice no válido
    }
}

/**
 * Cuenta y devuelve la cantidad de elementos no nulos en el array de [vehicles].
 */
fun countNotNullVehicles(vehicles: Array<Vehicle?>): Int {
    var count = 0
    for (i in vehicles.indices) {
        if (vehicles[i] != null) count++
    }
    return count
}

/**
 * Devuelve un nuevo array ordenado alfabéticamente con el método de burbuja,
 * según su modelo, sin elementos nulos.
 */
fun sortedArray(vehicles: Array<Vehicle?>): Array<Vehicle> {
    val sorted = copyArrayWithoutNulls(vehicles)
    for (i in sorted.indices)
        for (j in 0..sorted.lastIndex - 1 - i) {
            if (sorted[j].model.compareTo(sorted[j + 1].model, ignoreCase = true) > 0) {
                val aux = sorted[j]
                sorted[j] = sorted[j + 1]
                sorted[j + 1] = aux
            }
        }
    return sorted
}

/**
 * Copia el array de [vehicles] sin los elementos nulos.
 *
 * @return Nuevo array sin elementos nulos.
 */
fun copyArrayWithoutNulls(vehicles: Array<Vehicle?>): Array<Vehicle> {
    val quantity = countNotNullVehicles(vehicles)
    val copy = arrayOfNulls<Vehicle>(quantity)
    var copyIndex = 0
    for (v in vehicles) {
        if (v != null) copy[copyIndex++] = v
    }
    return copy.requireNoNulls()
}

/**
 * Devuelve una cadena con la cantidad de vehículos por
 * tipo en el array de [vehicles].
 */
fun getVehiclesByType(vehicles: Array<Vehicle?>): String {
    var cars = 0
    var motorbikes = 0
    var scooters = 0
    var trailers = 0
    var vans = 0
    for (v in vehicles) {
        if (v != null) {
            when (v) {
                is Car -> cars++
                is Motorbike -> motorbikes++
                is Scooter -> scooters++
                is Trailer -> trailers++
                is Van -> vans++
            }
        }
    }
    return """
         · Coches: $cars
         · Motos: $motorbikes
         · Patinetes: $scooters
         · Tráileres: $trailers
         · Furgonetas: $vans
        """.trimIndent()
}
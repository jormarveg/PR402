package com.jorge.pr402.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jorge.pr402.utils.countNotNullVehicles
import com.jorge.pr402.utils.getVehiclesByType
import com.jorge.pr402.utils.removeVehicle
import com.jorge.pr402.utils.sortedArray
import com.jorge.pr402.vehicles.Car
import com.jorge.pr402.vehicles.CargoVehicle
import com.jorge.pr402.vehicles.Motorbike
import com.jorge.pr402.vehicles.Scooter
import com.jorge.pr402.vehicles.Trailer
import com.jorge.pr402.vehicles.Van
import com.jorge.pr402.vehicles.Vehicle

/**
 * Pantalla que muestra una lista de vehículos.
 *
 * @param vehiclesArray Array de vehículos que se mostrará en la lista.
 * @param showSnackbar Lambda para mostrar una snackbar.
 * @param modifier Modificador para personalizar la apariencia de la pantalla.
 */
@Composable
fun VehicleListScreen(
    vehiclesArray: Array<Vehicle?>, showSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        var showVehicleTypesDialog by remember { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            val quantity = countNotNullVehicles(vehiclesArray)
            Text(
                text = "Vehículos ($quantity de ${vehiclesArray.size})",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge
            )
            Button(onClick = {
                showVehicleTypesDialog = true
            }) {
                Text("Vehículos por tipo")
            }
        }
        // boolean para forzar la actualización de la lista en pantalla,
        // porque compose no observa los cambios en arrays
        var triggerUpdate by remember { mutableStateOf(false) }
        // le decimos que cuando cambie el valor de triggerUpdate fuerce la recomposición
        key(triggerUpdate) {
            VehicleList(sortedArray(vehiclesArray),
                onVehicleDeleted = { delete, index ->
                    if (delete) {
                        val model = vehiclesArray[index]!!.model
                        removeVehicle(vehiclesArray, index)
                        triggerUpdate = !triggerUpdate
                        showSnackbar("Vehículo \"$model\" eliminado")
                    } else showSnackbar("Mantén pulsado el icono para eliminar el vehículo")
                })
        }
        if (showVehicleTypesDialog)
            InfoDialog(title = "Vehículos por tipo", text = getVehiclesByType(vehiclesArray)) {
                showVehicleTypesDialog = false
            }
    }
}

/**
 * Lista de [vehicles] con LazyColumn.
 *
 * @param vehicles Lista de vehículos que se mostrará.
 * @param onVehicleDeleted Lambda que se ejecutará cuando se pulse en el icono
 *                         de eliminar un vehículo.
 * @param modifier Modificador para personalizar la apariencia de la lista.
 */
@Composable
fun VehicleList(
    vehicles: Array<Vehicle>,
    onVehicleDeleted: (Boolean, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(vehicles) { index, v ->
            VehicleItem(v, onDeleteClick = { delete ->
                onVehicleDeleted(delete, index)
            })
        }
    }
}

/**
 * Elemento individual de la lista de vehículos.
 *
 * @param vehicle Vehículo que se mostrará en el elemento.
 * @param onDeleteClick Lambda que se ejecutará cuando se haga clic en el icono de eliminar,
 *                      recibe un boolean indicando si se ha mantenido pulsado o no.
 * @param modifier Modificador opcional.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VehicleItem(vehicle: Vehicle, onDeleteClick: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            val vehicleType = when (vehicle) {
                is Car -> "Coche"
                is Motorbike -> "Moto"
                is Scooter -> "Patinete"
                is Trailer -> "Tráiler"
                is Van -> "Furgoneta"
                else -> "" // nunca, pero es obligatorio
            }
            Row {
                Text(
                    vehicleType, style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    Icons.Default.Delete, contentDescription = "Eliminar",
                    modifier = Modifier
                        .combinedClickable(
                            onClick = { onDeleteClick(false) },
                            onLongClick = { onDeleteClick(true) },
                        )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Column(modifier = Modifier.weight(1.2f)) {
                    TextWithTitle("Modelo:", " ${vehicle.model}")
                    TextWithTitle("Ruedas:", " ${vehicle.wheels}")
                    TextWithTitle("Motor:", " ${vehicle.engine}")
                }
                Column(modifier = Modifier.weight(0.8f)) {
                    TextWithTitle("Asientos:", " ${vehicle.seats}")
                    TextWithTitle("Color:", " ${vehicle.color}")
                    if (vehicle is CargoVehicle)  // comprueba si es instancia de CargoVehicle
                        TextWithTitle("Carga máxima:", " ${vehicle.maxLoad}")
                }
            }
        }
    }
}

/**
 * Función de utilidad para mostrar texto [text] con título [title] en negrita.
 */
@Composable
fun TextWithTitle(title: String, text: String) {
    Text(buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(title)
        }
        append(text)
    })
}

/**
 * FAB para añadir un vehículo a la lista.
 *
 * @param modifier Modificador opcional.
 * @param onClick Lambda que se ejecutará al hacer clic en el botón.
 */
@Composable
fun FABAdd(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FloatingActionButton(modifier = modifier, onClick = onClick) {
        Icon(Icons.Default.Add, "Añadir nueva nota")
    }
}
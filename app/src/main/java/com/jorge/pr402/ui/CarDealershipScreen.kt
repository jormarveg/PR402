package com.jorge.pr402.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jorge.pr402.utils.addVehicle
import com.jorge.pr402.utils.countNotNullVehicles
import com.jorge.pr402.vehicles.Car
import com.jorge.pr402.vehicles.Motorbike
import com.jorge.pr402.vehicles.Scooter
import com.jorge.pr402.vehicles.Trailer
import com.jorge.pr402.vehicles.Van
import com.jorge.pr402.vehicles.Vehicle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Pantalla principal de la aplicación.
 * Dependiendo de si el array de vehículos ha sido
 * inicializado muestra un contenido u otro.
 *
 * @param title Título de la barra superior.
 * @param modifier Modificador opcional.
 */
@Composable
fun CarDealershipScreen(title: String, modifier: Modifier = Modifier) {
    // State necesario para gestionar la snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    // CoroutineScope para lanzar tareas asíncronas, en este caso, mostrar snackbar
    val scope = rememberCoroutineScope()
    fun snackbar(msg: String) = showSnackbar(snackbarHostState, scope, msg)
    var showNewVehicleDialog by remember { mutableStateOf(false) }
    var vehiclesQuantity by remember { mutableIntStateOf(0) }
    // cuando vehiclesQuantity cambia, vehiclesArray se inicializa
    val vehiclesArray by remember(vehiclesQuantity) {
        mutableStateOf(
            if (vehiclesQuantity > 0)
                arrayOfNulls<Vehicle>(vehiclesQuantity)  // array de vehículos
            else null
        )
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        // indicamos al Scaffold que se va a mostrar una snackbar
        // Scaffold se encarga de colocarla donde corresponda (abajo)
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // lo mismo con la barra superior
        topBar = { TopBar(title = title) },
        floatingActionButton = {
            if (vehiclesArray != null)
                // solo muestra el FAB si se ha inicializado el array
                FABAdd {
                    if (countNotNullVehicles(vehiclesArray!!) >= vehiclesQuantity)
                        snackbar("No puedes insertar más vehículos")
                    else
                        showNewVehicleDialog = true
                }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues -> // Scaffold calcula y devuelve los paddings
        MainLayout(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            vehiclesArray = vehiclesArray,
            onQuantitySet = { vehiclesQuantity = it },
            showSnackbar = ::snackbar
        )
        if (showNewVehicleDialog) {
            var errorText by remember { mutableStateOf("") }
            NewVehicleDialog(
                onDismiss = { showNewVehicleDialog = false },
                onConfirm = { type, model, wheels, engine, seats, color, maxLoad ->
                    val emptyFields =
                        model.trim().isEmpty() || wheels == 0 || engine.trim().isEmpty() ||
                                color.trim().isEmpty() || (type != "Patinete" && seats == 0) ||
                                ((type == "Tráiler" || type == "Furgoneta") && maxLoad == 0)
                    if (emptyFields)
                        errorText = "Hay campos vacíos."
                    else {
                        fun addNewVehicle(vehicle: Vehicle) = addVehicle(vehiclesArray!!, vehicle)
                        when (type) {
                            "Coche" -> addNewVehicle(Car(wheels, engine, seats, color, model))
                            "Moto" -> addNewVehicle(Motorbike(wheels, engine, seats, color, model))
                            "Patinete" -> addNewVehicle(Scooter(wheels, engine, color, model))
                            "Tráiler" -> addNewVehicle(
                                Trailer(wheels, engine, seats, color, model, maxLoad)
                            )

                            "Furgoneta" -> addNewVehicle(
                                Van(wheels, engine, seats, color, model, maxLoad)
                            )
                        }
                        showNewVehicleDialog = false
                    }
                }, errorText = errorText
            )
        }
    }
}

/**
 * Layout principal de la aplicación.
 *
 * @param vehiclesArray Array de vehículos que se mostrará.
 * @param onQuantitySet Lambda que se ejecutará al establecer la cantidad de vehículos.
 * @param showSnackbar Lambda para mostrar una snackbar en pantalla.
 * @param modifier Modificador opcional.
 */
@Composable
fun MainLayout(
    vehiclesArray: Array<Vehicle?>?,
    onQuantitySet: (Int) -> Unit,
    showSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        if (vehiclesArray == null) {
            AskVehiclesScreen(
                modifier = Modifier.fillMaxWidth(),
                onValid = { onQuantitySet(it) },
                showSnackbar = showSnackbar
            )
        } else {
            VehicleListScreen(vehiclesArray, showSnackbar)
        }
    }
}

/**
 * Muestra una snackbar con el mensaje [msg] proporcionado.
 *
 * @param snackbarHost SnackbarHostState necesario.
 * @param scope CoroutineScope necesario.
 */
fun showSnackbar(
    snackbarHost: SnackbarHostState,
    scope: CoroutineScope,
    msg: String
) {
    // lanza tarea asíncrona
    scope.launch {
        // si ya hay una mostrándose, la descarta
        snackbarHost.currentSnackbarData?.dismiss()
        snackbarHost.showSnackbar(msg)
    }
}
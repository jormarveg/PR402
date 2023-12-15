package com.jorge.pr402.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Diálogo para añadir un nuevo vehículo.
 *
 * @param onDismiss Lambda que se ejecutará al cerrar el diálogo.
 * @param onConfirm Lambda que se ejecutará al confirmar la creación del vehículo,
 *                  pasando los datos del vehículo.
 * @param modifier Modificador opcional.
 * @param errorText Texto de error que se mostrará en caso de error.
 */
@Composable
fun NewVehicleDialog(
    onDismiss: () -> Unit, onConfirm: (String, String, Int, String, Int, String, Int) -> Unit,
    modifier: Modifier = Modifier, errorText: String = "",
) {
    val vehicleTypes = arrayOf("Coche", "Moto", "Patinete", "Tráiler", "Furgoneta")
    // ruedas, motor, asientos, color y modelo
    var selectedType by remember { mutableStateOf(vehicleTypes[0]) }
    var model by remember { mutableStateOf("") }
    var wheels by remember { mutableIntStateOf(0) }
    var engine by remember { mutableStateOf("") }
    var seats by remember { mutableIntStateOf(0) }
    var color by remember { mutableStateOf("") }
    var maxLoad by remember { mutableIntStateOf(0) }
    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = "Añadir Vehículo")
        },
        text = {
            Column(
                modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DropdownVehicles(vehicleTypes) { selected ->
                    selectedType = selected
                }
                TextField(value = model, label = { Text(text = "Modelo") },
                    onValueChange = { model = it }, singleLine = true)
                NumericField(value = if (wheels <= 0) "" else "$wheels",
                    label = "Ruedas",
                    onValueChange = { wheels = it })
                TextField(value = engine, label = { Text(text = "Motor") },
                    onValueChange = { engine = it }, singleLine = true)
                if (selectedType != vehicleTypes[2])
                    NumericField(value = if (seats <= 0) "" else "$seats",
                        label = "Asientos",
                        onValueChange = { seats = it })
                TextField(value = color, label = { Text(text = "Color") },
                    onValueChange = { color = it }, singleLine = true)
                if (selectedType == vehicleTypes[3] || selectedType == vehicleTypes[4])
                    NumericField(value = if (maxLoad <= 0) "" else "$maxLoad",
                        label = "Carga máxima",
                        onValueChange = { maxLoad = it })
                Text(text = errorText, color = Color.Red, fontWeight = FontWeight.Bold)
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(selectedType, model, wheels, engine, seats, color, maxLoad)
            }) {
                Text(text = "Crear vehículo")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        })
}

/**
 * Campo de texto numérico que usa el teclado numérico. Solo
 * permite 3 dígitos o menos.
 *
 * @param value Valor del camp.
 * @param label Etiqueta del campo.
 * @param modifier Modificador opconal.
 * @param onValueChange Lambda que se ejecutará al cambiar el valor del campo.
 */
@Composable
fun NumericField(
    value: String, label: String, modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = value, onValueChange = {
            if (it.length <= 3) {
                val intValue = it.toIntOrNull() ?: 0
                onValueChange(intValue)
            }
        },
        label = { Text(text = label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

/**
 * Desplegable para seleccionar el tipo de vehículo.
 *
 * @param options Opciones disponibles en el desplegable.
 * @param onSelectedOption Lambda que se ejecutará al seleccionar una opción.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownVehicles(
    options: Array<String>,
    onSelectedOption: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        // para evitar que se muestre el teclado al pulsar en el TextField
        CompositionLocalProvider(LocalTextInputService provides null) {
            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                label = { Text("Tipo de vehículo") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            for (option in options) {
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOptionText = option
                        expanded = false
                        onSelectedOption(option)
                    }
                )
            }
        }
    }
}
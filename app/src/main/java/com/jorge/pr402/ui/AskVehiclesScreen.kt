package com.jorge.pr402.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jorge.pr402.utils.checkIfValidQuantity

/**
 * Pantalla encargada de preguntar al usuario cuántos vehículos quiere crear
 *
 * @param onValid Lambda que se ejecutará con la cantidad válida de vehículos a crear.
 * @param showSnackbar Lambda para mostrar snackbar, en este caso, cuando se borra un elemento.
 * @param modifier Modificador opcional para aplicar a la pantalla.
 */
@Composable
fun AskVehiclesScreen(
    onValid: (quantity: Int) -> Unit,
    showSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var vehiclesQuantity by remember { mutableStateOf("") }
        Text(text = "Introduce la cantidad de vehículos a crear:")
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.width(96.dp),
                value = vehiclesQuantity, onValueChange = { vehiclesQuantity = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                checkIfValidQuantity(vehiclesQuantity,
                    onValid = onValid,
                    onError = { showSnackbar(it) })
            }) {
                Text(text = "Aceptar")
            }
        }
    }
}
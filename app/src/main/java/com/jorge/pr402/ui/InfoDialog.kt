package com.jorge.pr402.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Diálogo que muestra información [text] con un título[title].
 * Al pulsar en Cerrar se ejecutará [onConfirm]
 */
@Composable
fun InfoDialog(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = title) },
        text = { Text(text = text) },
        onDismissRequest = onConfirm,
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Cerrar")
            }
        }
    )
}
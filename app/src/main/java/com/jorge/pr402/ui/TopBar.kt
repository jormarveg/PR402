package com.jorge.pr402.ui

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * TopBar es la barra superior de la aplicación.
 * Implementa CenterAlignedTopAppBar que es una barra superior con
 * título centrado.
 *
 * @param title Título de la barra superior.
 * @param modifier Modificador para aplicar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(text = title) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
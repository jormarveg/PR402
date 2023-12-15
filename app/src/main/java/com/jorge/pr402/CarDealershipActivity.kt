package com.jorge.pr402

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jorge.pr402.ui.CarDealershipScreen
import com.jorge.pr402.ui.theme.PR402Theme

const val TITLE = "Concesionario"

class CarDealershipActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 'dynamicColor = false' fuerza a respetar el tema
            PR402Theme(darkTheme = false, dynamicColor = false) {
                CarDealershipScreen(TITLE)
            }
        }
    }
}
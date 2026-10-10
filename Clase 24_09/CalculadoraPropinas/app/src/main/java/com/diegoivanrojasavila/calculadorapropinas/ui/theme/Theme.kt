package com.diegoivanrojasavila.calculadorapropinas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = VerdeOscuro,
    onPrimary = Color.White,
    primaryContainer = VerdeClaro,
    secondary = AzulPetroleo,
    secondaryContainer = AzulPetroleoClaro,
    tertiary = Dorado,
    tertiaryContainer = DoradoClaro,
    background = FondoClaro,
    surface = FondoClaro
)

private val EsquemaOscuro = darkColorScheme(
    primary = VerdeNoche,
    primaryContainer = VerdeNocheContenedor,
    secondary = AzulNoche,
    secondaryContainer = AzulNocheContenedor,
    tertiary = DoradoNoche,
    tertiaryContainer = DoradoNocheContenedor,
    background = FondoOscuro,
    surface = FondoOscuro
)

@Composable
fun CalculadoraPropinasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}

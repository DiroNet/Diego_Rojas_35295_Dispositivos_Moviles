package com.diegoivanrojasavila.lanzadordados.ui.tema

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// esquema de colores claro
private val EsquemaClaro = lightColorScheme(
    primary = AzulTurquesa,
    onPrimary = Color.White,
    primaryContainer = AzulTurquesaClaro,
    background = FondoBlanco,
    surface = FondoBlanco,
    onBackground = TextoOscuro,
    onSurface = TextoOscuro
)

// esquema de colores oscuro
private val EsquemaOscuro = darkColorScheme(
    primary = AzulTurquesaNoche,
    onPrimary = Color(0xFF003548),
    background = FondoNoche,
    surface = FondoNoche,
    onBackground = TextoClaro,
    onSurface = TextoClaro
)

// composable del tema principal
@Composable
fun TemaLanzadorDados(
    temaOscuro: Boolean = isSystemInDarkTheme(),
    contenido: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (temaOscuro) EsquemaOscuro else EsquemaClaro,
        typography = Tipografia,
        content = contenido
    )
}

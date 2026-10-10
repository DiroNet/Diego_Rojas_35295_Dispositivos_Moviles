package com.diegoivanrojasavila.calculadoranomina.logica

import java.io.Serializable

// Serializable para que el resultado no se pierda al girar la pantalla
data class ResultadoNomina(
    val valorHora: Double,
    val salarioBasico: Double,
    val totalHorasExtra: Double,
    val auxilioTransporte: Double,
    val totalDevengado: Double,
    val aporteSalud: Double,
    val aportePension: Double,
    val fondoSolidaridad: Double,
    val totalDeducciones: Double,
    val salarioNeto: Double,
    val rango: RangoSalarial
) : Serializable

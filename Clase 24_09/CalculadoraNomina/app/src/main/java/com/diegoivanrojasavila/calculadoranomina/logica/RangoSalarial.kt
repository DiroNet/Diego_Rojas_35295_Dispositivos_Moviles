package com.diegoivanrojasavila.calculadoranomina.logica

enum class RangoSalarial { RANGO_1, RANGO_2, RANGO_3 }

fun clasificarRango(salarioBasico: Double): RangoSalarial = when {
    salarioBasico <= TOPE_AUXILIO -> RangoSalarial.RANGO_1
    salarioBasico < TOPE_SOLIDARIDAD -> RangoSalarial.RANGO_2
    else -> RangoSalarial.RANGO_3
}

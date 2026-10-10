package com.diegoivanrojasavila.calculadoranomina.ui

import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

private val formatoPesos = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
    maximumFractionDigits = 0
    roundingMode = RoundingMode.HALF_UP
}

fun formatearPesos(valor: Double): String = formatoPesos.format(valor)

fun formatearSmmlv(valor: Double): String = String.format(Locale("es", "CO"), "%.2f", valor)

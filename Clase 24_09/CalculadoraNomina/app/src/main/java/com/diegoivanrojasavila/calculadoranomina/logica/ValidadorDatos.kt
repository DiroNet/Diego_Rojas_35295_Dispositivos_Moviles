package com.diegoivanrojasavila.calculadoranomina.logica

enum class ErrorValidacion {
    SALARIO_INVALIDO,
    SALARIO_BAJO_MINIMO,
    DIURNAS_INVALIDAS,
    NOCTURNAS_INVALIDAS,
    EXCESO_HORAS
}

sealed interface ResultadoValidacion {
    data class Valido(
        val salarioBasico: Double,
        val horasDiurnas: Double,
        val horasNocturnas: Double
    ) : ResultadoValidacion

    data class Invalido(val error: ErrorValidacion) : ResultadoValidacion
}

// si el campo esta vacio vale 0, si no es numero o es negativo da null
private fun convertirHoras(texto: String): Double? {
    if (texto.isBlank()) return 0.0
    val horas = texto.trim().toDoubleOrNull() ?: return null
    return if (horas.isFinite() && horas >= 0) horas else null
}

fun validarDatos(
    salarioTexto: String,
    diurnasTexto: String,
    nocturnasTexto: String
): ResultadoValidacion {
    val salario = salarioTexto.trim().toDoubleOrNull()
        ?.takeIf { it.isFinite() }
        ?: return ResultadoValidacion.Invalido(ErrorValidacion.SALARIO_INVALIDO)

    if (salario < SMMLV) return ResultadoValidacion.Invalido(ErrorValidacion.SALARIO_BAJO_MINIMO)

    val diurnas = convertirHoras(diurnasTexto)
        ?: return ResultadoValidacion.Invalido(ErrorValidacion.DIURNAS_INVALIDAS)
    val nocturnas = convertirHoras(nocturnasTexto)
        ?: return ResultadoValidacion.Invalido(ErrorValidacion.NOCTURNAS_INVALIDAS)

    if (diurnas + nocturnas > MAX_HORAS_EXTRA) {
        return ResultadoValidacion.Invalido(ErrorValidacion.EXCESO_HORAS)
    }

    return ResultadoValidacion.Valido(salario, diurnas, nocturnas)
}

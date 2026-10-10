package com.diegoivanrojasavila.calculadoranomina.logica

fun calcularNomina(
    salarioBasico: Double,
    horasDiurnas: Double,
    horasNocturnas: Double,
    esDominical: Boolean,
    transporteEmpresa: Boolean
): ResultadoNomina {
    // regla 1
    val valorHora = salarioBasico / HORAS_MENSUALES

    // regla 2
    val factorDiurno = if (esDominical) FACTOR_DIURNA_DOMINICAL else FACTOR_DIURNA
    val factorNocturno = if (esDominical) FACTOR_NOCTURNA_DOMINICAL else FACTOR_NOCTURNA
    val pagoExtrasDiurnas = horasDiurnas * valorHora * factorDiurno
    val pagoExtrasNocturnas = horasNocturnas * valorHora * factorNocturno
    val totalHorasExtra = pagoExtrasDiurnas + pagoExtrasNocturnas

    // regla 3, el auxilio no entra al ibc
    val ibc = salarioBasico + totalHorasExtra

    // regla 4
    val auxilioTransporte =
        if (salarioBasico <= TOPE_AUXILIO && !transporteEmpresa) AUXILIO_TRANSPORTE else 0.0

    // regla 5
    val totalDevengado = ibc + auxilioTransporte

    // regla 6
    val aporteSalud = ibc * PORCENTAJE_SALUD
    val aportePension = ibc * PORCENTAJE_PENSION
    val fondoSolidaridad = if (ibc >= TOPE_SOLIDARIDAD) ibc * PORCENTAJE_SOLIDARIDAD else 0.0
    val totalDeducciones = aporteSalud + aportePension + fondoSolidaridad

    // regla 7
    val salarioNeto = totalDevengado - totalDeducciones

    return ResultadoNomina(
        valorHora = valorHora,
        salarioBasico = salarioBasico,
        totalHorasExtra = totalHorasExtra,
        auxilioTransporte = auxilioTransporte,
        totalDevengado = totalDevengado,
        aporteSalud = aporteSalud,
        aportePension = aportePension,
        fondoSolidaridad = fondoSolidaridad,
        totalDeducciones = totalDeducciones,
        salarioNeto = salarioNeto,
        rango = clasificarRango(salarioBasico)
    )
}

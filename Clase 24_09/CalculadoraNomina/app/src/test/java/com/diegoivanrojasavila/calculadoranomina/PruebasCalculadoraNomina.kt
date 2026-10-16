package com.diegoivanrojasavila.calculadoranomina

import com.diegoivanrojasavila.calculadoranomina.logica.ErrorValidacion
import com.diegoivanrojasavila.calculadoranomina.logica.RangoSalarial
import com.diegoivanrojasavila.calculadoranomina.logica.ResultadoValidacion
import com.diegoivanrojasavila.calculadoranomina.logica.calcularNomina
import com.diegoivanrojasavila.calculadoranomina.logica.validarDatos
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PruebasCalculadoraNomina {

    private val margen = 1.0

    @Test
    fun casoA_salarioMinimoDiaHabil() {
        val r = calcularNomina(1_750_905.0, 10.0, 4.0, esDominical = false, transporteEmpresa = false)
        assertEquals(8_338.0, r.valorHora, margen)
        assertEquals(162_584.0, r.totalHorasExtra, margen)
        assertEquals(249_095.0, r.auxilioTransporte, margen)
        assertEquals(2_162_584.0, r.totalDevengado, margen)
        assertEquals(76_540.0, r.aporteSalud, margen)
        assertEquals(76_540.0, r.aportePension, margen)
        assertEquals(0.0, r.fondoSolidaridad, margen)
        assertEquals(2_009_505.0, r.salarioNeto, margen)
        assertEquals(RangoSalarial.RANGO_1, r.rango)
    }

    @Test
    fun casoB_dominical() {
        val r = calcularNomina(2_500_000.0, 6.0, 2.0, esDominical = true, transporteEmpresa = false)
        assertEquals(11_905.0, r.valorHora, margen)
        assertEquals(216_667.0, r.totalHorasExtra, margen)
        assertEquals(249_095.0, r.auxilioTransporte, margen)
        assertEquals(2_965_762.0, r.totalDevengado, margen)
        assertEquals(108_667.0, r.aporteSalud, margen)
        assertEquals(108_667.0, r.aportePension, margen)
        assertEquals(0.0, r.fondoSolidaridad, margen)
        assertEquals(2_748_428.0, r.salarioNeto, margen)
        assertEquals(RangoSalarial.RANGO_1, r.rango)
    }

    @Test
    fun casoC_transporteEmpresa() {
        val r = calcularNomina(3_000_000.0, 5.0, 0.0, esDominical = false, transporteEmpresa = true)
        assertEquals(14_286.0, r.valorHora, margen)
        assertEquals(89_286.0, r.totalHorasExtra, margen)
        assertEquals(0.0, r.auxilioTransporte, margen)
        assertEquals(3_089_286.0, r.totalDevengado, margen)
        assertEquals(123_571.0, r.aporteSalud, margen)
        assertEquals(123_571.0, r.aportePension, margen)
        assertEquals(0.0, r.fondoSolidaridad, margen)
        assertEquals(2_842_143.0, r.salarioNeto, margen)
        assertEquals(RangoSalarial.RANGO_1, r.rango)
    }

    @Test
    fun casoD_fondoSolidaridad() {
        val r = calcularNomina(8_000_000.0, 0.0, 0.0, esDominical = false, transporteEmpresa = false)
        assertEquals(38_095.0, r.valorHora, margen)
        assertEquals(0.0, r.totalHorasExtra, margen)
        assertEquals(0.0, r.auxilioTransporte, margen)
        assertEquals(8_000_000.0, r.totalDevengado, margen)
        assertEquals(320_000.0, r.aporteSalud, margen)
        assertEquals(320_000.0, r.aportePension, margen)
        assertEquals(80_000.0, r.fondoSolidaridad, margen)
        assertEquals(7_280_000.0, r.salarioNeto, margen)
        assertEquals(RangoSalarial.RANGO_3, r.rango)
    }

    @Test
    fun casoE_validaciones() {
        assertEquals(
            ResultadoValidacion.Invalido(ErrorValidacion.SALARIO_INVALIDO),
            validarDatos("", "", "")
        )
        assertEquals(
            ResultadoValidacion.Invalido(ErrorValidacion.SALARIO_BAJO_MINIMO),
            validarDatos("1000000", "", "")
        )
        assertEquals(
            ResultadoValidacion.Invalido(ErrorValidacion.EXCESO_HORAS),
            validarDatos("2000000", "30", "20")
        )
        assertEquals(
            ResultadoValidacion.Invalido(ErrorValidacion.DIURNAS_INVALIDAS),
            validarDatos("2000000", "-3", "")
        )
        assertTrue(validarDatos("2000000", "", "") is ResultadoValidacion.Valido)
    }
}

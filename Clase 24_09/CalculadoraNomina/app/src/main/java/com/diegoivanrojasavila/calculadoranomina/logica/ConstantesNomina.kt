package com.diegoivanrojasavila.calculadoranomina.logica

// salario minimo 2026 - Decreto 1469 de 2025
const val SMMLV = 1_750_905.0

// auxilio de transporte 2026 - Decreto 1470 de 2025
const val AUXILIO_TRANSPORTE = 249_095.0

// jornada de 42 horas semanales - Ley 2101 de 2021 (vigente desde el 15 de julio de 2026)
const val HORAS_MENSUALES = 210.0

// aporte a salud del trabajador - Ley 100 de 1993
const val PORCENTAJE_SALUD = 0.04

// aporte a pension del trabajador - Ley 100 de 1993
const val PORCENTAJE_PENSION = 0.04

// fondo de solidaridad pensional - Ley 797 de 2003
const val PORCENTAJE_SOLIDARIDAD = 0.01

// factores de las horas extra
const val FACTOR_DIURNA = 1.25
const val FACTOR_NOCTURNA = 1.75
const val FACTOR_DIURNA_DOMINICAL = 2.15
const val FACTOR_NOCTURNA_DOMINICAL = 2.65

// maximo 12 horas extra a la semana, se toman 4 semanas
const val MAX_HORAS_EXTRA = 48.0

const val TOPE_AUXILIO = 2 * SMMLV
const val TOPE_SOLIDARIDAD = 4 * SMMLV

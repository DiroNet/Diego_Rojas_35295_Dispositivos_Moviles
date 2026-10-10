package com.diegoivanrojasavila.calculadoranomina.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegoivanrojasavila.calculadoranomina.R
import com.diegoivanrojasavila.calculadoranomina.logica.ResultadoNomina
import com.diegoivanrojasavila.calculadoranomina.logica.SMMLV

@Composable
fun DesgloseNomina(resultado: ResultadoNomina, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            TituloSeccion(R.string.seccion_devengado)
            LineaConcepto(R.string.valor_hora, resultado.valorHora)
            LineaConcepto(R.string.salario_basico_corto, resultado.salarioBasico)
            LineaConcepto(R.string.horas_extra, resultado.totalHorasExtra)
            LineaConcepto(R.string.auxilio_transporte, resultado.auxilioTransporte)
            LineaConcepto(R.string.total_devengado, resultado.totalDevengado, negrita = true)

            HorizontalDivider()

            TituloSeccion(R.string.seccion_deducciones)
            LineaConcepto(R.string.salud, resultado.aporteSalud)
            LineaConcepto(R.string.pension, resultado.aportePension)
            LineaConcepto(R.string.fondo_solidaridad, resultado.fondoSolidaridad)
            LineaConcepto(R.string.total_deducciones, resultado.totalDeducciones, negrita = true)
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TituloSeccion(R.string.seccion_resultado)
            Text(
                text = stringResource(R.string.salario_neto),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = formatearPesos(resultado.salarioNeto),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.equivale_smmlv, formatearSmmlv(resultado.salarioNeto / SMMLV)),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun TituloSeccion(@StringRes titulo: Int) {
    Text(
        text = stringResource(titulo),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
private fun LineaConcepto(@StringRes concepto: Int, valor: Double, negrita: Boolean = false) {
    val estilo: TextStyle = if (negrita) {
        MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
    } else {
        MaterialTheme.typography.bodyLarge
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(concepto), style = estilo, modifier = Modifier.weight(1f))
        Text(text = formatearPesos(valor), style = estilo)
    }
}

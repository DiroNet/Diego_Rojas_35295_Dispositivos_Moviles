package com.diegoivanrojasavila.calculadoranomina.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diegoivanrojasavila.calculadoranomina.R
import com.diegoivanrojasavila.calculadoranomina.logica.RangoSalarial

@Composable
fun ImagenRango(rango: RangoSalarial, modifier: Modifier = Modifier) {
    // como en el lanzador de dados, cada rango tiene su imagen
    val imagen = when (rango) {
        RangoSalarial.RANGO_1 -> R.drawable.rango_1
        RangoSalarial.RANGO_2 -> R.drawable.rango_2
        RangoSalarial.RANGO_3 -> R.drawable.rango_3
    }
    val descripcion = when (rango) {
        RangoSalarial.RANGO_1 -> R.string.desc_rango_1
        RangoSalarial.RANGO_2 -> R.string.desc_rango_2
        RangoSalarial.RANGO_3 -> R.string.desc_rango_3
    }
    val texto = when (rango) {
        RangoSalarial.RANGO_1 -> R.string.rango_1
        RangoSalarial.RANGO_2 -> R.string.rango_2
        RangoSalarial.RANGO_3 -> R.string.rango_3
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(imagen),
            contentDescription = stringResource(descripcion),
            modifier = Modifier.size(96.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(texto),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

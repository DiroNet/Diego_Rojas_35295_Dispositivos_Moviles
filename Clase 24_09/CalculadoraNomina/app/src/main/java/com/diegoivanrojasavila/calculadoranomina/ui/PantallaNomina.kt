package com.diegoivanrojasavila.calculadoranomina.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegoivanrojasavila.calculadoranomina.R
import com.diegoivanrojasavila.calculadoranomina.logica.ErrorValidacion
import com.diegoivanrojasavila.calculadoranomina.logica.ResultadoNomina
import com.diegoivanrojasavila.calculadoranomina.logica.ResultadoValidacion
import com.diegoivanrojasavila.calculadoranomina.logica.SMMLV
import com.diegoivanrojasavila.calculadoranomina.logica.calcularNomina
import com.diegoivanrojasavila.calculadoranomina.logica.validarDatos
import com.diegoivanrojasavila.calculadoranomina.ui.tema.TemaCalculadoraNomina

@Composable
fun PantallaNomina(modifier: Modifier = Modifier) {
    // se usa rememberSaveable para que al girar no se borre nada
    var salarioTexto by rememberSaveable { mutableStateOf("") }
    var diurnasTexto by rememberSaveable { mutableStateOf("") }
    var nocturnasTexto by rememberSaveable { mutableStateOf("") }
    var esDominical by rememberSaveable { mutableStateOf(false) }
    var transporteEmpresa by rememberSaveable { mutableStateOf(false) }
    var resultado by rememberSaveable { mutableStateOf<ResultadoNomina?>(null) }
    var error by rememberSaveable { mutableStateOf<ErrorValidacion?>(null) }

    val administradorFoco = LocalFocusManager.current

    fun calcular() {
        when (val validacion = validarDatos(salarioTexto, diurnasTexto, nocturnasTexto)) {
            is ResultadoValidacion.Invalido -> {
                error = validacion.error
                resultado = null
            }
            is ResultadoValidacion.Valido -> {
                error = null
                resultado = calcularNomina(
                    salarioBasico = validacion.salarioBasico,
                    horasDiurnas = validacion.horasDiurnas,
                    horasNocturnas = validacion.horasNocturnas,
                    esDominical = esDominical,
                    transporteEmpresa = transporteEmpresa
                )
            }
        }
    }

    fun limpiar() {
        salarioTexto = ""
        diurnasTexto = ""
        nocturnasTexto = ""
        esDominical = false
        transporteEmpresa = false
        resultado = null
        error = null
        administradorFoco.clearFocus()
    }

    val errorEnSalario = error == ErrorValidacion.SALARIO_INVALIDO || error == ErrorValidacion.SALARIO_BAJO_MINIMO
    val errorEnDiurnas = error == ErrorValidacion.DIURNAS_INVALIDAS || error == ErrorValidacion.EXCESO_HORAS
    val errorEnNocturnas = error == ErrorValidacion.NOCTURNAS_INVALIDAS || error == ErrorValidacion.EXCESO_HORAS

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.subtitulo),
            style = MaterialTheme.typography.bodyMedium
        )

        CampoNumerico(
            etiqueta = R.string.salario_basico,
            valor = salarioTexto,
            alCambiarValor = { salarioTexto = it },
            opcionesTeclado = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            esError = errorEnSalario,
            modifier = Modifier.fillMaxWidth()
        )
        CampoNumerico(
            etiqueta = R.string.horas_diurnas,
            valor = diurnasTexto,
            alCambiarValor = { diurnasTexto = it },
            opcionesTeclado = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            esError = errorEnDiurnas,
            modifier = Modifier.fillMaxWidth()
        )
        CampoNumerico(
            etiqueta = R.string.horas_nocturnas,
            valor = nocturnasTexto,
            alCambiarValor = { nocturnasTexto = it },
            opcionesTeclado = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            accionesTeclado = KeyboardActions(onDone = { administradorFoco.clearFocus() }),
            esError = errorEnNocturnas,
            modifier = Modifier.fillMaxWidth()
        )

        // si ya hay resultado se vuelve a calcular solo al mover el switch
        FilaInterruptor(
            etiqueta = R.string.dominical,
            activado = esDominical,
            alCambiar = {
                esDominical = it
                if (resultado != null) calcular()
            },
            modifier = Modifier.fillMaxWidth()
        )
        FilaInterruptor(
            etiqueta = R.string.transporte_empresa,
            activado = transporteEmpresa,
            alCambiar = {
                transporteEmpresa = it
                if (resultado != null) calcular()
            },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    administradorFoco.clearFocus()
                    calcular()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.calcular))
            }
            OutlinedButton(
                onClick = { limpiar() },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.limpiar))
            }
        }

        error?.let { MensajeError(it) }

        resultado?.let {
            ImagenRango(rango = it.rango, modifier = Modifier.padding(top = 8.dp))
            DesgloseNomina(resultado = it)
        }
    }
}

@Composable
private fun MensajeError(error: ErrorValidacion) {
    val mensaje = when (error) {
        ErrorValidacion.SALARIO_INVALIDO -> stringResource(R.string.error_salario_invalido)
        ErrorValidacion.SALARIO_BAJO_MINIMO -> stringResource(R.string.error_salario_minimo, formatearPesos(SMMLV))
        ErrorValidacion.DIURNAS_INVALIDAS,
        ErrorValidacion.NOCTURNAS_INVALIDAS -> stringResource(R.string.error_horas_invalidas)
        ErrorValidacion.EXCESO_HORAS -> stringResource(R.string.error_exceso_horas)
    }
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = mensaje,
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VistaPreviaPantallaNomina() {
    TemaCalculadoraNomina {
        Surface {
            PantallaNomina()
        }
    }
}

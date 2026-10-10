package com.diegoivanrojasavila.calculadorapropinas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegoivanrojasavila.calculadorapropinas.ui.theme.CalculadoraPropinasTheme
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraPropinasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CalculadoraPropinasLayout()
                }
            }
        }
    }
}

@Composable
fun CalculadoraPropinasLayout() {
    var valorCuenta by remember { mutableStateOf("") }
    var porcentajeTexto by remember { mutableStateOf("") }
    var redondear by remember { mutableStateOf(false) }

    val cuenta = valorCuenta.toDoubleOrNull() ?: 0.0
    val porcentaje = porcentajeTexto.toDoubleOrNull() ?: 0.0
    val propina = calcularPropina(cuenta, porcentaje, redondear)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.nombre_estudiante),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 40.dp)
        )
        Text(
            text = stringResource(R.string.calcular_propina),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 24.dp)
                .align(alignment = Alignment.Start)
        )
        CampoNumeroEditable(
            etiqueta = R.string.valor_cuenta,
            opcionesTeclado = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            valor = valorCuenta,
            onValueChange = { valorCuenta = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        CampoNumeroEditable(
            etiqueta = R.string.porcentaje_propina,
            opcionesTeclado = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            valor = porcentajeTexto,
            onValueChange = { porcentajeTexto = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        FilaRedondearPropina(
            redondear = redondear,
            onRedondearChange = { redondear = it },
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = stringResource(R.string.total_propina, propina),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

// el estado vive arriba, este campo solo muestra y avisa cambios
@Composable
fun CampoNumeroEditable(
    @StringRes etiqueta: Int,
    opcionesTeclado: KeyboardOptions,
    valor: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = valor,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChange,
        label = { Text(stringResource(etiqueta)) },
        keyboardOptions = opcionesTeclado
    )
}

@Composable
fun FilaRedondearPropina(
    redondear: Boolean,
    onRedondearChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.redondear_propina))
        Switch(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = redondear,
            onCheckedChange = onRedondearChange
        )
    }
}

private fun calcularPropina(monto: Double, porcentaje: Double = 15.0, redondear: Boolean): String {
    var propina = porcentaje / 100 * monto
    if (redondear) {
        propina = ceil(propina)
    }
    return NumberFormat.getCurrencyInstance().format(propina)
}

@Preview(showBackground = true)
@Composable
fun CalculadoraPropinasPreview() {
    CalculadoraPropinasTheme {
        CalculadoraPropinasLayout()
    }
}

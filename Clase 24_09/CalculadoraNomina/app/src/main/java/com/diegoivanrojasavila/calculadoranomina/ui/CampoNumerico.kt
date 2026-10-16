package com.diegoivanrojasavila.calculadoranomina.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

// no guarda estado, todo le llega desde la pantalla
@Composable
fun CampoNumerico(
    @StringRes etiqueta: Int,
    valor: String,
    alCambiarValor: (String) -> Unit,
    opcionesTeclado: KeyboardOptions,
    modifier: Modifier = Modifier,
    accionesTeclado: KeyboardActions = KeyboardActions.Default,
    esError: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiarValor,
        label = { Text(stringResource(etiqueta)) },
        singleLine = true,
        isError = esError,
        keyboardOptions = opcionesTeclado,
        keyboardActions = accionesTeclado,
        modifier = modifier
    )
}

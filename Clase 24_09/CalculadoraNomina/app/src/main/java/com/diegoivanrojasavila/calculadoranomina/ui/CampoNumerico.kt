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
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    esError: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(stringResource(etiqueta)) },
        singleLine = true,
        isError = esError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
    )
}

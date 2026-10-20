package com.mexiti.costogasolina.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

/**
 * Campo de texto numérico reutilizable para capturar datos como precio por litro, litros o propina.
 *
 * @param label Recurso de texto ID ([StringRes]) para la etiqueta desplegada.
 * @param leadingIcon Recurso gráfico ID ([DrawableRes]) para el icono al inicio del campo.
 * @param keyboardsOptions Opciones de teclado (ej. tipo numérico, acción ImeAction).
 * @param value Valor actual del campo en formato texto.
 * @param onValueChanged Callback activado cuando el usuario modifica el contenido del campo.
 * @param modifier Modificador de diseño.
 */
@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardsOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        label = { Text(text = stringResource(id = label)) },
        value = value,
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), contentDescription = null) },
        keyboardOptions = keyboardsOptions,
        modifier = modifier.fillMaxWidth(),
        onValueChange = onValueChanged
    )
}

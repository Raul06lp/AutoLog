package com.carlafdez.autolog.presentation.screens.addScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.carlafdez.autolog.domain.model.Cliente
import com.carlafdez.autolog.ui.theme.Carta

@Composable
fun ClienteSelector(
    clienteSeleccionado: Cliente?,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        OutlinedTextField(
            value = clienteSeleccionado?.nombre ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Cliente") },
            placeholder = {
                Text(if (isLoading) "Cargando clientes..." else "Selecciona un cliente")
            },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = Color.Gray,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = Carta
            )
        )
    }
}

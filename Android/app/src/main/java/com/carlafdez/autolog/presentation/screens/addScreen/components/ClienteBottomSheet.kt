package com.carlafdez.autolog.presentation.screens.addScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.domain.model.Cliente
import com.carlafdez.autolog.presentation.screens.addScreen.AccentBlue
import com.carlafdez.autolog.ui.theme.Texto
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteBottomSheet(
    clientes: List<Cliente>,
    isLoading: Boolean,
    clienteSeleccionado: Cliente?,
    onClienteClick: (Cliente) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Seleccionar cliente",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Texto,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            HorizontalDivider()

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AccentBlue)
                }
            } else if (clientes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontraron clientes", color = Color.Gray)
                }
            } else {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    clientes.forEach { cliente ->
                        val seleccionado = cliente.id == clienteSeleccionado?.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onClienteClick(cliente) }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = cliente.nombre,
                                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                                    color = if (seleccionado) AccentBlue else Color.Black
                                )
                                Text(text = cliente.email, fontSize = 12.sp, color = Color.Gray)
                            }
                            if (seleccionado) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = AccentBlue)
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ClienteBottomSheetPreview(){
    ClienteBottomSheet(
        clientes = listOf(
            Cliente(1, "Carlos López", "carlos@email.com")
        ),
        isLoading = false,
        clienteSeleccionado = null,
        onClienteClick = {},
        onDismiss = {}
    )
}
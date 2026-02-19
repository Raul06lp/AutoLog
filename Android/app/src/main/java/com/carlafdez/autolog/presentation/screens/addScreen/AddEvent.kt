package com.carlafdez.autolog.presentation.screens.addScreen

import android.net.Uri
import com.carlafdez.autolog.domain.model.Cliente

sealed interface AddEvent {
    data class MatriculaChanged(val value: String) : AddEvent
    data class MarcaChanged(val value: String) : AddEvent
    data class ModeloChanged(val value: String) : AddEvent
    data class AnioChanged(val value: String) : AddEvent
    data class ColorChanged(val value: String) : AddEvent
    data class KilometrajeChanged(val value: String) : AddEvent
    data class ObservacionesChanged(val value: String) : AddEvent
    data class ClienteSeleccionado(val cliente: Cliente) : AddEvent
    data class ImagenSeleccionada(val uri: Uri?) : AddEvent
    data object GuardarClick : AddEvent
    data object ErrorDismissed : AddEvent
    data object ResetGuardado : AddEvent
}

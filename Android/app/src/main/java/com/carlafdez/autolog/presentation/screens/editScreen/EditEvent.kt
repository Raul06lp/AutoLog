package com.carlafdez.autolog.presentation.screens.editScreen

import android.net.Uri

sealed interface EditEvent {
    data class MatriculaChanged(val value: String) : EditEvent
    data class MarcaChanged(val value: String) : EditEvent
    data class ModeloChanged(val value: String) : EditEvent
    data class AnioChanged(val value: String) : EditEvent
    data class ColorChanged(val value: String) : EditEvent
    data class KilometrajeChanged(val value: String) : EditEvent
    data class ObservacionesChanged(val value: String) : EditEvent
    data class MedidasTomadasChanged(val value: String) : EditEvent
    data class ImagenSeleccionada(val uri: Uri?) : EditEvent
    data object GuardarClick : EditEvent
    data object ErrorDismissed : EditEvent
}

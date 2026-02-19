package com.carlafdez.autolog.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ErrorResponseDTO(
    @SerializedName("timestamp")
    val timestamp: String? = null,

    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("path")
    val path: String? = null
)

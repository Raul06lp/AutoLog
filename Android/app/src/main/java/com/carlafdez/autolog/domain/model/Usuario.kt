package com.carlafdez.autolog.domain.model

sealed class Usuario(
    open val id: Long,
    open val nombre: String,
    open val email: String,
    open val tipo: TipoUsuario
) {
    data class ClienteUsuario(
        override val id: Long,
        override val nombre: String,
        override val email: String,
        val cantidadVehiculos: Int = 0
    ) : Usuario(id, nombre, email, TipoUsuario.CLIENTE)

    data class MecanicoUsuario(
        override val id: Long,
        override val nombre: String,
        override val email: String,
        val cantidadVehiculos: Int = 0
    ) : Usuario(id, nombre, email, TipoUsuario.MECANICO)
}

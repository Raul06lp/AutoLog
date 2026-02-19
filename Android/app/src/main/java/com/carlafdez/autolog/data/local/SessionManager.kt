package com.carlafdez.autolog.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.carlafdez.autolog.domain.model.TipoUsuario
import com.carlafdez.autolog.domain.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_USUARIO_ID = longPreferencesKey("usuario_id")
        private val KEY_USUARIO_NOMBRE = stringPreferencesKey("usuario_nombre")
        private val KEY_USUARIO_EMAIL = stringPreferencesKey("usuario_email")
        private val KEY_USUARIO_TIPO = stringPreferencesKey("usuario_tipo") // "CLIENTE" o "MECANICO"
    }

    val usuarioId: Flow<Long?> = context.dataStore.data
        .map { it[KEY_USUARIO_ID] }

    val usuarioNombre: Flow<String?> = context.dataStore.data
        .map { it[KEY_USUARIO_NOMBRE] }

    val usuarioEmail: Flow<String?> = context.dataStore.data
        .map { it[KEY_USUARIO_EMAIL] }

    val usuarioTipo: Flow<TipoUsuario?> = context.dataStore.data
        .map { prefs ->
            when (prefs[KEY_USUARIO_TIPO]) {
                "CLIENTE" -> TipoUsuario.CLIENTE
                "MECANICO" -> TipoUsuario.MECANICO
                else -> null
            }
        }

    // Obtener usuario completo
    val usuario: Flow<Usuario?> = context.dataStore.data
        .map { prefs ->
            val id = prefs[KEY_USUARIO_ID]
            val nombre = prefs[KEY_USUARIO_NOMBRE]
            val email = prefs[KEY_USUARIO_EMAIL]
            val tipo = prefs[KEY_USUARIO_TIPO]

            if (id != null && nombre != null && email != null && tipo != null) {
                when (tipo) {
                    "CLIENTE" -> Usuario.ClienteUsuario(id, nombre, email)
                    "MECANICO" -> Usuario.MecanicoUsuario(id, nombre, email)
                    else -> null
                }
            } else {
                null
            }
        }

    suspend fun saveSession(id: Long, nombre: String, email: String, tipo: TipoUsuario) {
        context.dataStore.edit {
            it[KEY_USUARIO_ID] = id
            it[KEY_USUARIO_NOMBRE] = nombre
            it[KEY_USUARIO_EMAIL] = email
            it[KEY_USUARIO_TIPO] = tipo.name
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}

package com.carlafdez.autolog.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_MECANICO_ID = longPreferencesKey("mecanico_id")
        private val KEY_MECANICO_NOMBRE = stringPreferencesKey("mecanico_nombre")
    }

    val mecanicoId: Flow<Long?> = context.dataStore.data
        .map { it[KEY_MECANICO_ID] }

    val mecanicoNombre: Flow<String?> = context.dataStore.data
        .map { it[KEY_MECANICO_NOMBRE] }

    suspend fun saveSession(id: Long, nombre: String) {
        context.dataStore.edit {
            it[KEY_MECANICO_ID] = id
            it[KEY_MECANICO_NOMBRE] = nombre
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}

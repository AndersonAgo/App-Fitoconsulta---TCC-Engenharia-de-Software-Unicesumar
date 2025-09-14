package com.anderson.fitoconsulta.repository

import com.anderson.fitoconsulta.database.FitoconsultaQueries
import com.anderson.fitoconsulta.database.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlantRepository(private val queries: FitoconsultaQueries) {

    suspend fun getAllPlants(): List<Plant> {
        return withContext(Dispatchers.IO) {
            queries.selectAll().executeAsList()
        }
    }

    suspend fun searchPlants(query: String): List<Plant> {
        return withContext(Dispatchers.IO) {
            queries.searchByName("%$query%").executeAsList()
        }
    }

    suspend fun getPlantById(id: Long): Plant? {
        return withContext(Dispatchers.IO) {
            queries.getById(id).executeAsOneOrNull()
        }
    }
}

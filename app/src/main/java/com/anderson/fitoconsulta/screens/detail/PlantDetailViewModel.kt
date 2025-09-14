package com.anderson.fitoconsulta.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anderson.fitoconsulta.database.Plant
import com.anderson.fitoconsulta.repository.PlantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailScreenState(
    val plant: Plant? = null,
    val isLoading: Boolean = true
)

class PlantDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: PlantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        val plantId: Long = checkNotNull(savedStateHandle["plantId"])
        loadPlantDetails(plantId)
    }

    private fun loadPlantDetails(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val plant = repository.getPlantById(id)
            _uiState.update { it.copy(plant = plant, isLoading = false) }
        }
    }
}
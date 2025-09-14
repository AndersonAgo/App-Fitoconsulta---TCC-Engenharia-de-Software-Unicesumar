package com.anderson.fitoconsulta.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anderson.fitoconsulta.database.Plant
import com.anderson.fitoconsulta.repository.PlantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log

data class HomeScreenState(
    val plants: List<Plant> = emptyList(),
    val searchQuery: String = "",
    val isSearchBarVisible: Boolean = false,
    val isLoading: Boolean = true
)

class HomeViewModel(private val repository: PlantRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAllPlants()
    }

    private fun loadAllPlants() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val plants = repository.getAllPlants()

            //Log para teste
            Log.d("FitoconsultaDebug", "Plantas carregadas do repositório: ${plants.size} itens.")

            _uiState.update { it.copy(plants = plants, isLoading = false) }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val plants = repository.searchPlants(query)
            _uiState.update { it.copy(plants = plants, isLoading = false) }
        }
    }

    fun onToggleSearchBar() {
        val currentVisibility = _uiState.value.isSearchBarVisible
        _uiState.update { it.copy(isSearchBarVisible = !currentVisibility) }
        if (currentVisibility) {
            _uiState.update { it.copy(searchQuery = "") }
            loadAllPlants()
        }
    }

    fun onClearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
        loadAllPlants()
    }
}
package com.responsi.digimonexplorer.ui.screens.home

import com.responsi.digimonexplorer.data.model.Digimon

/**
 * UI State terstruktur untuk Home Screen: Loading, Success (Data), dan Error
 */
sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val digimons: List<Digimon>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

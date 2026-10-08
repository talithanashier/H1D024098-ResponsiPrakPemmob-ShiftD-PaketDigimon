package com.responsi.digimonexplorer.ui.screens.detail

import com.responsi.digimonexplorer.data.model.Digimon

/**
 * UI State terstruktur untuk Detail Screen
 */
sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val digimon: Digimon) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

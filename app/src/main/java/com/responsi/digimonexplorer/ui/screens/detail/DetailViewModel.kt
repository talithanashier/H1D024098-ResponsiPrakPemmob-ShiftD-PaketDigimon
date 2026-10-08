package com.responsi.digimonexplorer.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.responsi.digimonexplorer.data.repository.DigimonRepository
import com.responsi.digimonexplorer.data.repository.DigimonRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Detail Screen
 */
class DetailViewModel(
    private val repository: DigimonRepository = DigimonRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var currentDigimonId: Int = 0

    fun loadDigimonDetail(id: Int) {
        currentDigimonId = id
        _uiState.value = DetailUiState.Loading
        viewModelScope.launch {
            repository.getDigimonById(id)
                .onSuccess { digimon ->
                    _uiState.value = DetailUiState.Success(digimon)
                }
                .onFailure { throwable ->
                    val errorMsg = throwable.localizedMessage 
                        ?: "Gagal memuat detail Digimon."
                    _uiState.value = DetailUiState.Error(errorMsg)
                }
        }
    }

    fun retry() {
        if (currentDigimonId > 0) {
            loadDigimonDetail(currentDigimonId)
        }
    }

    companion object {
        fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailViewModel(DigimonRepositoryImpl()) as T
            }
        }
    }
}

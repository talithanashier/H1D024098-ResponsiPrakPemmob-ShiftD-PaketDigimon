package com.responsi.digimonexplorer.ui.screens.home

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
 * ViewModel untuk Home Screen mengikuti pola MVVM
 */
class HomeViewModel(
    private val repository: DigimonRepository = DigimonRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDigimons()
    }

    /**
     * Mengambil daftar Digimon dari repository dan memperbarui state
     */
    fun loadDigimons() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            repository.getDigimons(pageSize = 25)
                .onSuccess { list ->
                    if (list.isEmpty()) {
                        _uiState.value = HomeUiState.Error("Tidak ada data Digimon yang ditemukan.")
                    } else {
                        _uiState.value = HomeUiState.Success(list)
                    }
                }
                .onFailure { throwable ->
                    val errorMsg = throwable.localizedMessage 
                        ?: "Gagal memuat data Digimon. Pastikan koneksi internet aktif."
                    _uiState.value = HomeUiState.Error(errorMsg)
                }
        }
    }

    /**
     * Factory untuk inisialisasi HomeViewModel jika diperlukan
     */
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(DigimonRepositoryImpl()) as T
            }
        }
    }
}

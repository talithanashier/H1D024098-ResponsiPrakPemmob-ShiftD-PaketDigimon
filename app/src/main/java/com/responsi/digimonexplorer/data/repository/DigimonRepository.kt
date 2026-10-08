package com.responsi.digimonexplorer.data.repository

import com.responsi.digimonexplorer.data.api.ApiClient
import com.responsi.digimonexplorer.data.api.DigiApiService
import com.responsi.digimonexplorer.data.model.Digimon
import com.responsi.digimonexplorer.data.model.toDomainModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Interface Repository untuk abstraksi data Digimon
 */
interface DigimonRepository {
    suspend fun getDigimons(pageSize: Int = 20): Result<List<Digimon>>
    suspend fun getDigimonById(id: Int): Result<Digimon>
}

/**
 * Implementasi Repository yang mengambil data dari DigiApiService
 * serta mengoptimalkan performa dengan in-memory cache dan parallel fetching.
 */
class DigimonRepositoryImpl(
    private val apiService: DigiApiService = ApiClient.apiService
) : DigimonRepository {

    // In-memory cache agar navigasi ke detail cepat tanpa re-fetch jika sudah ada
    private val memoryCache = mutableMapOf<Int, Digimon>()

    override suspend fun getDigimons(pageSize: Int): Result<List<Digimon>> {
        return try {
            val listResponse = apiService.getDigimonList(pageSize = pageSize)
            val items = listResponse.content

            // Mengambil detail (Level, Attribute, Type) secara paralel menggunakan coroutine
            val digimonList = coroutineScope {
                items.map { item ->
                    async {
                        // Cek cache terlebih dahulu
                        memoryCache[item.id] ?: try {
                            val detail = apiService.getDigimonDetail(item.id)
                            val domainModel = detail.toDomainModel()
                            memoryCache[item.id] = domainModel
                            domainModel
                        } catch (e: Exception) {
                            // Fallback jika detail 1 item bermasalah
                            Digimon(
                                id = item.id,
                                name = item.name,
                                imageUrl = item.image.orEmpty(),
                                level = "Unknown",
                                attribute = "Unknown",
                                type = "Unknown"
                            )
                        }
                    }
                }.awaitAll()
            }

            Result.success(digimonList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDigimonById(id: Int): Result<Digimon> {
        return try {
            val cached = memoryCache[id]
            if (cached != null && cached.description.isNotBlank()) {
                Result.success(cached)
            } else {
                val detail = apiService.getDigimonDetail(id)
                val domainModel = detail.toDomainModel()
                memoryCache[id] = domainModel
                Result.success(domainModel)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

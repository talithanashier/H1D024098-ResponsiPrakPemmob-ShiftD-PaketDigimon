package com.responsi.digimonexplorer.data.api

import com.responsi.digimonexplorer.data.model.DigimonDetailResponse
import com.responsi.digimonexplorer.data.model.DigimonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface Retrofit untuk Digi-API (DAPI)
 * Base URL: https://digi-api.com/api/v1/
 */
interface DigiApiService {

    @GET("digimon")
    suspend fun getDigimonList(
        @Query("pageSize") pageSize: Int = 20
    ): DigimonListResponse

    @GET("digimon/{id}")
    suspend fun getDigimonDetail(
        @Path("id") id: Int
    ): DigimonDetailResponse
}

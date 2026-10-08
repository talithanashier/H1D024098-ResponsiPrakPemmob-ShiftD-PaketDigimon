package com.responsi.digimonexplorer.data.model

import com.google.gson.annotations.SerializedName

/**
 * Respons API untuk endpoint GET /api/v1/digimon
 */
data class DigimonListResponse(
    @SerializedName("content")
    val content: List<DigimonListItemDto> = emptyList(),
    @SerializedName("pageable")
    val pageable: PageableDto? = null
)

data class DigimonListItemDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("href")
    val href: String? = null,
    @SerializedName("image")
    val image: String? = null
)

data class PageableDto(
    @SerializedName("currentPage")
    val currentPage: Int? = null,
    @SerializedName("elementsOnPage")
    val elementsOnPage: Int? = null,
    @SerializedName("totalElements")
    val totalElements: Int? = null,
    @SerializedName("totalPages")
    val totalPages: Int? = null
)

/**
 * Respons API untuk endpoint GET /api/v1/digimon/{id}
 */
data class DigimonDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("xAntibody")
    val xAntibody: Boolean? = false,
    @SerializedName("images")
    val images: List<ImageDto> = emptyList(),
    @SerializedName("levels")
    val levels: List<LevelDto> = emptyList(),
    @SerializedName("types")
    val types: List<TypeDto> = emptyList(),
    @SerializedName("attributes")
    val attributes: List<AttributeDto> = emptyList(),
    @SerializedName("fields")
    val fields: List<FieldDto> = emptyList(),
    @SerializedName("releaseDate")
    val releaseDate: String? = null,
    @SerializedName("descriptions")
    val descriptions: List<DescriptionDto> = emptyList(),
    @SerializedName("skills")
    val skills: List<SkillDto> = emptyList()
)

data class ImageDto(
    @SerializedName("href")
    val href: String?,
    @SerializedName("transparent")
    val transparent: Boolean? = false
)

data class LevelDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("level")
    val level: String?
)

data class TypeDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("type")
    val type: String?
)

data class AttributeDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("attribute")
    val attribute: String?
)

data class FieldDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("field")
    val field: String?,
    @SerializedName("image")
    val image: String?
)

data class DescriptionDto(
    @SerializedName("origin")
    val origin: String?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("description")
    val description: String?
)

data class SkillDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("skill")
    val skill: String?,
    @SerializedName("translation")
    val translation: String?,
    @SerializedName("description")
    val description: String?
)

/**
 * Domain model yang bersih untuk digunakan di UI Layer
 */
data class Digimon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val level: String,
    val attribute: String,
    val type: String,
    val description: String = "",
    val releaseDate: String = "",
    val skills: List<String> = emptyList()
)

/**
 * Extension mapper dari DTO ke Domain Model
 */
fun DigimonDetailResponse.toDomainModel(): Digimon {
    val img = images.firstOrNull()?.href.orEmpty()
    val lvl = levels.firstOrNull()?.level ?: "Unknown"
    val attr = attributes.firstOrNull()?.attribute ?: "Unknown"
    val typ = types.firstOrNull()?.type ?: "Unknown"
    
    // Ambil deskripsi bahasa Inggris atau fallback yang pertama
    val desc = descriptions.firstOrNull { it.language?.equals("en_us", ignoreCase = true) == true }?.description
        ?: descriptions.firstOrNull()?.description
        ?: "No description available."
        
    val skillList = skills.mapNotNull { it.skill }.filter { it.isNotBlank() }

    return Digimon(
        id = id,
        name = name,
        imageUrl = img,
        level = lvl,
        attribute = attr,
        type = typ,
        description = desc,
        releaseDate = releaseDate ?: "-",
        skills = skillList
    )
}

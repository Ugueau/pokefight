package com.example.pokefight.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("base_experience") val baseExperience: Int,
    val height: Int,
    val id: Int,
    @SerializedName("is_default") val isDefault: Boolean,
    val name: String,
    val order: Int,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Int
)

data class Sprites(
    @SerializedName("back_default") val backDefault: String?,
    @SerializedName("back_female") val backFemale: String?, // Nullable because it can be null in the JSON
    @SerializedName("back_shiny") val backShiny: String?,
    @SerializedName("back_shiny_female") val backShinyFemale: String?, // Nullable because it can be null in the JSON
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("front_female") val frontFemale: String?, // Nullable because it can be null in the JSON
    @SerializedName("front_shiny") val frontShiny: String?,
    @SerializedName("front_shiny_female") val frontShinyFemale: String? // Nullable because it can be null in the JSON
)

data class Stat(
    @SerializedName("base_stat") val baseStat: Int,
    val effort: Int,
    val stat: StatInfo
)

data class StatInfo(
    val name: String,
    val url: String
)

data class Type(
    val slot: Int,
    val type: TypeInfo
)

data class TypeInfo(
    val name: String,
    val url: String
)
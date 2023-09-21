package com.example.pokefight.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("base_experience") @Expose val baseExperience: Int,
    @SerializedName("height") @Expose val height: Int,
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("is_default") @Expose val isDefault: Boolean,
    @SerializedName("name") @Expose val name: String,
    @SerializedName("order") @Expose val order: Int,
    @SerializedName("sprites") @Expose val sprites: Sprites,
    @SerializedName("stats") @Expose val stats: List<Stat>,
    @SerializedName("types") @Expose val types: List<Type>,
    @SerializedName("weight") @Expose val weight: Int
)

data class Sprites(
    @SerializedName("back_default") @Expose val backDefault: String?,
    @SerializedName("back_female") @Expose val backFemale: String?, // Nullable because it can be null in the JSON
    @SerializedName("back_shiny") @Expose val backShiny: String?,
    @SerializedName("back_shiny_female") @Expose val backShinyFemale: String?, // Nullable because it can be null in the JSON
    @SerializedName("front_default") @Expose val frontDefault: String?,
    @SerializedName("front_female") @Expose val frontFemale: String?, // Nullable because it can be null in the JSON
    @SerializedName("front_shiny") @Expose val frontShiny: String?,
    @SerializedName("front_shiny_female") @Expose val frontShinyFemale: String? // Nullable because it can be null in the JSON
)

data class Stat(
    @SerializedName("base_stat") @Expose val baseStat: Int,
    @SerializedName("effort") @Expose val effort: Int,
    @SerializedName("StatInfo") @Expose val stat: StatInfo
)

data class StatInfo(
    @SerializedName("name") @Expose val name: String,
    @SerializedName("url") @Expose val url: String
)

data class Type(
    @SerializedName("slot") @Expose val slot: Int,
    @SerializedName("type") @Expose val type: TypeInfo
)

data class TypeInfo(
    @SerializedName("name") @Expose val name: String,
    @SerializedName("url") @Expose val url: String
)
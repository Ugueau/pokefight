package com.example.pokefight.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("base_experience")  val baseExperience: Int,
    @SerializedName("height")  val height: Int,
    @SerializedName("id")  val id: Int,
    @SerializedName("is_default")  val isDefault: Boolean,
    @SerializedName("name")  val name: String,
    @SerializedName("order")  val order: Int,
    @SerializedName("sprites")  val sprites: Sprites,
    @SerializedName("stats")  val stats: List<Stat>,
    @SerializedName("types")  val types: List<Type>,
    @SerializedName("weight")  val weight: Int
)

data class Sprites(
    @SerializedName("back_default")  val backDefault: String?,
    @SerializedName("back_female")  val backFemale: String?, // Nullable because it can be null in the JSON
    @SerializedName("back_shiny")  val backShiny: String?,
    @SerializedName("back_shiny_female")  val backShinyFemale: String?, // Nullable because it can be null in the JSON
    @SerializedName("front_default")  val frontDefault: String?,
    @SerializedName("front_female")  val frontFemale: String?, // Nullable because it can be null in the JSON
    @SerializedName("front_shiny")  val frontShiny: String?,
    @SerializedName("front_shiny_female")  val frontShinyFemale: String? // Nullable because it can be null in the JSON
)

data class Stat(
    @SerializedName("base_stat")  val baseStat: Int,
    @SerializedName("effort")  val effort: Int,
    @SerializedName("StatInfo")  val stat: StatInfo
)

data class StatInfo(
    @SerializedName("name")  val name: String,
    @SerializedName("url")  val url: String
)

data class Type(
    @SerializedName("slot")  val slot: Int,
    @SerializedName("type")  val type: TypeInfo
)

data class TypeInfo(
    @SerializedName("name")  val name: String,
    @SerializedName("url")  val url: String
)

fun Pokemon.formatId(id : Int):String{
    if(id < 10){
        return "#00$id"
    }else if(id < 100){
        return "#0$id"
    }else{
        return "#$id"
    }
}

enum class Attribute{
    HP,
    ATTACK,
    DEFENSE,
    SPEED
}

fun Pokemon.getAttribute(pokemon: Pokemon, attribute: Attribute): Int{
    return when(attribute){
        Attribute.HP -> pokemon.stats[0].baseStat
        Attribute.ATTACK -> pokemon.stats[1].baseStat
        Attribute.DEFENSE -> pokemon.stats[2].baseStat
        Attribute.SPEED -> pokemon.stats[5].baseStat

    }
}

enum class PokemonElement(val hexColor: String) {
    FIRE("#FF3D00"),
    WATER("#2979FF"),
    GRASS("#00C853"),
    ELECTRIC("#FFEA00"),
    ICE("#B3E5FC"),
    PSYCHIC("#AA00FF"),
    DARK("#212121"),
    FLYING("#03A9F4"),
    ROCK("#795548"),
    GROUND("#FF6D00"),
    FAIRY("#FF4081"),
    STEEL("#9E9E9E"),
    POISON("#8E24AA"),
    BUG("#4CAF50"),
    GHOST("#7B1FA2"),
    DRAGON("#00BCD4"),
    FIGHTING("#795548"),
    NORMAL("#D7CCC8");
}

enum class Rarity(val rarity: Int){
    COMMON(70), // 0 - 70
    UNCOMMON(80), // 71 -80
    RARE(90), // 81 - 90
    EPIC(100), // 91 - 100
    LEGENDARY(255) // 101+
}

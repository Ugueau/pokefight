package com.example.pokefight.model

import com.example.pokefight.R
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
    SPEED,
    SPECIAL_ATTACK,
    SPECIAL_DEFENSE
}

fun Pokemon.getAttribute(attribute: Attribute): Int{
    return when(attribute){
        Attribute.HP -> this.stats[0].baseStat
        Attribute.ATTACK -> this.stats[1].baseStat
        Attribute.DEFENSE -> this.stats[2].baseStat
        Attribute.SPECIAL_ATTACK -> this.stats[3].baseStat
        Attribute.SPECIAL_DEFENSE -> this.stats[4].baseStat
        Attribute.SPEED -> this.stats[5].baseStat
    }
}

enum class PokemonElement(val color: Int) {
    FIRE(R.color.fire_color),
    WATER(R.color.water_color),
    GRASS(R.color.grass_color),
    ELECTRIC(R.color.electric_color),
    ICE(R.color.ice_color),
    PSYCHIC(R.color.psychic_color),
    DARK(R.color.dark_color),
    FLYING(R.color.flying_color),
    ROCK(R.color.rock_color),
    GROUND(R.color.ground_color),
    FAIRY(R.color.fairy_color),
    STEEL(R.color.steel_color),
    POISON(R.color.poison_color),
    BUG(R.color.bug_color),
    GHOST(R.color.ghost_color),
    DRAGON(R.color.dragon_color),
    FIGHTING(R.color.fighting_color),
    NORMAL(R.color.normal_color);
}

fun Pokemon.getTypeColor(type: Type): Int {
    return when (type.type.name) {
        "fire" -> PokemonElement.FIRE.color
        "water" -> PokemonElement.WATER.color
        "grass" -> PokemonElement.GRASS.color
        "electric" -> PokemonElement.ELECTRIC.color
        "ice" -> PokemonElement.ICE.color
        "psychic" -> PokemonElement.PSYCHIC.color
        "dark" -> PokemonElement.DARK.color
        "flying" -> PokemonElement.FLYING.color
        "rock" -> PokemonElement.ROCK.color
        "ground" -> PokemonElement.GROUND.color
        "fairy" -> PokemonElement.FAIRY.color
        "steel" -> PokemonElement.STEEL.color
        "poison" -> PokemonElement.POISON.color
        "bug" -> PokemonElement.BUG.color
        "ghost" -> PokemonElement.GHOST.color
        "dragon" -> PokemonElement.DRAGON.color
        "fighting" -> PokemonElement.FIGHTING.color
        "normal" -> PokemonElement.NORMAL.color
        else -> -1
    }
}

enum class Rarity(val value: Int){
    COMMON(70), // 0 - 70
    UNCOMMON(80), // 71 -80
    RARE(90), // 81 - 90
    EPIC(100), // 91 - 100
    LEGENDARY(255) // 101+
}

fun Pokemon.getRarity() : Rarity{

    val score :Int = (this.getAttribute(Attribute.ATTACK)+this.getAttribute(Attribute.SPECIAL_ATTACK)+this.getAttribute(Attribute.SPECIAL_DEFENSE)+this.getAttribute(Attribute.DEFENSE)+this.getAttribute(Attribute.HP)+this.getAttribute(Attribute.SPEED))/6

    return if(score <= Rarity.COMMON.value){
        Rarity.COMMON
    }else if(score <= Rarity.UNCOMMON.value){
        Rarity.UNCOMMON
    }else if(score <= Rarity.RARE.value){
        Rarity.RARE
    }else if(score <= Rarity.EPIC.value){
        Rarity.EPIC
    }else {
        Rarity.LEGENDARY
    }
}

fun Pokemon.stringify() : String{
    return "${this.name} : ${this.id}"
}
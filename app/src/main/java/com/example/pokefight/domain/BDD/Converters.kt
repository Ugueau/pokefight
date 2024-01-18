package com.example.pokefight.domain.BDD

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromListInt(value: List<Int>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toListInt(value: String): List<Int> {
        return value.split(",").mapNotNull { it.toIntOrNull() }
    }
}
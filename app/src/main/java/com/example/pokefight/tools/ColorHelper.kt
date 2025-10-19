package com.example.pokefight.tools

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

object ColorHelper {

    @ColorInt
    fun resolveThemeColorAttribute(context: Context, @AttrRes attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

}

package com.example.would_you_rather

import android.content.Context
import android.content.SharedPreferences

object LocalPrefs {
    private const val PREFS_NAME = "wyr_prefs"
    private const val KEY_LAST_USERNAME = "last_username"
    private const val KEY_OPTION_TEXT_SIZE = "option_text_size"
    private const val KEY_LAST_RATING = "last_rating"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLastUsername(context: Context, username: String) {
        prefs(context).edit().putString(KEY_LAST_USERNAME, username).apply()
    }

    fun getLastUsername(context: Context): String =
        prefs(context).getString(KEY_LAST_USERNAME, "") ?: ""

    fun saveOptionTextSize(context: Context, sizeSp: Int) {
        prefs(context).edit().putInt(KEY_OPTION_TEXT_SIZE, sizeSp).apply()
    }

    fun getOptionTextSize(context: Context): Int =
        prefs(context).getInt(KEY_OPTION_TEXT_SIZE, 18)

    fun saveLastRating(context: Context, rating: Float) {
        prefs(context).edit().putFloat(KEY_LAST_RATING, rating).apply()
    }

    fun getLastRating(context: Context): Float =
        prefs(context).getFloat(KEY_LAST_RATING, 3f)
}

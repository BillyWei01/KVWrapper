package io.github.kvwrapper.util

import android.content.Context
import android.content.SharedPreferences
import io.github.kvwrapper.base.AppContext

class SpKV(name: String) {
    private val sp: SharedPreferences = AppContext.context.getSharedPreferences(name, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sp.edit()

    fun getAll(): MutableMap<String, *> {
        return sp.all
    }

    fun getString(key: String?, defValue: String?): String? {
        return sp.getString(key, defValue)
    }

    fun getStringSet(key: String?, defValues: Set<String>?): Set<String>? {
        return sp.getStringSet(key, defValues)
    }

    fun getInt(key: String?, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }

    fun getLong(key: String?, defValue: Long): Long {
        return sp.getLong(key, defValue)
    }

    fun getDouble(key: String?, defValue: Double): Double {
        return if (contains(key)) java.lang.Double.longBitsToDouble(
            getLong(key, 0L)
        ) else defValue
    }

    fun getFloat(key: String?, defValue: Float): Float {
        return sp.getFloat(key, defValue)
    }

    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return sp.getBoolean(key, defValue)
    }

    fun contains(key: String?): Boolean {
        return sp.contains(key)
    }

    fun putString(key: String?, value: String?) {
        editor.putString(key, value).apply()
    }

    fun putStringSet(key: String?, values: Set<String>?) {
        editor.putStringSet(key, values).apply()
    }

    fun putInt(key: String?, value: Int) {
        editor.putInt(key, value).apply()
    }

    fun putLong(key: String?, value: Long) {
        editor.putLong(key, value).apply()
    }

    fun putDouble(key: String?, value: Double): SharedPreferences.Editor {
        return editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
    }

    fun putFloat(key: String?, value: Float) {
        editor.putFloat(key, value).apply()
    }

    fun putBoolean(key: String?, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun remove(key: String?) {
        editor.remove(key).apply()
    }

    fun clear() {
        editor.clear().apply()
    }

    fun commit(): Boolean {
        return editor.commit()
    }

    fun apply() {
        editor.apply()
    }

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        sp.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        sp.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
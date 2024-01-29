package io.github.kvwrapper.kvbase

import android.content.Context
import android.content.SharedPreferences
import io.github.kvwrapper.base.AppContext
import io.github.kvwrapper.KVStore

/**
 * 以 [SharedPreferences] 为基础的 [KVStore] 的实现
 */
class SpKV(name: String): KVStore {
    private val sp: SharedPreferences = AppContext.context.getSharedPreferences(name, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sp.edit()

    override fun putBoolean(key: String, value: Boolean?) {
        if (value == null) {
            editor.remove(key).apply()
        } else {
            editor.putBoolean(key, value).apply()
        }
    }

    // 基本类型，要实现不存在value时返回null, 需要先调用 contains 判断
    override fun getBoolean(key: String): Boolean? {
        return if (sp.contains(key)) sp.getBoolean(key, false) else null
    }

    // 因此，如果有默认值，直接调用带默认值的方法会更有效率
    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sp.getBoolean(key, defValue)
    }

    override fun putInt(key: String, value: Int?) {
        if (value == null) {
            editor.remove(key).apply()
        } else {
            editor.putInt(key, value).apply()
        }
    }

    override fun getInt(key: String): Int? {
        return if (sp.contains(key)) sp.getInt(key, 0) else null
    }

    override fun getInt(key: String, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }

    override fun putFloat(key: String, value: Float?) {
        if (value == null) {
            editor.remove(key).apply()
        } else {
            editor.putFloat(key, value).apply()
        }
    }

    override fun getFloat(key: String): Float? {
        return if (sp.contains(key)) sp.getFloat(key, 0f) else null
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return sp.getFloat(key, defValue)
    }

    override fun putLong(key: String, value: Long?) {
        if (value == null) {
            editor.remove(key).apply()
        } else {
            editor.putLong(key, value).apply()
        }
    }

    override fun getLong(key: String): Long? {
        return if (sp.contains(key)) sp.getLong(key, 0L) else null
    }

    override fun getLong(key: String, defValue: Long): Long {
        return sp.getLong(key, defValue)
    }

    override fun putDouble(key: String, value: Double?) {
        if (value == null) {
            editor.remove(key).apply()
        } else {
            editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
        }
    }

    override fun getDouble(key: String): Double? {
        return if (sp.contains(key)) getDouble(key, 0.0) else null
    }

    override fun getDouble(key: String, defValue: Double): Double {
        return if (contains(key)) java.lang.Double.longBitsToDouble(
            getLong(key, 0L)
        ) else defValue
    }

    override fun putString(key: String, value: String?) {
        editor.putString(key, value).apply()
    }

    // getString要实现没有value时返回null, 不需要先判断contains, 直接defValue传null即可。
    override fun getString(key: String): String? {
        return sp.getString(key, null)
    }

    override fun putStringSet(key: String, value: Set<String>?) {
        editor.putStringSet(key, value).apply()
    }

    override fun getStringSet(key: String): Set<String>? {
        return sp.getStringSet(key, null)
    }

    fun remove(key: String) {
        editor.remove(key).apply()
    }

    fun contains(key: String): Boolean {
        return sp.contains(key)
    }
}
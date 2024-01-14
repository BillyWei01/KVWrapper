package io.github.kvwrapper.kvdelegate


import android.util.Base64
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//------------------------基础类型属性委托的实现-------------------------

class BooleanProperty(private val key: String, private val defValue: Boolean) :
    ReadWriteProperty<KVData, Boolean> {
    override fun getValue(thisRef: KVData, property: KProperty<*>): Boolean {
        return thisRef.kv.getBoolean(key, defValue)
    }

    override fun setValue(thisRef: KVData, property: KProperty<*>, value: Boolean) {
        thisRef.kv.putBoolean(key, value)
    }
}

class IntProperty(private val key: String, private val defValue: Int) :
    ReadWriteProperty<KVData, Int> {
    override fun getValue(thisRef: KVData, property: KProperty<*>): Int {
        return thisRef.kv.getInt(key, defValue)
    }

    override fun setValue(thisRef: KVData, property: KProperty<*>, value: Int) {
        thisRef.kv.putInt(key, value)
    }
}

class FloatProperty(private val key: String, private val defValue: Float) :
    ReadWriteProperty<KVData, Float> {
    override fun getValue(thisRef: KVData, property: KProperty<*>): Float {
        return thisRef.kv.getFloat(key, defValue)
    }

    override fun setValue(thisRef: KVData, property: KProperty<*>, value: Float) {
        thisRef.kv.putFloat(key, value)
    }
}

class LongProperty(private val key: String, private val defValue: Long) :
    ReadWriteProperty<KVData, Long> {
    override fun getValue(thisRef: KVData, property: KProperty<*>): Long {
        return thisRef.kv.getLong(key, defValue)
    }

    override fun setValue(thisRef: KVData, property: KProperty<*>, value: Long) {
        thisRef.kv.putLong(key, value)
    }
}

class DoubleProperty(private val key: String, private val defValue: Double) :
    ReadWriteProperty<KVData, Double> {
    override fun getValue(thisRef: KVData, property: KProperty<*>): Double {
        return thisRef.kv.getDouble(key, defValue)
    }

    override fun setValue(thisRef: KVData, property: KProperty<*>, value: Double) {
        thisRef.kv.putDouble(key, value)
    }
}

class StringProperty(private val key: String, private val defValue: String) :
    ReadWriteProperty<KVData, String> {
    override fun getValue(thisRef: KVData, property: KProperty<*>): String {
        return thisRef.kv.getString(key, null) ?: defValue
    }

    override fun setValue(thisRef: KVData, property: KProperty<*>, value: String) {
        thisRef.kv.putString(key, value)
    }
}

class StringSetProperty(private val key: String, private val defValue: Set<String>?) :
    ReadWriteProperty<KVData, Set<String>?> {
    override fun getValue(thisRef: KVData, property: KProperty<*>): Set<String>? {
        return thisRef.kv.getStringSet(key, defValue) ?: defValue
    }

    override fun setValue(thisRef: KVData, property: KProperty<*>, value: Set<String>?) {
        thisRef.kv.putStringSet(key, value)
    }
}

interface ObjectEncoder<T>{
    fun encode(obj: T?): String?

    fun decode(data: String?): T?
}

object ArrayEncoder : ObjectEncoder<ByteArray> {
    private const val BASE64_FLAGS = Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING

    override fun encode(obj: ByteArray?): String? {
        if(obj == null) return null
        return Base64.encodeToString(obj, BASE64_FLAGS)
    }

    override fun decode(data: String?): ByteArray? {
        if(data == null) return null
        return Base64.decode(data, BASE64_FLAGS)
    }
}

class ObjectProperty<T>(
    private val key: String,
    private val encoder: ObjectEncoder<T>,
    private val defValue: T?) :
    ReadWriteProperty<KVData, T?> {
    private var instance: T? = null

    override fun getValue(thisRef: KVData, property: KProperty<*>): T? {
        if (instance == null) {
            kotlin.runCatching {
                val value = thisRef.kv.getString(key, null)
                instance = encoder.decode(value) ?: defValue
            }
        }
        return instance
    }

    override fun setValue(thisRef: KVData, property: KProperty<*>, value: T?) {
        instance = value
        kotlin.runCatching {
            thisRef.kv.putString(key, encoder.encode(value))
        }
    }
}
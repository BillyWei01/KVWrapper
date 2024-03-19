package io.github.kvwrapper

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


//------------------------扩展key的基础类型-------------------------

/**
 * 基本类型的委托属性，定义时需传入常量的key，而开发中有时候需要【常量+变量】的key。
 * 为此，我们定义这个[CombineKVHandler]类, 通过动态代理的方式，。
 * 在调用put/get的过程中，[CombineKVHandler]拼接两级key，传递给[KVStore]实例。
 */
private class CombineKVHandler(
    private val kvStore: KVStore,  // KVData 所返回的 KVStore 实例
    private val key: String        // 一级key
) : InvocationHandler {
    companion object {
        private const val SEPARATOR = "--"

        /**
         * 代理类缓存 (value为代理类）
         */
        private val cache = HashMap<KVData, KVStore>()

        @Synchronized
        fun getProxy(kvData: KVData, key: String): KVStore {
            return cache.getOrPut(kvData) {
                val kv = kvData.kv
                val clazz = kv::class.java
                Proxy.newProxyInstance(
                    clazz.classLoader,
                    clazz.interfaces,
                    CombineKVHandler(kv, key)
                ) as KVStore
            }
        }
    }

    /**
     * 拼接两级key
     */
    private fun combineKey(extKey: Any): String {
        val extKeyStr = extKey.toString()
        return if (extKeyStr.isEmpty()) key else "$key${SEPARATOR}$extKeyStr"
    }

    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
        args[0] = combineKey(args[0])
        return method.invoke(kvStore, *args)
    }
}

private class LazyInitializer<T>(private val creator: (param: KVData) -> T) {
    @Volatile
    private var instance: T? = null

    fun get(param: KVData): T {
        val t = instance
        if (t != null) return t
        synchronized(this) {
            if (instance == null) {
                instance = creator.invoke(param)
            }
            return instance!!
        }
    }
}

class CombineKVProperty(private val key: String) : ReadOnlyProperty<KVData, KVStore> {
    private val kv = LazyInitializer { CombineKVHandler.getProxy(it, key) }

    override fun getValue(thisRef: KVData, property: KProperty<*>): KVStore {
        return kv.get(thisRef)
    }
}

//--------------------------------------------------------------------

/**
 * KVStore是比较灵活的API，可以get/set任意类型的value。
 * 但有时候我们想要约束【常量+变量】key所对应的value类型，
 * 故此，我们定义一些明确value类型的API。
 *
 * 在这些实现中，我们用 `operator` 修饰get和set方法；
 * 通过委托访问value时，语法如下：
 * ```
 * // 读取
 * val value = Foo.xxx[extKey]
 *
 * // 写入
 * Foo.xxx[extKey] = value
 * ```
 *
 * extKey可以任意类型的参数。
 * 需要指出，extKey 的 toString() 方法需返回稳定唯一的字符串。
 */
open class ExtProperty<T>(
    private val key: String,
    private val creator: (kv: KVStore) -> T
) : ReadOnlyProperty<KVData, T> {
    private val extKV = LazyInitializer { creator(CombineKVHandler.getProxy(it, key)) }

    override fun getValue(thisRef: KVData, property: KProperty<*>): T {
        return extKV.get(thisRef)
    }
}

//--------------------------------------------------------------------

class ExtBooleanProperty(key: String, defValue: Boolean) :
    ExtProperty<ExtBoolean>(key, { ExtBoolean(it, defValue) })

// 这里的 KVStore 是代理类
class ExtBoolean(private val kv: KVStore, private val defValue: Boolean) {
    operator fun get(extKey: Any): Boolean {
        return kv.getBoolean(extKey.toString(), defValue)
    }

    operator fun set(extKey: Any, value: Boolean) {
        kv.putBoolean(extKey.toString(), value)
    }
}

class ExtNullableBooleanProperty(key: String) :
    ExtProperty<ExtNullableBoolean>(key, { ExtNullableBoolean(it) })

class ExtNullableBoolean(private val kv: KVStore) {
    operator fun get(extKey: Any): Boolean? {
        return kv.getBoolean(extKey.toString())
    }

    operator fun set(extKey: Any, value: Boolean?) {
        kv.putBoolean(extKey.toString(), value)
    }
}

//--------------------------------------------------------------------

class ExtIntProperty(key: String, defValue: Int) :
    ExtProperty<ExtInt>(key, { ExtInt(it, defValue) })

class ExtInt(private val kv: KVStore, private val defValue: Int) {
    operator fun get(extKey: Any): Int {
        return kv.getInt(extKey.toString(), defValue)
    }

    operator fun set(extKey: Any, value: Int) {
        kv.putInt(extKey.toString(), value)
    }
}

class ExtNullableIntProperty(key: String) :
    ExtProperty<ExtNullableInt>(key, { ExtNullableInt(it) })

class ExtNullableInt(private val kv: KVStore) {
    operator fun get(extKey: Any): Int? {
        return kv.getInt(extKey.toString())
    }

    operator fun set(extKey: Any, value: Int?) {
        kv.putInt(extKey.toString(), value)
    }
}

//--------------------------------------------------------------------

class ExtFloatProperty(key: String, defValue: Float) :
    ExtProperty<ExtFloat>(key, { ExtFloat(it, defValue) })

class ExtFloat(private val kv: KVStore, private val defValue: Float) {
    operator fun get(extKey: Any): Float {
        return kv.getFloat(extKey.toString(), defValue)
    }

    operator fun set(extKey: Any, value: Float) {
        kv.putFloat(extKey.toString(), value)
    }
}

class ExtNullableFloatProperty(key: String) :
    ExtProperty<ExtNullableFloat>(key, { ExtNullableFloat(it) })

class ExtNullableFloat(private val kv: KVStore) {
    operator fun get(extKey: Any): Float? {
        return kv.getFloat(extKey.toString())
    }

    operator fun set(extKey: Any, value: Float?) {
        kv.putFloat(extKey.toString(), value)
    }
}

//--------------------------------------------------------------------

class ExtLongProperty(key: String, defValue: Long) :
    ExtProperty<ExtLong>(key, { ExtLong(it, defValue) })

class ExtLong(private val kv: KVStore, private val defValue: Long) {
    operator fun get(extKey: Any): Long {
        return kv.getLong(extKey.toString(), defValue)
    }

    operator fun set(extKey: Any, value: Long) {
        kv.putLong(extKey.toString(), value)
    }
}

class ExtNullableLongProperty(key: String) :
    ExtProperty<ExtNullableLong>(key, { ExtNullableLong(it) })

class ExtNullableLong(private val kv: KVStore) {
    operator fun get(extKey: Any): Long? {
        return kv.getLong(extKey.toString())
    }

    operator fun set(extKey: Any, value: Long?) {
        kv.putLong(extKey.toString(), value)
    }
}

//--------------------------------------------------------------------

class ExtDoubleProperty(key: String, defValue: Double) :
    ExtProperty<ExtDouble>(key, { ExtDouble(it, defValue) })

class ExtDouble(private val kv: KVStore, private val defValue: Double) {
    operator fun get(extKey: Any): Double {
        return kv.getDouble(extKey.toString(), defValue)
    }

    operator fun set(extKey: Any, value: Double) {
        kv.putDouble(extKey.toString(), value)
    }
}

class ExtNullableDoubleProperty(key: String) :
    ExtProperty<ExtNullableDouble>(key, { ExtNullableDouble(it) })

class ExtNullableDouble(private val kv: KVStore) {
    operator fun get(extKey: Any): Double? {
        return kv.getDouble(extKey.toString())
    }

    operator fun set(extKey: Any, value: Double?) {
        kv.putDouble(extKey.toString(), value)
    }
}

//--------------------------------------------------------------------

class ExtStringProperty(key: String, defValue: String) :
    ExtProperty<ExtString>(key, { ExtString(it, defValue) })

class ExtString(private val kv: KVStore, private val defValue: String) {
    operator fun get(extKey: Any): String {
        return kv.getString(extKey.toString()) ?: defValue
    }

    operator fun set(extKey: Any, value: String) {
        kv.putString(extKey.toString(), value)
    }
}

class ExtNullableStringProperty(key: String) :
    ExtProperty<ExtNullableString>(key, { ExtNullableString(it) })

class ExtNullableString(private val kv: KVStore) {
    operator fun get(extKey: Any): String? {
        return kv.getString(extKey.toString())
    }

    operator fun set(extKey: Any, value: String?) {
        kv.putString(extKey.toString(), value)
    }
}

//--------------------------------------------------------------------

class ExtStringSetProperty(key: String, defValue: Set<String>) :
    ExtProperty<ExtSetString>(key, { ExtSetString(it, defValue) })

class ExtSetString(private val kv: KVStore, private val defValue: Set<String>) {
    operator fun get(extKey: Any): Set<String> {
        return kv.getStringSet(extKey.toString()) ?: defValue
    }

    operator fun set(extKey: Any, value: Set<String>) {
        kv.putStringSet(extKey.toString(), value)
    }
}

class ExtNullableStringSetProperty(key: String) :
    ExtProperty<ExtSetNullableString>(key, { ExtSetNullableString(it) })

class ExtSetNullableString(private val kv: KVStore) {
    operator fun get(extKey: Any): Set<String>? {
        return kv.getStringSet(extKey.toString())
    }

    operator fun set(extKey: Any, value: Set<String>?) {
        kv.putStringSet(extKey.toString(), value)
    }
}


//--------------------------------------------------------------------

class ExtObjectProperty<T>(key: String, encoder: ObjectConverter<T>, defValue: T) :
    ExtProperty<ExtObject<T>>(key, { ExtObject(it, encoder, defValue) })

class ExtObject<T>(
    private val kv: KVStore,
    private val encoder: ObjectConverter<T>,
    private val defValue: T
) {
    private val cache by lazy { HashMap<String, T>() }

    @Synchronized
    operator fun get(extKey: Any): T {
        val keyStr = extKey.toString()
        val obj = cache[keyStr]
        if (obj != null) {
            return obj
        }
        val data = kv.getObject(keyStr, encoder)
        if (data != null) {
            cache[keyStr] = data
        }
        return data ?: defValue
    }

    @Synchronized
    operator fun set(extKey: Any, value: T) {
        val keyStr = extKey.toString()
        kv.putObject(keyStr, value, encoder)
        cache[keyStr] = value
    }
}

class ExtNullableObjectProperty<T>(key: String, encoder: ObjectConverter<T>) :
    ExtProperty<ExtNullableObject<T>>(key, { ExtNullableObject(it, encoder) })

class ExtNullableObject<T>(
    private val kv: KVStore,
    private val encoder: ObjectConverter<T>,
) {
    private val cache by lazy { HashMap<String, T>() }

    @Synchronized
    operator fun get(extKey: Any): T? {
        val keyStr = extKey.toString()
        val obj = cache[keyStr]
        if (obj != null) {
            return obj
        }
        val data = kv.getObject(keyStr, encoder)
        if (data != null) {
            cache[keyStr] = data
        }
        return data
    }

    @Synchronized
    operator fun set(extKey: Any, value: T?) {
        val keyStr = extKey.toString()
        kv.putObject(keyStr, value, encoder)
        if (value == null) {
            cache.remove(keyStr)
        } else {
            cache[keyStr] = value
        }
    }
}

//--------------------------------------------------------------------

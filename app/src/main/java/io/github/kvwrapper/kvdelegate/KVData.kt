package io.github.kvwrapper.kvdelegate

import io.github.kvwrapper.util.SpKV

abstract class KVData {
    abstract val kv: SpKV

    // 基础类型
    protected fun boolean(key: String, defValue: Boolean = false) = BooleanProperty(key, defValue)
    protected fun int(key: String, defValue: Int = 0) = IntProperty(key, defValue)
    protected fun float(key: String, defValue: Float = 0f) = FloatProperty(key, defValue)
    protected fun long(key: String, defValue: Long = 0L) = LongProperty(key, defValue)
    protected fun double(key: String, defValue: Double = 0.0) = DoubleProperty(key, defValue)
    protected fun string(key: String, defValue: String = "") = StringProperty(key, defValue)
    protected fun array(key: String, defValue: ByteArray = EMPTY_ARRAY) = ObjectProperty(key, ArrayEncoder, defValue)

    // 内置的对象类型
    protected fun stringSet(key: String, defValue: Set<String>? = null) = StringSetProperty(key, defValue)

    // 自定义对象类型
    protected fun <T> obj(key: String, encoder: ObjectEncoder<T>, defValue: T? = null) = ObjectProperty(key, encoder, defValue)

    // 枚举类型
    protected fun <T> stringEnum(key: String, converter: StringEnumConverter<T>) = StringEnumProperty(key, converter)
    protected fun <T> intEnum(key: String, converter: IntEnumConverter<T>) = IntEnumProperty(key, converter)

    // Map类型
    protected fun combineKey(key: String) = CombineKeyProperty(key)
    protected fun string2String(key: String) = StringToStringProperty(key)
    protected fun string2Set(key: String) = StringToSetProperty(key)
    protected fun string2Int(key: String) = StringToIntProperty(key)
    protected fun string2Boolean(key: String) = StringToBooleanProperty(key)
    protected fun int2Boolean(key: String) = IntToBooleanProperty(key)

    companion object {
        private val EMPTY_ARRAY = ByteArray(0)
    }
}
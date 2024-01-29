package io.github.kvwrapper.kvbase

import android.util.Base64
import io.github.kvwrapper.NullableObjectEncoder
import io.github.kvwrapper.ObjectEncoder


// 可根据需要内置一些常用的数据类型的编码器
// 这里以 byte[] 为例举例：

object ArrayEncoder : ObjectEncoder<ByteArray> {
    private const val BASE64_FLAGS = Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING

    override fun encode(obj: ByteArray): String {
        return Base64.encodeToString(obj, BASE64_FLAGS)
    }

    override fun decode(data: String): ByteArray {
        return Base64.decode(data, BASE64_FLAGS)
    }
}

object NullableArrayEncoder : NullableObjectEncoder<ByteArray> {
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
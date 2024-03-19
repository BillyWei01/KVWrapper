package io.github.kvwrapper.kvbase

import android.util.Base64
import io.github.kvwrapper.ObjectConverter


// 可根据需要内置一些常用的数据类型的编码器
// 这里以 byte[] 为例举例：

object ArrayConverter : ObjectConverter<ByteArray> {
    private const val BASE64_FLAGS = Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING

    override fun encode(obj: ByteArray): String {
        return Base64.encodeToString(obj, BASE64_FLAGS)
    }

    override fun decode(text: String): ByteArray {
        return Base64.decode(text, BASE64_FLAGS)
    }
}

object NullableArrayEncoder : ObjectConverter<ByteArray> {
    private const val BASE64_FLAGS = Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING

    override fun encode(obj: ByteArray): String {
        return Base64.encodeToString(obj, BASE64_FLAGS)
    }

    override fun decode(text: String): ByteArray {
        return Base64.decode(text, BASE64_FLAGS)
    }
}
package io.github.kvwrapper.util

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.Locale

object Utils {
    private val HEX_DIGITS = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'
    )

    fun getMD5(msg: ByteArray?): String {
        if (msg == null) {
            return ""
        }
        return bytes2Hex(getMD5Array(msg))
    }

    fun getMD5Array(msg: ByteArray?): ByteArray? {
        if (msg == null) return null
        try {
            return MessageDigest.getInstance("MD5").digest(msg)
        } catch (ignore: Exception) {
        }
        return Arrays.copyOf(msg, 16)
    }

    fun bytes2Hex(bytes: ByteArray?): String {
        if (bytes == null || bytes.isEmpty()) {
            return ""
        }
        val len = bytes.size
        val buf = CharArray(len shl 1)
        for (i in 0 until len) {
            val b = bytes[i].toInt()
            val index = i shl 1
            buf[index] = HEX_DIGITS[b shr 4 and 0xF]
            buf[index + 1] = HEX_DIGITS[b and 0xF]
        }
        return String(buf)
    }

    fun formatTime(t: Long): String? {
        val date = Date(t)
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sd.format(date)
    }
}

package io.github.kvwrapper.account

import io.github.kvwrapper.ObjectConverter


enum class Gender(private val value: Int) {
    UNKNOWN(0),
    MALE(1),
    FEMALE(2);

    companion object {
        fun intToType(value: Int?): Gender {
            return when (value) {
                1 -> MALE
                2 -> FEMALE
                else -> UNKNOWN
            }
        }

        val CONVERTER = object : ObjectConverter<Gender> {
            override fun encode(obj: Gender): String {
                return obj.value.toString()
            }

            override fun decode(text: String): Gender {
                return intToType(text.toIntOrNull())
            }
        }
    }
}
package io.github.kvwrapper.account

import androidx.annotation.Keep
import com.google.gson.Gson
import io.github.kvwrapper.NullableObjectEncoder
import java.io.Serializable

@Keep
data class AccountInfo(
    val uid: Long,
    val token: String,
    val nickname: String,
    val phoneNo: String,
    val email: String
) : Serializable {
    companion object {
        val ENCODER = object : NullableObjectEncoder<AccountInfo> {
            override fun encode(obj: AccountInfo?): String? {
                if (obj == null) return null
                return Gson().toJson(obj)
            }

            override fun decode(data: String?): AccountInfo? {
                if (data == null) return null
                return Gson().fromJson(data, AccountInfo::class.java)
            }
        }
    }
}
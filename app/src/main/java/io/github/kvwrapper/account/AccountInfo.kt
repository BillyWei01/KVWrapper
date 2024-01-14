package io.github.kvwrapper.account

import com.google.gson.Gson
import io.github.kvwrapper.kvdelegate.ObjectEncoder
import java.io.Serializable

data class AccountInfo(
    var uid: Long,
    var token: String,
    var nickname: String,
    var phoneNo: String,
    var email: String
) : Serializable {
    companion object {
        val ENCODER = object : ObjectEncoder<AccountInfo> {
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
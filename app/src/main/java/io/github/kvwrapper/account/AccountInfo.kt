package io.github.kvwrapper.account

import androidx.annotation.Keep
import com.google.gson.Gson
import io.github.kvwrapper.ObjectConverter
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
        val ENCODER = object : ObjectConverter<AccountInfo> {
            override fun encode(obj: AccountInfo): String {
                return Gson().toJson(obj)
            }

            override fun decode(text: String): AccountInfo {
                return Gson().fromJson(text, AccountInfo::class.java)
            }
        }
    }
}
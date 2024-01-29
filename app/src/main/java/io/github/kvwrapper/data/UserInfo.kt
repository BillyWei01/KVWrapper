package io.github.kvwrapper.data

import android.util.ArrayMap
import io.github.kvwrapper.account.AccountInfo
import io.github.kvwrapper.account.Gender
import io.github.kvwrapper.base.AppContext
import io.github.kvwrapper.kvbase.NullableArrayEncoder
import io.github.kvwrapper.kvbase.UserKV


/**
 * 用户信息
 */
class UserInfo(uid: Long) : UserKV("user_info", uid) {
    companion object {
        private val map = ArrayMap<Long, UserInfo>()

        fun get(): UserInfo {
            return get(AppContext.uid)
        }

        @Synchronized
        fun get(uid: Long): UserInfo {
            return map.getOrPut(uid) {
                UserInfo(uid)
            }
        }
    }

    var userAccount by nullableObj("user_account", AccountInfo.ENCODER)
    var gender by obj("gender", Gender.CONVERTER, Gender.UNKNOWN)
    var isVip by boolean("is_vip")
    var fansCount by int("fans_count")
    var score by float("score")
    var loginTime by long("login_time")
    var balance by double("balance")
    var sign by string("sing")
    var lock by nullableObj("lock", NullableArrayEncoder)
    var tags by stringSet("tags")
    val favorites by extStringSet("favorites")
    val config by combineKV("config")
}

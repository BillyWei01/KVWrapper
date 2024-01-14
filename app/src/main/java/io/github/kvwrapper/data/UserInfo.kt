package io.github.kvwrapper.data

import android.util.ArrayMap
import io.github.kvwrapper.account.AccountInfo
import io.github.kvwrapper.account.Gender
import io.github.kvwrapper.base.AppContext
import io.github.kvwrapper.kvbase.UserKV
import io.github.kvwrapper.kvdelegate.ObjectProperty


/**
 * 用户信息
 */
class UserInfo(uid: Long) : UserKV("user_info", uid) {
    companion object {
        private val map = ArrayMap<Long, UserInfo>()

        @Synchronized
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

    var userAccount by obj("user_account", AccountInfo.ENCODER)
    var gender by intEnum("gender", Gender.CONVERTER)
    var isVip by boolean("is_vip")
    var fansCount by int("fans_count")
    var score by float("score")
    var loginTime by long("login_time")
    var balance by double("balance")
    var sign by string("sing")
    var lock by array("lock")
    var tags by stringSet("tags")
    val favorites by string2Set("favorites")
    val config by combineKey("config")
}

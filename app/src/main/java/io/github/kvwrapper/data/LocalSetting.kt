package io.github.kvwrapper.data

import android.util.Log
import io.github.kvwrapper.account.Gender
import io.github.kvwrapper.kvbase.GlobalKV


/**
 * App本地设置（配置项）
 * 离线数据，不需要同步，不需要区分环境
 */
object LocalSetting : GlobalKV("local_setting") {
    // 是否开启开发者入口
    var enableDeveloper by boolean("enable_developer")

    // 用户ID
    var userId by long("user_id")

    // id -> name 的映射 （演示扩展类型的用法）
    val idToName by extNullableString("id_to_name")

    // 收藏
    val favorites by extStringSet("favorites")

    // 性别（演示对象类型声明：参数）
    var gender by obj("gender", Gender.CONVERTER, Gender.UNKNOWN)
}


// 基本类型的读写
fun test1(){
    // 写入
    LocalSetting.userId = 10001L
    LocalSetting.gender = Gender.FEMALE

    // 读取
    val uid = LocalSetting.userId
    val gender = LocalSetting.gender
}

// 扩展key的基本类型的读写
fun test2() {
    if (LocalSetting.idToName[1] == null || LocalSetting.idToName[2] == null) {
        Log.d("TAG", "Put values to idToName")
        LocalSetting.idToName[1] = "Jonn"
        LocalSetting.idToName[2] = "Mary"
    } else {
        Log.d("TAG", "There are values in idToName")
    }
    Log.d("TAG", "idToName values: " +
                "1 -> ${LocalSetting.idToName[1]}, " +
                "2 -> ${LocalSetting.idToName[2]}"
    )
}
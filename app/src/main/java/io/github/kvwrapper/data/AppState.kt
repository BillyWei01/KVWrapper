package io.github.kvwrapper.data

import io.github.kvwrapper.base.Env
import io.github.kvwrapper.kvbase.GlobalKV
import io.github.kvwrapper.kvbase.UserKV


/**
 * APP信息
 */
object AppState : GlobalKV("app_state") {
    /**
     * 服务器环境
     *
     * 这个信息时作为[UserKV]实例的路径参数之一，决定了只能用[GlobalKV]来存储，否则就循环依赖了
     */
    var environment by stringEnum("environment", Env.CONVERTER)

    /**
     * 用户ID
     */
    var userId by long("user_id")

    /**
     * 设备ID
     */
    var deviceId by string("device_id")
}
package io.github.kvwrapper.kvbase

import io.github.kvwrapper.base.AppContext
import io.github.kvwrapper.KVData
import io.github.kvwrapper.KVStore
import io.github.kvwrapper.util.Utils

/**
 * 用户数据存储
 *
 * 需要同时区分 “环境“ 和 ”用户“。
 */
abstract class UserKV(
    private val name: String,
    private val userId: Long
) : KVData() {
    override val kv: KVStore by lazy {
        val fileName = "${name}_${userId}_${AppContext.env.tag}"
        if (AppContext.debug) {
            SpKV(fileName)
        } else {
            // 如果是release包，可以对文件名做个md5，以便匿藏uid等信息
            SpKV(Utils.getMD5(fileName.toByteArray()))
        }
    }
}

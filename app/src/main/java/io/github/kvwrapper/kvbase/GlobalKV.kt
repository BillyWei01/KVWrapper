package io.github.kvwrapper.kvbase

import io.github.kvwrapper.KVData
import io.github.kvwrapper.KVStore

/**
 * 全局数据
 *
 * 切换环境和用户，不影响GlobalKV所访问的数据实例。
 */
open class GlobalKV(name: String) : KVData() {
    override val kv: KVStore by lazy {
        SpKV(name)
    }
}
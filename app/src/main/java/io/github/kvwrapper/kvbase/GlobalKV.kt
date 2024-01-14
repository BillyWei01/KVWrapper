package io.github.kvwrapper.kvbase

import io.github.kvwrapper.kvdelegate.KVData
import io.github.kvwrapper.util.SpKV

/**
 * 全局数据
 *
 * 切换环境和用户，不影响GlobalKV所访问的数据实例。
 */
open class GlobalKV(name: String) : KVData() {
    override val kv: SpKV by lazy {
        SpKV(name)
    }
}
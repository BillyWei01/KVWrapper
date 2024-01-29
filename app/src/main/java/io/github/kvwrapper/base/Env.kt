package io.github.kvwrapper.base

import io.github.kvwrapper.ObjectEncoder


/**
 * Server Environment
 *
 * 命名为ServerEnvironment又太长，
 * 命名为Environment又太多重名的类，
 * 所以简化命名为: Env。
 *
 * 不同的团队，对于环境的命名不相同：
 * - 有的团队命名为：线上环境/开发环境；
 * - 有的团队命名为：生产环境/上线前环境。
 *
 * 具体命名按照团队命名习惯即可。
 */
enum class Env(val tag: String) {
    /**
     * Post-Production Environment, 生产环境
     */
    PPE("ppe"),

    /**
     * Before Online Environment，上线前环境
     */
    BOE("boe");

    companion object {
        val CONVERTER = object : ObjectEncoder<Env> {
            override fun encode(obj: Env): String {
                return obj.tag
            }

            override fun decode(data: String): Env {
                return if (data == BOE.tag) BOE else PPE
            }
        }
    }
}
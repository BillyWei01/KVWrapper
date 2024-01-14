package io.github.kvwrapper.application

import android.app.Application
import android.content.Context
import io.github.kvwrapper.BuildConfig
import io.github.kvwrapper.base.AppContext
import io.github.kvwrapper.base.IAppContext

class AppApplication : Application(), IAppContext {
    override val context: Context
        get() = this

    override val debug: Boolean
        get() = BuildConfig.DEBUG

    override val isMainProcess: Boolean
        get() = appId == null || appId == BuildConfig.APPLICATION_ID

    private var appId: String? = null

    override fun onCreate() {
        super.onCreate()
        // 第一件事，先初始APP上下文
        AppContext.init(this)

        appId = ProcessUtil.getProcessName(this)
    }
}


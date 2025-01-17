package com.sample.tmdb.feature_webview

import android.content.Context
import org.mozilla.geckoview.GeckoRuntime

object GeckoRuntimeHolder {
    private var geckoRuntime: GeckoRuntime? = null

    fun getOrCreate(context: Context): GeckoRuntime {
        if (geckoRuntime == null) {
            geckoRuntime = GeckoRuntime.create(context.applicationContext)
        }
        return geckoRuntime!!
    }
}

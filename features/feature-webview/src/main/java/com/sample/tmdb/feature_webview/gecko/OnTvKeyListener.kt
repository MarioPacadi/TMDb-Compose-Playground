package com.sample.tmdb.feature_webview.gecko

import android.view.KeyEvent
import android.view.View
import org.mozilla.geckoview.AllowOrDeny
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoSession

//class OnKeyListener(private val geckoSession: GeckoSession) : View.OnKeyListener {
//
//    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
//        if (event != null && event.action == KeyEvent.ACTION_DOWN) {
//            when (keyCode) {
//                KeyEvent.KEYCODE_DPAD_UP -> {
//                    handleUpKey()
//                    return true
//                }
//
//                KeyEvent.KEYCODE_DPAD_DOWN -> {
//                    handleDownKey()
//                    return true
//                }
//
//                KeyEvent.KEYCODE_DPAD_LEFT -> {
//                    handleLeftKey()
//                    return true
//                }
//
//                KeyEvent.KEYCODE_DPAD_RIGHT -> {
//                    handleRightKey()
//                    return true
//                }
//
//                KeyEvent.KEYCODE_DPAD_CENTER -> {
//                    handleSelectKey()
//                    return true
//                }
//            }
//        }
//        return false
//    }
//}

class OnKeyListener(private val geckoSession: GeckoSession) : View.OnKeyListener {

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null && event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_CENTER -> {
                    // Trigger click inside iframe when "Select" key is pressed
//                    handleIframeClick()
                    return true
                }
            }
        }
        return false
    }
}
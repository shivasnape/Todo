package com.shivichu.passwordsaver.utils

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

open class BaseDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusAndNavBars()
        transparentStatusAndNavigation()
    }

    private fun hideStatusAndNavBars() {
        dialog?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    open fun transparentStatusAndNavigation() {
        // make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true
            )
        }
        if (Build.VERSION.SDK_INT >= 19) {
            var visibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            visibility = visibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            dialog?.window?.decorView?.setSystemUiVisibility(visibility)
        }

        if (Build.VERSION.SDK_INT >= 21) {
            var windowManagerLytParams = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            windowManagerLytParams =
                windowManagerLytParams or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION //or WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            setWindowFlag(windowManagerLytParams, false)
            dialog?.window?.statusBarColor = Color.TRANSPARENT
            dialog?.window?.navigationBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win: Window? = dialog?.window
        val winParams: WindowManager.LayoutParams =
            win?.getAttributes() ?: WindowManager.LayoutParams()
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        if (win != null) {
            win.setAttributes(winParams)
        }
    }
}
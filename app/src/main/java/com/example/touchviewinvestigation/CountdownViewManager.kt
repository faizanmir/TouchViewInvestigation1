package com.example.touchviewinvestigation

import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.Gravity
import android.view.WindowManager

object CountdownViewManager   {

    private var isCountingDown = false
    private var countdownView: CountdownView? = null

    @Suppress("DEPRECATION")
    @Synchronized
    fun showCountdown(ctx: Context) {
        if (isCountingDown) {
            return
        }
        Log.d(TAG,"[CountdownViewManager][showCountdown] start")
        val params = WindowManager.LayoutParams()
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        params.format = PixelFormat.TRANSLUCENT
        params.flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        params.gravity = Gravity.TOP
        params.height = 1
        params.width = 1
        val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        countdownView = CountdownView(ctx)
        wm.addView(countdownView, params)
        isCountingDown = true
        Log.d(TAG,"[CountdownViewManager][showCountdown] end")
    }

    @Synchronized
    fun removeCountdown(ctx: Context) {
        val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        try {
            if (!isCountingDown) {
                return
            }
            //countdownView.cancelTime();
            Log.d(TAG,"[CountdownViewManager][removeCountdown] removing")
            wm.removeView(countdownView)
            isCountingDown = false
        } catch (e: Exception) {
            Log.d(TAG, "[CountdownViewManager][removeCountdown] Exception", e)
        }
    }

    private const val TAG = "CountdownViewManager"
}
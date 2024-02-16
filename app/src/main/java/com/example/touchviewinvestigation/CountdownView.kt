package com.example.touchviewinvestigation

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * User: jfan
 * Date: 4/29/2015
 */
class CountdownView(ctx: Context) : LinearLayout(
    ctx
) {
    private var extended = false
    private var gestureDetector: GestureDetector

    init {
        setBackgroundColor(Color.BLACK)
        gestureDetector = GestureDetector(ctx, CountdownGestureDetector())
        this.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    internal inner class CountdownGestureDetector : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            extended = true
            return true
        }



        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            extended = true
            return true
        }



        override fun onDown(e: MotionEvent): Boolean {
            extended = true
            return true
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e(TAG,"Event detected ${event}", )
        return if (event.action == MotionEvent.ACTION_OUTSIDE) {
          //  LOGGER.info("[CountdownView][onTouchEvent] reset timer")
//            val intent = Intent(context, MonitorService::class.java)
//            intent.action = Actions.ACTION_RESET_TIMER
//            context.startService(intent)
            true
        } else {
            true
        }
    }

    companion object {
        private const val TAG = "CountdownView"
    }
}
package com.sample.tmdb.feature_webview.gecko

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.os.Build
import android.os.SystemClock
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.sample.tmdb.feature_webview.gecko.GeckoViewWithVirtualCursor.Companion
import org.mozilla.geckoview.GeckoView
import org.mozilla.geckoview.ScreenLength
import kotlin.math.abs

// Inherit from GeckoView, as before
@Suppress("NAME_SHADOWING")
class GeckoViewWithVirtualCursorV2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null):
    GeckoView(context, attrs) {

    companion object {
        private const val CURSOR_DISAPPEAR_TIMEOUT = 5000
        private const val SCROLL_HACK_PADDING = 300
        private const val UNCHANGED = Integer.MIN_VALUE
    }

    private var inputMethodManager: InputMethodManager? = null
    private var cursorRadius: Int = 0
    private var cursorRadiusPressed: Int = 0
    private var maxCursorSpeed: Float = 0f
    private var scrollStartPadding = 100
    private var cursorStrokeWidth: Float = 0f
    private val cursorDirection = Point(0, 0)
    private val cursorPosition = PointF(0f, 0f)
    private val cursorSpeed = PointF(0f, 0f)
    private val paint = Paint()
    private var lastCursorUpdate = System.currentTimeMillis() - CURSOR_DISAPPEAR_TIMEOUT
    private var dpadCenterPressed = false
    internal var tmpPointF = PointF()
    private var callback: Callback? = null

    // Timer for hiding the cursor
    private val cursorHideRunnable = Runnable { invalidate() }

    init {
        init()
    }

    @SuppressLint("NewApi")
    private fun init() {
        if (isInEditMode) {
            return
        }

        paint.isAntiAlias = true
        setWillNotDraw(false)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val (width, height) = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val size = Point()
            wm.defaultDisplay.getSize(size) // This is deprecated in API 30
            size.x to size.y
        } else {
            val windowMetrics = wm.currentWindowMetrics
            val bounds = windowMetrics.bounds
            bounds.width() to bounds.height()
        }

        cursorStrokeWidth = (width / 400).toFloat()
        cursorRadius = width / 110
        cursorRadiusPressed = cursorRadius + d2p(context, 5f).toInt()
        maxCursorSpeed = (width / 25).toFloat()
        scrollStartPadding = width / 15
        overScrollMode = OVER_SCROLL_NEVER
        inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun onSizeChanged(w: Int, h: Int, ow: Int, oh: Int) {
        super.onSizeChanged(w, h, ow, oh)
        if (isInEditMode) {
            return
        }
        cursorPosition.set(w / 2.0f, h / 2.0f)
        postDelayed(cursorHideRunnable, CURSOR_DISAPPEAR_TIMEOUT.toLong())
    }

    // This method is where the cursor will be drawn.
    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (isInEditMode) return
        val canvas = canvas ?: return

        // Draw the cursor
        if (!isCursorDissappear) {
            val cx = cursorPosition.x
            val cy = cursorPosition.y
            val radius = if (dpadCenterPressed) cursorRadiusPressed else cursorRadius

            paint.color = Color.argb(128, 255, 255, 255)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(cx, cy, radius.toFloat(), paint)

            paint.color = Color.GRAY
            paint.strokeWidth = cursorStrokeWidth
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(cx, cy, radius.toFloat(), paint)
        }
    }

    private val cursorUpdateRunnable = object : Runnable {
        override fun run() {
            removeCallbacks(cursorHideRunnable)

            val newTime = System.currentTimeMillis()
            val dTime = newTime - lastCursorUpdate
            lastCursorUpdate = newTime

            val accelerationFactor = 0.05f * dTime

            cursorSpeed.set(
                bound(cursorSpeed.x + bound(cursorDirection.x.toFloat(), 1f) * accelerationFactor, maxCursorSpeed),
                bound(cursorSpeed.y + bound(cursorDirection.y.toFloat(), 1f) * accelerationFactor, maxCursorSpeed)
            )

            if (abs(cursorSpeed.x) < 0.1f) cursorSpeed.x = 0f
            if (abs(cursorSpeed.y) < 0.1f) cursorSpeed.y = 0f

            if (cursorDirection.x == 0 && cursorDirection.y == 0 && cursorSpeed.x == 0f && cursorSpeed.y == 0f) {
                postDelayed(cursorHideRunnable, CURSOR_DISAPPEAR_TIMEOUT.toLong())
                return
            }

            tmpPointF.set(cursorPosition)
            cursorPosition.offset(cursorSpeed.x, cursorSpeed.y)

            // Ensure cursor is within bounds
            cursorPosition.x = cursorPosition.x.coerceIn(0f, width.toFloat() - 1)
            cursorPosition.y = cursorPosition.y.coerceIn(0f, height.toFloat() - 1)

            if (tmpPointF != cursorPosition) {
                if (dpadCenterPressed) {
                    dispatchMotionEvent(cursorPosition.x, cursorPosition.y, MotionEvent.ACTION_MOVE)
                }
            }

            // Post update to keep cursor moving
            post(this)
        }
    }

    private fun dispatchMotionEvent(x: Float, y: Float, action: Int) {
        val eventTime = SystemClock.uptimeMillis()
        val motionEvent = MotionEvent.obtain(
            eventTime, eventTime, action, 1, arrayOfNulls<MotionEvent.PointerProperties>(1), arrayOf(
                MotionEvent.PointerCoords().apply {
                    this.x = x
                    this.y = y
                    pressure = 1f
                    size = 1f
                }
            ), 0, 0, 1f, 1f, 0, 0, 0, 0
        )
        dispatchTouchEvent(motionEvent)
    }

    private fun bound(value: Float, max: Float): Float {
        return value.coerceIn(-max, max)
    }

    private val isCursorDissappear: Boolean
        get() {
            val newTime = System.currentTimeMillis()
            return newTime - lastCursorUpdate > CURSOR_DISAPPEAR_TIMEOUT
        }

    interface Callback {
        fun onUserInteraction()
    }

    // Update the dispatchKeyEvent to handle DPAD inputs for cursor movement
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        callback?.onUserInteraction()

        when (event.keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    cursorDirection.x = -1
                } else if (event.action == KeyEvent.ACTION_UP) {
                    cursorDirection.x = 0
                }
                return true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    cursorDirection.x = 1
                } else if (event.action == KeyEvent.ACTION_UP) {
                    cursorDirection.x = 0
                }
                return true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    cursorDirection.y = -1
                } else if (event.action == KeyEvent.ACTION_UP) {
                    cursorDirection.y = 0
                }
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    cursorDirection.y = 1
                } else if (event.action == KeyEvent.ACTION_UP) {
                    cursorDirection.y = 0
                }
                return true
            }
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    dispatchMotionEvent(cursorPosition.x, cursorPosition.y, MotionEvent.ACTION_DOWN)
                } else if (event.action == KeyEvent.ACTION_UP) {
                    dispatchMotionEvent(cursorPosition.x, cursorPosition.y, MotionEvent.ACTION_UP)
                }
                return true
            }
        }
        return super.dispatchKeyEvent(event)
    }
}

package com.example.nikitabobkov.customviewexample

import android.content.Context
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup

class HoneycombLayout : ViewGroup {
    private var amount = 0
    private var widthView = 0
    private var heightView = 0
    private var honeycombRadius = 0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.HoneycombLayout, 0, 0)
        amount = typedArray.getInteger(R.styleable.HoneycombLayout_hcl_amount, 25)
        honeycombRadius = typedArray.getFloat(R.styleable.HoneycombLayout_hcl_radius, 0f)
        typedArray.recycle()
        addHoneycombs()
    }

    private fun addHoneycombs() {
        removeAllViews()
        for (i in 0..amount) {
            val view = HoneycombButton(context)
            view.setText(i.toString())
            honeycombRadius = if (honeycombRadius > 0) honeycombRadius else DEFAULT_RADIUS
            view.setRadius(honeycombRadius)
            addView(view)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthView = MeasureSpec.getSize(widthMeasureSpec)
        heightView = MeasureSpec.getSize(heightMeasureSpec)
        for (i in 0 until childCount) {
            measureChild(getChildAt(i), (honeycombRadius * 2).toInt(), (honeycombRadius * 2).toInt())
        }
        setMeasuredDimension(widthView, heightView)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var xLeftTop = 0
        var yLeftTop = 0
        var xRightBottom = 0
        var yRightBottom: Int
        var linesCounter = 0
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            xRightBottom += view.measuredWidth
            if (xRightBottom > width) {
                linesCounter++
                xRightBottom = if (linesCounter % 2 != 0) view.measuredWidth / 2 else 0
                xLeftTop = if (linesCounter % 2 != 0) view.measuredWidth / 2 else 0
                xRightBottom += view.measuredWidth
                yLeftTop += (honeycombRadius * 2).toInt()
            }
            yRightBottom = yLeftTop + view.measuredHeight
            if (yRightBottom > height) {
                return
            }
            view.layout(xLeftTop, yLeftTop, xRightBottom, yRightBottom)
            xLeftTop += view.measuredWidth
        }
    }

    fun setHoneycombRadius(radius: Float) {
        honeycombRadius = radius
        requestLayout()
    }

    fun setHoneycombAmount(amount: Int) {
        this.amount = amount
        addHoneycombs()
        invalidate()
    }

    fun setOnClickListener(listener: OnHoneycombClickListener) {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is HoneycombButton) {
                view.setOnClickListener(listener)
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}
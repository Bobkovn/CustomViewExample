package com.example.nikitabobkov.customviewexample

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

const val DEFAULT_RADIUS = 100f

class HoneycombLayout : ViewGroup {
    private var amount = 0
    private var widthView = 0
    private var heightView = 0
    private var honeycombRadius = 0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        addHoneycombs()
    }

    private fun addHoneycombs() {
        removeAllViews()
        for (i in amount downTo 0) {
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
        for (i in amount downTo 0) {
            measureChild(getChildAt(i), (honeycombRadius * 2).toInt(), (honeycombRadius * 2).toInt())
        }
        setMeasuredDimension(widthView, heightView)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var xLeftTop = 0
        var yLeftTop = 0
        var xRightBottom = 0
        var yRightBottom = 0
        var linesCounter = 0

        for (i in amount downTo 0) {
            val view = getChildAt(i)
            view.layout(xLeftTop, yLeftTop, xRightBottom + view.measuredWidth, yRightBottom + view.measuredHeight)
            xLeftTop += view.measuredWidth
            xRightBottom += view.measuredWidth
            if (xRightBottom > width) {
                linesCounter++
                xRightBottom = if (linesCounter % 2 != 0) view.measuredWidth / 2 else 0
                xLeftTop = if (linesCounter % 2 != 0) view.measuredWidth / 2 else 0
                yLeftTop += (honeycombRadius * 2).toInt()
                yRightBottom += (honeycombRadius * 2).toInt()
            }
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
}
package com.example.nikitabobkov.customviewexample

import android.content.Context
import android.hardware.SensorManager
import android.util.AttributeSet
import android.view.ViewGroup

class HoneycombLayout : ViewGroup {
    private var amount: Int = 0
    private var widthView: Int = 0
    private var heightView: Int = 0

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
            view.setRadius(200f)
            addView(view)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthView = MeasureSpec.getSize(widthMeasureSpec)
        heightView = MeasureSpec.getSize(heightMeasureSpec)
        // measure child
        measureChild(getChildAt(0), 300, 300)
        setMeasuredDimension(widthView, heightView)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val view = getChildAt(0)
        view.layout(200, 200, 500, 500)

    }

    fun setHoneycombAmount(amount: Int) {
        this.amount = amount
        addHoneycombs()
        invalidate()
    }
}
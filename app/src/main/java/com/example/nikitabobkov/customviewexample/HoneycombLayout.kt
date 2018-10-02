package com.example.nikitabobkov.customviewexample

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class HoneycombLayout : ViewGroup {
    private var amount: Int = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

    }

    fun setHoneycombAmount(amount: Int) {
        this.amount = amount
        invalidate()
    }
}
package com.example.nikitabobkov.customviewexample

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View



class HoneycombsButton : View {
    private lateinit var hexagonPath: Path
    private lateinit var hexagonBorderPath: Path
    private lateinit var paint: Paint
    private var radius: Float = 0.0f
    private val maskColor: Int = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        paint = Paint()
        hexagonPath = Path()
        hexagonBorderPath = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
        radius = Math.min(width / 2f, height / 2f) - 10f
        calculatePath(radius)
    }

    private fun calculatePath(radius: Float) {
        val halfRadius = radius / 2f
        val triangleHeight = (Math.sqrt(3.0) * halfRadius).toFloat()
        val centerX = measuredWidth / 2f
        val centerY = measuredHeight / 2f

        this.hexagonPath.reset()
        this.hexagonPath.moveTo(centerX, centerY + radius)
        this.hexagonPath.lineTo(centerX - triangleHeight, centerY + halfRadius)
        this.hexagonPath.lineTo(centerX - triangleHeight, centerY - halfRadius)
        this.hexagonPath.lineTo(centerX, centerY - radius)
        this.hexagonPath.lineTo(centerX + triangleHeight, centerY - halfRadius)
        this.hexagonPath.lineTo(centerX + triangleHeight, centerY + halfRadius)
        this.hexagonPath.close()

        val radiusBorder = radius - 5f
        val halfRadiusBorder = radiusBorder / 2f
        val triangleBorderHeight = (Math.sqrt(3.0) * halfRadiusBorder).toFloat()

        this.hexagonBorderPath.reset()
        this.hexagonBorderPath.moveTo(centerX, centerY + radiusBorder)
        this.hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY + halfRadiusBorder)
        this.hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY - halfRadiusBorder)
        this.hexagonBorderPath.lineTo(centerX, centerY - radiusBorder)
        this.hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY - halfRadiusBorder)
        this.hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY + halfRadiusBorder)
        this.hexagonBorderPath.close()
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(hexagonBorderPath, paint)
        canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    }
}
package com.example.nikitabobkov.customviewexample

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class HoneycombButton : View {
    private lateinit var hexagonPath: Path
    private lateinit var hexagonBorderPath: Path
    private lateinit var borderPaint: Paint
    private lateinit var textPaint: Paint
    private var radius: Float = 0.0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        hexagonPath = Path()
        hexagonBorderPath = Path()
        textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 20f

        borderPaint = Paint()
        borderPaint.color = Color.BLACK
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.strokeWidth = 50f
        borderPaint.style = Paint.Style.STROKE
    }

    fun setRadius(radius: Float) {
        calculatePath(radius)
    }

    fun setBorderColor(color: Int) {
        this.borderPaint.color = color
        invalidate()
    }

    fun setText(text: String) {

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

        hexagonPath.reset()
        hexagonPath.moveTo(centerX, centerY + radius)
        hexagonPath.lineTo(centerX - triangleHeight, centerY + halfRadius)
        hexagonPath.lineTo(centerX - triangleHeight, centerY - halfRadius)
        hexagonPath.lineTo(centerX, centerY - radius)
        hexagonPath.lineTo(centerX + triangleHeight, centerY - halfRadius)
        hexagonPath.lineTo(centerX + triangleHeight, centerY + halfRadius)
        hexagonPath.close()

        val radiusBorder = radius - 5f
        val halfRadiusBorder = radiusBorder / 2f
        val triangleBorderHeight = (Math.sqrt(3.0) * halfRadiusBorder).toFloat()

        hexagonBorderPath.reset()
        hexagonBorderPath.moveTo(centerX, centerY + radiusBorder)
        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY + halfRadiusBorder)
        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY - halfRadiusBorder)
        hexagonBorderPath.lineTo(centerX, centerY - radiusBorder)
        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY - halfRadiusBorder)
        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY + halfRadiusBorder)
        hexagonBorderPath.close()
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(hexagonBorderPath, borderPaint)
        canvas?.clipPath(hexagonPath)
        canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        canvas?.drawText("Some Text", measuredWidth / 2f, measuredHeight / 2f, textPaint)
    }
}
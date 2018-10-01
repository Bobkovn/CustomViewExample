package com.example.nikitabobkov.customviewexample

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View

class HoneycombButton : View {
    private lateinit var hexagonPath: Path
    private lateinit var hexagonBorderPath: Path
    private lateinit var borderPaint: Paint
    private lateinit var textPaint: TextPaint
    private var radius: Float = 0.0f
    private var widthView: Int = 0
    private var heightView: Int = 0
    private var widthHoneycomb: Int = 0
    private var heightHoneycomb: Int = 0
    private var text: String = "vfdjkvdfdfsdfdsfsdfs"
    private var bounds: Rect = Rect()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        hexagonPath = Path()
        hexagonBorderPath = Path()
        textPaint = TextPaint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 150f

        borderPaint = Paint()
        borderPaint.color = Color.RED
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
        this.text = text
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthView = MeasureSpec.getSize(widthMeasureSpec)
        heightView = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthView, heightView)
        radius = Math.min(widthView / 2f, heightView / 2f)
        calculatePath(radius)
        calculateTextSize()
    }

    private fun calculateTextSize() {
        text = TextUtils.ellipsize(text, textPaint, widthHoneycomb.toFloat(), TextUtils.TruncateAt.END) as String
        textPaint.getTextBounds(text, 0, text.length - 1, bounds)
    }

    private fun calculatePath(radius: Float) {
        val halfRadius = radius / 2f
        val triangleHeight = (Math.sqrt(3.0) * halfRadius).toFloat()
        val centerX = measuredWidth / 2f
        val centerY = measuredHeight / 2f
        widthHoneycomb = (triangleHeight * 2).toInt()
        heightHoneycomb = (radius * 2).toInt()

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

        val xPos = (widthView - textPaint.textSize * Math.abs(text.length / 2)) / 2

        val yPos = (canvas?.height!! / 2 - (textPaint.descent() + textPaint.ascent()) / 2)
        canvas.drawText(text, xPos, yPos, textPaint)
    }
}
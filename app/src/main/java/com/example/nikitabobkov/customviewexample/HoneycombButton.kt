package com.example.nikitabobkov.customviewexample

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class HoneycombButton : View {
    private lateinit var hexagonPath: Path
    private lateinit var hexagonBorderPath: Path
    private lateinit var borderPaint: Paint
    private lateinit var textPaint: TextPaint
    private var radius: Float = 0f
    private var textSize: Float = 0f
    private var widthHoneycomb: Int = 0
    private var heightHoneycomb: Int = 0
    private var borderWidth: Float = 50f
    private var text: String = ""
    private var ellipsizeText: String = ""
    private var clickableRegion = Region()
    private var listener: OnHoneycombClickListener? = null
    private var bounds: Rect = Rect()
    private var color: Int = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        hexagonPath = Path()
        hexagonBorderPath = Path()
        textPaint = TextPaint()

        borderPaint = Paint()
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.strokeWidth = borderWidth
        borderPaint.style = Paint.Style.STROKE

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.HoneycombButton, 0, 0)
        text = getTextFromAttr(typedArray)
        color = typedArray.getInteger(R.styleable.HoneycombButton_hcb_color, context.resources.getColor(R.color.colorHoneycomb))
        borderPaint.color = typedArray.getInteger(R.styleable.HoneycombButton_hcb_borderColor, context.resources.getColor(R.color.colorHoneycombBorder))
        textPaint.color = typedArray.getInteger(R.styleable.HoneycombButton_hcb_textColor, Color.BLACK)
        textSize = typedArray.getFloat(R.styleable.HoneycombButton_hcb_textSize, 0f)
        borderPaint.pathEffect = CornerPathEffect(typedArray.getFloat(R.styleable.HoneycombButton_hcb_cornerRadius, 10f))
        radius = typedArray.getFloat(R.styleable.HoneycombButton_hcb_radius, 0f)
        typedArray.recycle()
    }

    private fun getTextFromAttr(a: TypedArray): String {
        val id = a.getResourceId(R.styleable.HoneycombButton_hcb_text, -1)
        return if (id == -1) "vfvvfvhuj" else resources.getString(id)
    }

    fun setRadius(radius: Float) {
        this.radius = radius
        requestLayout()
    }

    fun setCornerRadius(radius: Float) {
        borderPaint.pathEffect = CornerPathEffect(radius)
        invalidate()
    }

    fun setColor(color: Int) {
        this.color = color
        invalidate()
    }

    fun setTextColor(color: Int) {
        textPaint.color = context.resources.getColor(color)
        invalidate()
    }

    fun setTextSize(textSize: Float) {
        textPaint.textSize = textSize
        invalidate()
    }

    fun setBorderColor(color: Int) {
        borderPaint.color = context.resources.getColor(color)
        invalidate()
    }

    fun setText(text: String) {
        this.text = text
        calculateTextSize()
        invalidate()
    }

    fun getHoneycombWidth(): Int {
        return widthHoneycomb
    }

    fun getHoneycombHeight(): Int {
        return heightHoneycomb
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        radius = if (radius > 0) radius else 200f
        calculateView()
        calculateClickableRegion()
        invalidate()
    }

    private fun calculateTextSize() {
        if (text.isNotEmpty()) {
            textPaint.textSize = if (textSize > 0) textSize else (widthHoneycomb / 5).toFloat()
            ellipsizeText = TextUtils.ellipsize(text, textPaint, widthHoneycomb.toFloat() - (borderWidth * 2), TextUtils.TruncateAt.END) as String
            textPaint.getTextBounds(ellipsizeText, 0, ellipsizeText.length - 1, bounds)
        }
    }

    private fun calculateView() {
        val halfRadius = radius / 2f
        val triangleHeight = (Math.sqrt(3.0) * halfRadius).toFloat()
        widthHoneycomb = (triangleHeight * 2 + borderWidth).toInt()
        heightHoneycomb = (radius * 2 + borderWidth).toInt()
        setMeasuredDimension(widthHoneycomb, heightHoneycomb)

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

        calculateTextSize()
    }

    private fun calculateClickableRegion() {
        val rectF = RectF()
        hexagonPath.computeBounds(rectF, true)
        val rect = Rect(rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(), rectF.bottom.toInt())
        clickableRegion.setPath(hexagonPath, Region(rect))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(hexagonBorderPath, borderPaint)
        canvas?.clipPath(hexagonPath)
        canvas?.drawColor(color)

        //TODO: 03/10/18 find x pos
        if (text.isNotEmpty()) {
            val xPos = (widthHoneycomb / 2 - bounds.width() / 2).toFloat() - borderWidth
//        var xPos = (widthHoneycomb - textPaint.textSize * Math.abs(text.length / 2)) / 2
//        if (xPos < borderWidth) {
//            xPos = borderWidth
//        }
            val yPos = (height / 2 - (textPaint.descent() + textPaint.ascent()) / 2)

            canvas?.drawText(ellipsizeText, xPos, yPos, textPaint)
        }
    }

    fun setOnClickListener(listener: OnHoneycombClickListener) {
        this.listener = listener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x!!.toInt()
        val y = event.y.toInt()
        if (clickableRegion.contains(x, y) && event.action == MotionEvent.ACTION_UP) {
            listener?.onHoneycombClick()
        }
        return true
    }
}
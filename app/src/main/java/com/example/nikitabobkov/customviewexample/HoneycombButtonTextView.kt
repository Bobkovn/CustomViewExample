package com.example.nikitabobkov.customviewexample

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.example.nikitabobkov.customviewexample.Utils.Companion.convertDpToPixel

const val THIRTY_DEGREE_RADIAN = 0.523599
const val HEXAGON_SIDE = 6

class HoneycombButtonTextView : TextView {
    private lateinit var hexagonPath: Path
    private lateinit var hexagonBorderPath: Path
    private lateinit var borderPaint: Paint
    private var radius: Float = 0f
    private var heightView: Int = 0
    private var borderWidth: Float = 50f
    private var clickableRegion = Region()
    private var listener: OnHoneycombClickListener? = null
    private var bounds: Rect = Rect()
    private var color: Int = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs, defStyleAttr, defStyleRes)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        hexagonPath = Path()
        hexagonBorderPath = Path()

        borderPaint = Paint()
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.strokeWidth = borderWidth
        borderPaint.style = Paint.Style.STROKE

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.HoneycombButton, defStyleAttr, defStyleRes)
        text = getTextFromAttr(typedArray)
        color = typedArray.getInteger(R.styleable.HoneycombButton_hcb_color, context.resources.getColor(R.color.colorHoneycomb))
        radius = typedArray.getFloat(R.styleable.HoneycombButton_hcb_radius, 0f)
        radius = Utils.convertDpToPixel(radius, context)
        borderPaint.apply {
            color = typedArray.getInteger(R.styleable.HoneycombButton_hcb_borderColor, context.resources.getColor(R.color.colorHoneycombBorder))
            pathEffect = CornerPathEffect(typedArray.getFloat(R.styleable.HoneycombButton_hcb_cornerRadius, 10f))
        }
        typedArray.recycle()
    }

    private fun getTextFromAttr(a: TypedArray): String {
        val id = a.getResourceId(R.styleable.HoneycombButton_hcb_text, -1)
        return if (id == -1) "" else resources.getString(id)
    }

    fun setRadius(radius: Float) {
        this.radius = Utils.convertDpToPixel(radius, context)
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

    fun setBorderColor(color: Int) {
        borderPaint.color = context.resources.getColor(color)
        invalidate()
    }

    fun setText(text: String) {
        this.text = text
        setupTextSize()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setupRadius(widthMeasureSpec, heightMeasureSpec)
        setupView()
        setupClickableRegion()
        gravity = Gravity.CENTER
        invalidate()
    }

    private fun setupRadius(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        if (radius == 0f) {
            radius = if (widthMode == View.MeasureSpec.EXACTLY || heightMode == View.MeasureSpec.EXACTLY) {
                Math.min(widthSize / 2f, heightSize / 2f)
            } else {
                Utils.convertDpToPixel(DEFAULT_RADIUS_DP, context)
            }
        }
    }

    private fun setupView() {
        heightView = (radius * 2).toInt()
        setMeasuredDimension(heightView, heightView)
        val centerX = measuredWidth / 2f
        val centerY = measuredHeight / 2f
        hexagonPath.reset()
        hexagonBorderPath.reset()
        val radian = 2.0 * Math.PI / HEXAGON_SIDE
        for (i in 0 until HEXAGON_SIDE) {
            val x1 = (centerX + radius * Math.cos(radian * i + THIRTY_DEGREE_RADIAN)).toFloat()
            val y1 = (centerY + radius * Math.sin(radian * i + THIRTY_DEGREE_RADIAN)).toFloat()
            if (i == 0) {
                hexagonPath.moveTo(x1, y1)
                hexagonBorderPath.moveTo(x1, y1)
            } else {
                hexagonPath.lineTo(x1, y1)
                hexagonBorderPath.lineTo(x1, y1)
            }
        }
        hexagonPath.close()
        hexagonBorderPath.close()

        setupTextSize()
    }

    private fun setupTextSize() {
        paint.getTextBounds(text.toString(), 0, text.length, bounds)
        val sp = textSize / resources.displayMetrics.scaledDensity
        if (bounds.width() > radius * 2 - convertDpToPixel(50f, context)) {
            textSize = sp - 1
            setupTextSize()
        }
    }

    private fun setupClickableRegion() {
        val rectF = RectF()
        hexagonPath.computeBounds(rectF, true)
        val rect = Rect(rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(), rectF.bottom.toInt())
        clickableRegion.setPath(hexagonPath, Region(rect))
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(hexagonBorderPath, borderPaint)
        canvas?.clipPath(hexagonPath)
        canvas?.drawColor(color)
        super.onDraw(canvas)
    }

    fun setOnClickListener(listener: OnHoneycombClickListener) {
        this.listener = listener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x!!.toInt()
        val y = event.y.toInt()
        if (clickableRegion.contains(x, y) && event.action == MotionEvent.ACTION_UP) {
            performClick()
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        listener?.onHoneycombClick(text.toString())
        return true
    }
}
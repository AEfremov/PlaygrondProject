package ru.efremov.playgrondproject.piechart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PieChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Data
    private var data: PieData? = null

    // Graphics
//    private val borderPaint = Paint()
    private val linePaint = Paint()
    private val indicatorCirclePaint = Paint()
    private var indicatorCircleRadius = 0f
    private val mainTextPaint = Paint()
    private val oval = RectF()

    init {
//        borderPaint.apply {
//            style = Paint.Style.STROKE
//            isAntiAlias = true
//            color = Color.WHITE
//        }
        indicatorCirclePaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = Color.LTGRAY
            alpha = 0
        }
        linePaint.apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = Color.LTGRAY
            alpha = 0
        }
        mainTextPaint.apply {
            isAntiAlias = true
            color = Color.BLACK
            alpha = 0
        }
    }

    /**
     * Populates the data object and sets up the view based off the new data
     *
     * @param data the new set of data to be represented by the pie chart
     */
    fun setData(data: PieData) {
        this.data = data
        setPieSliceDimensions()
        invalidate()
    }

    /**
     * Calculates and sets the dimensions of the pie slices in the pie chart
     */
    private fun setPieSliceDimensions() {
        var lastAngle = 0f
        data?.pieSlices?.forEach {
            // starting angle is the location of the last angle drawn
            it.value.startAngle = lastAngle
            // sweep angle is determined by multiplying the percentage of the project time with respect
            // to the total time recorded and scaling it to unit circle degrees by multiplying by 360
            it.value.sweepAngle = (((it.value.value / data?.totalValue!!)) * 360f).toFloat()
            lastAngle += it.value.sweepAngle

            setIndicatorLocation(it.key)
        }
    }

    /**
     * Use the angle between the start and sweep angles to help get position of the indicator circle
     * formula for x pos: (length of line) * cos(middleAngle) + (distance from left edge of screen)
     * formula for y pos: (length of line) * sin(middleAngle) + (distance from top edge of screen)
     *
     * @param key key of pie slice being altered
     */
    private fun setIndicatorLocation(key: String) {
        data?.pieSlices?.get(key)?.let {
            val middleAngle = it.sweepAngle / 2 + it.startAngle

            it.indicatorCircleLocation.x = (layoutParams.height.toFloat() / 2 - layoutParams.height / 8) *
                    Math.cos(Math.toRadians(middleAngle.toDouble())).toFloat() + width / 2
            it.indicatorCircleLocation.y = (layoutParams.height.toFloat() / 2 - layoutParams.height / 8) *
                    Math.sin(Math.toRadians(middleAngle.toDouble())).toFloat() + layoutParams.height / 2
        }
    }

    /**
     * Sets the bounds of the pie chart
     *
     * @param top the top bound of the circle. top of view by default
     * @param bottom the bottom bound of the circle. bottom of view by default
     * @param left the left bound of the circle. half of height by default
     * @param right the right bound of the circle. hald of height by default
     */
    private fun setCircleBounds(
        top: Float = 0f, bottom: Float = layoutParams.height.toFloat(),
        left: Float = (width / 2) - (layoutParams.height / 2).toFloat(),
        right: Float = (width / 2) + (layoutParams.height / 2).toFloat()
    ) {
        oval.top = top
        oval.bottom = bottom
        oval.left = left
        oval.right = right
    }

    /**
     * Sets the text sizes and thickness of graphics used in the view
     */
    private fun setGraphicSizes() {
        mainTextPaint.textSize = height / 15f
//        borderPaint.strokeWidth = height / 80f
        linePaint.strokeWidth = height / 120f
        indicatorCircleRadius = height / 70f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setCircleBounds()
        setGraphicSizes()
        data?.pieSlices?.forEach {
            setIndicatorLocation(it.key)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        data?.pieSlices?.let { slices ->
            slices.forEach {
                canvas?.drawArc(oval, it.value.startAngle, it.value.sweepAngle, true, it.value.paint)
//                canvas?.drawArc(oval, it.value.startAngle, it.value.sweepAngle, true, borderPaint)
                drawIndicators(canvas, it.value)
            }
        }
    }

    /**
     * Draws the indicators for projects displayed on the pie chart
     *
     * @param canvas the canvas used to draw onto the screen
     * @param pieItem the project information to display
     */
    private fun drawIndicators(canvas: Canvas?, pieItem: PieSlice) {
        // draw line & text for indicator circle if on left side of the pie chart
        if (pieItem.indicatorCircleLocation.x < width / 2) {
            drawIndicatorLine(canvas, pieItem, IndicatorAlignment.LEFT)
            drawIndicatorText(canvas, pieItem, IndicatorAlignment.LEFT)
            // draw line & text for indicator circle if on right side of the pie chart
        } else {
            drawIndicatorLine(canvas, pieItem, IndicatorAlignment.RIGHT)
            drawIndicatorText(canvas, pieItem, IndicatorAlignment.RIGHT)
        }
        // draw indicator circles for pie slice
        canvas?.drawCircle(pieItem.indicatorCircleLocation.x, pieItem.indicatorCircleLocation.y,
            indicatorCircleRadius, indicatorCirclePaint)
    }

    enum class IndicatorAlignment {
        LEFT, RIGHT
    }

    /**
     * Draws indicator lines onto the canvas dependent on which side the of the pie the slice is on
     *
     * @param canvas the canvas to draw onto
     * @param pieItem the pie data to draw
     * @param alignment which side of the pie chart this particular slice is on
     */
    private fun drawIndicatorLine(canvas: Canvas?, pieItem: PieSlice, alignment: IndicatorAlignment) {
        val xOffset = if (alignment == IndicatorAlignment.LEFT) width / 4 * -1 else width / 4
        canvas?.drawLine(
            pieItem.indicatorCircleLocation.x, pieItem.indicatorCircleLocation.y,
            pieItem.indicatorCircleLocation.x + xOffset, pieItem.indicatorCircleLocation.y, linePaint
        )
    }

    /**
     * Draws indicator names onto the canvas dependent on which side the of the pie the slice is on
     *
     * @param canvas the canvas to draw onto
     * @param pieItem the pie data to draw
     * @param alignment which side of the pie chart this particular slice is on
     */
    private fun drawIndicatorText(canvas: Canvas?, pieItem: PieSlice, alignment: IndicatorAlignment) {
        val xOffset = if (alignment == IndicatorAlignment.LEFT) width / 4 * -1 else width / 4
        if (alignment == IndicatorAlignment.LEFT) mainTextPaint.textAlign = Paint.Align.LEFT
        else mainTextPaint.textAlign = Paint.Align.RIGHT
        canvas?.drawText(pieItem.name, pieItem.indicatorCircleLocation.x + xOffset,
            pieItem.indicatorCircleLocation.y - 10, mainTextPaint)
    }
}
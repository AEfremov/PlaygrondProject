package ru.efremov.playgrondproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var valuesDegree: FloatArray
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val colors = intArrayOf(Color.BLUE, Color.GREEN, Color.GRAY)
    private var rectF = RectF(10f, 10f, 400f, 400f)
    private var temp = 0

    fun setData(values: FloatArray) {
        valuesDegree = FloatArray(values.size)
        for (i in values.indices) {
            valuesDegree[i] = values[i]
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        for (i in valuesDegree.indices) {
//            paint.color = colors[i]
//            canvas?.drawArc(rectF, temp.toFloat(), valuesDegree[i], true, paint)
//            temp += valuesDegree[i - 1].toInt()
//        }

        for (i in valuesDegree.indices) {
            if (i == 0) {
                paint.color = colors[i]
                canvas?.drawArc(rectF, 0f, valuesDegree[i], true, paint)
            } else {
                temp += valuesDegree[i - 1].toInt()
                paint.color = colors[i]
                canvas?.drawArc(rectF, temp.toFloat(), valuesDegree[i], true, paint)
            }
        }
    }
}
package com.example.mehen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class MehenView(context: Context?, attrs: AttributeSet?) : View(context, attrs){
    private val scaleFactor = 1.0f
    private var originX = 20f
    private var originY = 200f
    private var cellSide = 130f
    private val paint = Paint()
    private val greenColor = Color.parseColor("#00FF7F")
    private val redColor = Color.parseColor("#FF69B4")
    private val lightColor = Color.parseColor("#FFDEAD")
    private val blackColor = Color.parseColor("#000000")
    private val mehenList = listOf(
        listOf(0, 7, lightColor),
        listOf(0, 6, lightColor),
        listOf(0, 5, lightColor),
        listOf(0, 4, redColor),
        listOf(0, 3, lightColor),
        listOf(0, 2, greenColor),
        listOf(0, 1, lightColor),
        listOf(0, 0, lightColor),
        listOf(1, 0, lightColor),
        listOf(2, 0, lightColor),
        listOf(3, 0, lightColor),
        listOf(4, 0, greenColor),
        listOf(5, 0, lightColor),
        listOf(6, 0, lightColor),
        listOf(7, 0, lightColor),
        listOf(7, 1, lightColor),
        listOf(7, 2, lightColor),
        listOf(7, 3, greenColor),
        listOf(7, 4, lightColor),
        listOf(7, 5, lightColor),
        listOf(7, 6, lightColor),
        listOf(7, 7, redColor),
        listOf(6, 7, lightColor),
        listOf(5, 7, greenColor),
        listOf(4, 7, lightColor),
        listOf(3, 7, lightColor),
        listOf(2, 7, lightColor),
        listOf(1, 7, greenColor),
        listOf(1, 6, lightColor),
        listOf(1, 5, lightColor),
        listOf(1, 4, lightColor),
        listOf(1, 3, lightColor),
        listOf(1, 2, lightColor),
        listOf(1, 1, greenColor),
        listOf(2, 1, lightColor),
        listOf(3, 1, greenColor),
        listOf(4, 1, lightColor),
        listOf(5, 1, lightColor),
        listOf(6, 1, lightColor),
        listOf(6, 2, lightColor),
        listOf(6, 3, lightColor),
        listOf(6, 4, greenColor),
        listOf(6, 5, lightColor),
        listOf(6, 6, lightColor),
        listOf(5, 6, lightColor),
        listOf(4, 6, greenColor),
        listOf(3, 6, lightColor),
        listOf(2, 6, greenColor),
        listOf(2, 5, lightColor),
        listOf(2, 4, lightColor),
        listOf(2, 3, lightColor),
        listOf(2, 2, greenColor),
        listOf(3, 2, lightColor),
        listOf(4, 2, greenColor),
        listOf(5, 2, lightColor),
        listOf(5, 3, lightColor),
        listOf(5, 4, lightColor),
        listOf(5, 5, greenColor),
        listOf(4, 5, lightColor),
        listOf(3, 5, greenColor),
        listOf(3, 4, lightColor),
        listOf(3, 3, greenColor),
        listOf(4, 3, lightColor),
        listOf(4, 4, greenColor),
    )

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val mehenBoardSide = min(width, height)*scaleFactor
        cellSide = mehenBoardSide/8f
        originX = (width - mehenBoardSide)/2f
        originY = (height - mehenBoardSide)/2f

        drawMehenBoard(canvas)
    }

    private fun drawMehenBoard(canvas: Canvas){
        for (i in mehenList){
            //fill
            paint.color = i[2]
            paint.style = Paint.Style.FILL
            canvas.drawRect(
                originX+(i[1])*cellSide,
                originY+(i[0])*cellSide,
                originX+(1+i[1])*cellSide,
                originY+(1+i[0])*cellSide,
                paint)
            //stroke
            paint.color = blackColor
            paint.style = Paint.Style.STROKE
            canvas.drawRect(
                originX+(i[1])*cellSide,
                originY+(i[0])*cellSide,
                originX+(1+i[1])*cellSide,
                originY+(1+i[0])*cellSide,
                paint)
        }
    }
}
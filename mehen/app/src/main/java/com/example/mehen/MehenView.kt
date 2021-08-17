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
    private val greenColor = Color.parseColor("#00FF00")
    private val redColor = Color.parseColor("#FF1493")
    private val lightColor = Color.parseColor("#FFDEAD")
    private val blackColor = Color.parseColor("#000000")

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val mehenBoardSide = min(width, height)*scaleFactor
        cellSide = mehenBoardSide/8f
        originX = (width - mehenBoardSide)/2f
        originY = (height - mehenBoardSide)/2f

        drawMehenBoard(canvas)
    }

    private fun drawMehenBoard(canvas: Canvas){
        for (row in 0 until 8){
            for (col in 0 until 8){
                //fill
                paint.color = redColor
                paint.style = Paint.Style.FILL
                canvas.drawRect(
                    originX + col * cellSide,
                    originY + row * cellSide,
                    originX + (col + 1) * cellSide,
                    originY + (row + 1) * cellSide,
                    paint
                )
                //stroke
                paint.color = blackColor
                paint.style = Paint.Style.STROKE
                canvas.drawRect(
                    originX + col * cellSide,
                    originY + row * cellSide,
                    originX + (col + 1) * cellSide,
                    originY + (row + 1) * cellSide,
                    paint
                )
            }
        }
    }
}
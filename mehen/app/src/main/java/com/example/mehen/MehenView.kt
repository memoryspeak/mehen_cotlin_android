package com.example.mehen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import kotlin.math.min

class MehenView(context: Context?, attrs: AttributeSet?) : View(context, attrs){
    private val scaleFactor = 1.0f
    private var originX = 20f
    private var originY = 200f
    private var cellSide = 130f

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val mehenBoardSide = min(width, height)*scaleFactor
        cellSide = mehenBoardSide/8f
        originX = (width - mehenBoardSide)/2f
        originY = (height - mehenBoardSide)/2f

        val paint = Paint()
        paint.color = Color.LTGRAY

        canvas?.drawRect(originX, originY, originX + cellSide, originY + cellSide, paint)
    }
}
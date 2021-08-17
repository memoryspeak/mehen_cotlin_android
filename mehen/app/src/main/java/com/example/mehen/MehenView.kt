package com.example.mehen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MehenView(context: Context?, attrs: AttributeSet?) : View(context, attrs){

    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        paint.color = Color.LTGRAY

        canvas?.drawRect(100f, 200f, 100f+70, 200f+70, paint)
    }
}
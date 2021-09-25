package com.example.mehen

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min
import kotlin.random.Random
import android.graphics.DashPathEffect




class MehenView(context: Context?, attrs: AttributeSet?) : View(context, attrs){
    private val pieceSize = 0.9f
    private val pieceSizeToTouch = 1.0f
    private val diceRollSize = 0.9f
    private val dotSize = 0.3f
    private val scaleFactor = 1.0f
    private var originX = 20f
    private var originY = 20f
    private var cellSide = 130f
    private val textSize = 40f
    private val paint = Paint()
    private val paintLine = Paint()
    private val paintOutline = Paint()
    private val whiteColor = Color.parseColor("#FFFFFF")
    private val greenColor = Color.parseColor("#008000")
    private val redColor = Color.parseColor("#FF0000")
    private val lightColor = Color.parseColor("#FFDEAD")
    private val blackColor = Color.parseColor("#000000")
    private val blueColor = Color.parseColor("#1E90FF")
    private val yellowColor = Color.parseColor("#FFFF00")
    private val outBoardColor = whiteColor
    private val mehenList = listOf(
        listOf(0, 7, outBoardColor),
        listOf(0, 6, outBoardColor),
        listOf(0, 5, outBoardColor),
        listOf(0, 4, outBoardColor),
        listOf(0, 3, outBoardColor),
        listOf(0, 2, outBoardColor),
        listOf(0, 1, outBoardColor),
        listOf(0, 0, outBoardColor),
        listOf(1, 7, lightColor),
        listOf(1, 6, lightColor),
        listOf(1, 5, lightColor),
        listOf(1, 4, redColor),
        listOf(1, 3, lightColor),
        listOf(1, 2, greenColor),
        listOf(1, 1, lightColor),
        listOf(1, 0, lightColor),
        listOf(2, 0, lightColor),
        listOf(3, 0, lightColor),
        listOf(4, 0, lightColor),
        listOf(5, 0, greenColor),
        listOf(6, 0, lightColor),
        listOf(7, 0, lightColor),
        listOf(8, 0, lightColor),
        listOf(8, 1, lightColor),
        listOf(8, 2, lightColor),
        listOf(8, 3, greenColor),
        listOf(8, 4, lightColor),
        listOf(8, 5, lightColor),
        listOf(8, 6, lightColor),
        listOf(8, 7, redColor),
        listOf(7, 7, lightColor),
        listOf(6, 7, greenColor),
        listOf(5, 7, lightColor),
        listOf(4, 7, lightColor),
        listOf(3, 7, lightColor),
        listOf(2, 7, greenColor),
        listOf(2, 6, lightColor),
        listOf(2, 5, lightColor),
        listOf(2, 4, lightColor),
        listOf(2, 3, lightColor),
        listOf(2, 2, lightColor),
        listOf(2, 1, greenColor),
        listOf(3, 1, lightColor),
        listOf(4, 1, greenColor),
        listOf(5, 1, lightColor),
        listOf(6, 1, lightColor),
        listOf(7, 1, lightColor),
        listOf(7, 2, lightColor),
        listOf(7, 3, lightColor),
        listOf(7, 4, greenColor),
        listOf(7, 5, lightColor),
        listOf(7, 6, lightColor),
        listOf(6, 6, lightColor),
        listOf(5, 6, greenColor),
        listOf(4, 6, lightColor),
        listOf(3, 6, greenColor),
        listOf(3, 5, lightColor),
        listOf(3, 4, lightColor),
        listOf(3, 3, lightColor),
        listOf(3, 2, greenColor),
        listOf(4, 2, lightColor),
        listOf(5, 2, greenColor),
        listOf(6, 2, lightColor),
        listOf(6, 3, lightColor),
        listOf(6, 4, lightColor),
        listOf(6, 5, greenColor),
        listOf(5, 5, lightColor),
        listOf(4, 5, greenColor),
        listOf(4, 4, lightColor),
        listOf(4, 3, greenColor),
        listOf(5, 3, lightColor),
        listOf(5, 4, greenColor),
        listOf(9, 0, outBoardColor),
        listOf(9, 1, outBoardColor),
        listOf(9, 2, outBoardColor),
        listOf(9, 3, outBoardColor),
        listOf(9, 4, outBoardColor),
        listOf(9, 5, outBoardColor),
        listOf(9, 6, outBoardColor),
        listOf(9, 7, outBoardColor),
    )
    private val imgResIDs = setOf(
        R.drawable.white_lion,
        R.drawable.white_goose,
        R.drawable.black_lion,
        R.drawable.black_goose,
    )
    private val imgResIDsOfDiceRollWhite = listOf(
        R.drawable.dice_roll_1_white,
        R.drawable.dice_roll_2_white,
        R.drawable.dice_roll_3_white,
        R.drawable.dice_roll_4_white,
        R.drawable.dice_roll_5_white,
        R.drawable.dice_roll_6_white,
    )
    private val imgResIDsOfDiceRollBlack = listOf(
        R.drawable.dice_roll_1_black,
        R.drawable.dice_roll_2_black,
        R.drawable.dice_roll_3_black,
        R.drawable.dice_roll_4_black,
        R.drawable.dice_roll_5_black,
        R.drawable.dice_roll_6_black,
    )

    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private val bitmapsOfDiceRollWhite = mutableMapOf<Int, Bitmap>()
    private val bitmapsOfDiceRollBlack = mutableMapOf<Int, Bitmap>()
    private var movingPieceBitmap: Bitmap? = null
    private var movingPiece: MehenPiece? = null
    private var fromCol: Int = -1
    private var fromRow: Int = -1
    private var movingPieceX = -1f
    private var movingPieceY = -1f

    var mehenDelegate: MehenDelegate? = null

    init { loadBitmaps() }
    init { loadBitmapsOfDiceRollWhite() }
    init { loadBitmapsOfDiceRollBlack() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        val bigger = 10*smaller/8
        setMeasuredDimension(smaller, bigger)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val mehenBoardSide = min(width, height)*scaleFactor
        cellSide = mehenBoardSide/8f
        originX = (width - mehenBoardSide)/2f
        originY = (height - mehenBoardSide-2*cellSide)/2f

        drawMehenBoard(canvas)
        drawPieces(canvas)

        drawWhiteDiceRoll(canvas)
        drawBlackDiceRoll(canvas)

        drawCanWhiteMove(canvas)
        drawCanBlackMove(canvas)

        drawPossibleDots(canvas)
        drawPieceOutline(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                fromCol = ((event.x - originX) / cellSide).toInt()
                fromRow = 9 - ((event.y - originY) / cellSide).toInt()
                MehenSingleton.outlineList.clear()

                mehenDelegate?.pieceAt(Square(fromCol, fromRow))?.let {
                    if (it.player == Player.WHITE && MehenSingleton.canWhiteMove || it.player == Player.BLACK && MehenSingleton.canBlackMove){
                        movingPiece = it
                        movingPieceBitmap = bitmaps[it.resID]
                        MehenSingleton.outlineList.add(fromCol)
                        MehenSingleton.outlineList.add(9 - fromRow)
                    } else return false
                }

                if (fromCol == 7 && fromRow == 9) {
//                    MehenSingleton.outlineList.clear()
                    if (MehenSingleton.canBlackDiceRoll) {
                        MehenSingleton.blackValueDiceRoll = randomDiceValue()
                        if (MehenSingleton.blackValueDiceRoll == 0) {
                            MehenSingleton.memoryBlack +=1
                        } else {
                            MehenSingleton.canBlackMove = true
                            MehenSingleton.canWhiteMove = false
                            MehenSingleton.canBlackDiceRoll = false
                        }
                    }
                }
                if (fromCol == 7 && fromRow == 0) {
//                    MehenSingleton.outlineList.clear()
                    if (MehenSingleton.canWhiteDiceRoll) {
                        MehenSingleton.whiteValueDiceRoll = randomDiceValue()
                        if (MehenSingleton.whiteValueDiceRoll == 0) {
                            MehenSingleton.memoryWhite += 1
                        } else {
                            MehenSingleton.canWhiteMove = true
                            MehenSingleton.canBlackMove = false
                            MehenSingleton.canWhiteDiceRoll = false
                        }
                    }
                }
                if (!MehenSingleton.viewPossibleDot){
                    val key = listOf<Int>(9 - fromRow, fromCol)
                    MehenSingleton.possibleDots.clear()
                    movingPiece?.let { MehenSingleton.bindingSquare[key]?.let { it1 ->
                        findPossibleDots(
                            it1, it.player, it.mehenman)
                    } }
                    MehenSingleton.viewPossibleDot = true
                } else {
                    MehenSingleton.possibleDots.clear()
                    MehenSingleton.viewPossibleDot = false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                movingPieceX = event.x
                movingPieceY = event.y
                if (!MehenSingleton.viewPossibleDot){
                    val key = listOf<Int>(9 - fromRow, fromCol)
                    MehenSingleton.possibleDots.clear()
                    movingPiece?.let { MehenSingleton.bindingSquare[key]?.let { it1 ->
                        findPossibleDots(
                            it1, it.player, it.mehenman)
                    } }
                    MehenSingleton.viewPossibleDot = true
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 9 - ((event.y - originY) / cellSide).toInt()
                if (fromCol != col || fromRow != row) {
                    mehenDelegate?.movePiece(Square(fromCol, fromRow), Square(col, row))

                }
                movingPiece = null
                movingPieceBitmap = null
                invalidate()
            }
        }
        return true
    }

    private fun drawPieces(canvas: Canvas) {
        for (row in 0 until 10)
            for (col in 0 until 8)
                mehenDelegate?.pieceAt(Square(col, row))?.let { piece ->
                    if (piece != movingPiece) {
                        drawPieceAt(canvas, col, row, piece.resID)
                    }
                }
        movingPieceBitmap?.let {
            canvas.drawBitmap(
                it,
                null,
                RectF(
                    movingPieceX - cellSide*pieceSizeToTouch/2,
                    movingPieceY - cellSide*pieceSizeToTouch/2,
                    movingPieceX + cellSide*pieceSizeToTouch/2,
                    movingPieceY + cellSide*pieceSizeToTouch/2),
                paint)
        }
    }

    private fun drawPieceAt(canvas: Canvas, col: Int, row: Int, resID: Int) =
        canvas.drawBitmap(
            bitmaps[resID]!!,
            null, RectF(
                originX + col * cellSide + cellSide/(10*pieceSize),
                originY + (9 - row) * cellSide + cellSide/(10*pieceSize),
                originX + (col + 1) * cellSide - cellSide/(10*pieceSize),
                originY + ((9 - row) + 1) * cellSide - cellSide/(10*pieceSize)),
            paint)

    private fun loadBitmaps() =
        imgResIDs.forEach { imgResID ->
            bitmaps[imgResID] = BitmapFactory.decodeResource(resources, imgResID)
        }

    private fun loadBitmapsOfDiceRollWhite() {
        for (i in 0 until 6) {
            bitmapsOfDiceRollWhite[i] = BitmapFactory.decodeResource(resources, imgResIDsOfDiceRollWhite[i])
        }
    }

    private fun loadBitmapsOfDiceRollBlack() {
        for (i in 0 until 6) {
            bitmapsOfDiceRollBlack[i] = BitmapFactory.decodeResource(resources, imgResIDsOfDiceRollBlack[i])
        }
    }

    private fun drawWhiteMemory(canvas: Canvas){
        paint.color = blackColor
        paint.textSize = textSize
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(
            "+${MehenSingleton.memoryWhite}",
            originX + 13*cellSide/2,
            originY + 19*cellSide/2 + textSize/2, paint)
    }

    private fun drawBlackMemory(canvas: Canvas){
        paint.color = blackColor
        paint.textSize = textSize
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(
            "+${MehenSingleton.memoryBlack}",
            originX + 13*cellSide/2,
            originY + 1*cellSide/2 + textSize/2, paint)
    }

    private fun drawCanWhiteMove(canvas: Canvas){
        if (!MehenSingleton.canWhiteMove && MehenSingleton.canWhiteDiceRoll || MehenSingleton.canWhiteMove && !MehenSingleton.canWhiteDiceRoll) {
            paint.color = greenColor
            paint.style = Paint.Style.FILL
            canvas.drawOval(RectF(
                originX + 6*cellSide + 2*cellSide/3,
                originY + 9*cellSide + 2*cellSide/3,
                originX + 6*cellSide+5*cellSide/6,
                originY + 9*cellSide+5*cellSide/6),
                paint)
        }
    }

    private fun drawCanBlackMove(canvas: Canvas){
        if (!MehenSingleton.canBlackMove && MehenSingleton.canBlackDiceRoll || MehenSingleton.canBlackMove && !MehenSingleton.canBlackDiceRoll) {
            paint.color = greenColor
            paint.style = Paint.Style.FILL
            canvas.drawOval(RectF(
                originX + 6*cellSide + 2*cellSide/3,
                originY + 0*cellSide + 2*cellSide/3,
                originX + 6*cellSide+5*cellSide/6,
                originY + 0*cellSide+5*cellSide/6),
                paint)
        }
    }

    private fun drawWhiteDiceRoll(canvas: Canvas){
        val diceRollWhite = (bitmapsOfDiceRollWhite[MehenSingleton.whiteValueDiceRoll])
        if (diceRollWhite != null) {
            canvas.drawBitmap(
                diceRollWhite,
                null,
                RectF(
                    originX + 7*cellSide + cellSide/(10*diceRollSize),
                    originY + 9*cellSide + cellSide/(10*diceRollSize),
                    originX + 8*cellSide - cellSide/(10*diceRollSize),
                    originY + 10*cellSide - cellSide/(10*diceRollSize)),
                paint)
        }
        drawWhiteMemory(canvas)
    }

    private fun drawBlackDiceRoll(canvas: Canvas){
        val diceRollBlack = (bitmapsOfDiceRollBlack[MehenSingleton.blackValueDiceRoll])
        if (diceRollBlack != null) {
            canvas.drawBitmap(
                diceRollBlack,
                null,
                RectF(
                    originX + 7*cellSide + cellSide/(10*diceRollSize),
                    originY + cellSide/(10*diceRollSize),
                    originX + 8*cellSide - cellSide/(10*diceRollSize),
                    originY+cellSide - cellSide/(10*diceRollSize)),
                paint)
        }
        drawBlackMemory(canvas)
    }

    private fun drawPossibleDots(canvas: Canvas){
        if (MehenSingleton.viewPossibleDot){
            for (dot in MehenSingleton.possibleDots){
                if (dot != null){
                    paint.color = Color.parseColor(dot.dotColor)
                    paint.style = Paint.Style.FILL
                    canvas.drawOval(RectF(
                        originX+(dot.col)*cellSide + cellSide*(1 - dotSize)/2,
                        originY+(dot.row)*cellSide + cellSide*(1 - dotSize)/2,
                        originX+(1+dot.col)*cellSide - cellSide*(1 - dotSize)/2,
                        originY+(1+dot.row)*cellSide - cellSide*(1 - dotSize)/2),
                        paint)
                }
            }
        }
    }

    private fun drawPieceOutline(canvas: Canvas){
        if (MehenSingleton.outlineList.size != 0){
            val col = MehenSingleton.outlineList[0]
            val row = MehenSingleton.outlineList[1]
            paintOutline.color = Color.parseColor("#80B8860B")
            paintOutline.style = Paint.Style.FILL
            canvas.drawOval(
                originX+col*cellSide+cellSide/(10*pieceSize),
                originY+row*cellSide+cellSide/(10*pieceSize),
                originX+(col+1)*cellSide-cellSide/(10*pieceSize),
                originY+(row+1)*cellSide-cellSide/(10*pieceSize),
                paintOutline)
        }
    }

    private fun findPossibleDots(position: Int, player: Player, mehenman: Mehenman){
        val blueDotColor: String = "#E6B8860B"
        val yellowDotColor: String = "#E6BC8F8F"
        if (mehenman == Mehenman.WALKER){
            if (player == Player.WHITE){
                for (dot in position + MehenSingleton.whiteValueDiceRoll + 2 until position + MehenSingleton.whiteValueDiceRoll + MehenSingleton.memoryWhite + 2){
                    val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                    for (i in keys){
                        MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], yellowDotColor))
                    }
                }
                for (dot in position + 1 until position + MehenSingleton.memoryWhite + 1){
                    val whiteDot = position + MehenSingleton.whiteValueDiceRoll + 1
                    if (dot != whiteDot){
                        val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                        for (i in keys){ MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], yellowDotColor)) }
                    }
                }
                val diceRollKeys = MehenSingleton.bindingSquare.filterValues { it == position + MehenSingleton.whiteValueDiceRoll + 1 }.keys
                for (i in diceRollKeys){
                    MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], blueDotColor))
                }
                if (MehenSingleton.whiteValueDiceRoll == 5){ when (position){
                    0 -> MehenSingleton.possibleDots.add(PossibleDot(7, 2, blueDotColor))
                    6 -> MehenSingleton.possibleDots.add(PossibleDot(1, 2, blueDotColor))
                    12 -> MehenSingleton.possibleDots.add(PossibleDot(1, 4, blueDotColor))
                    18 -> MehenSingleton.possibleDots.add(PossibleDot(7, 2, blueDotColor))
                    24 -> MehenSingleton.possibleDots.add(PossibleDot(6, 5, blueDotColor))
                    28 -> MehenSingleton.possibleDots.add(PossibleDot(6, 3, blueDotColor))
                    34 -> MehenSingleton.possibleDots.add(PossibleDot(2, 3, blueDotColor))
                    36 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(2, 3, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 5, blueDotColor))
                    }
                    42 -> MehenSingleton.possibleDots.add(PossibleDot(5, 6, blueDotColor))
                    46 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(5, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 6, blueDotColor))
                    }
                    48 -> MehenSingleton.possibleDots.add(PossibleDot(5, 4, blueDotColor))
                    52 -> MehenSingleton.possibleDots.add(PossibleDot(3, 4, blueDotColor))
                    54 -> MehenSingleton.possibleDots.add(PossibleDot(3, 4, blueDotColor))
                    58 -> MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                    60 -> MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                    62 -> MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                } }
            } else {
                for (dot in position + MehenSingleton.blackValueDiceRoll + 2 until position + MehenSingleton.blackValueDiceRoll + MehenSingleton.memoryBlack + 2){
                    val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                    for (i in keys){
                        MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], yellowDotColor))
                    }
                }
                for (dot in position + 1 until position + MehenSingleton.memoryBlack + 1){
                    val blackDot = position + MehenSingleton.blackValueDiceRoll + 1
                    if (dot != blackDot){
                        val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                        for (i in keys){ MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], yellowDotColor)) }
                    }
                }
                val diceRollKeys = MehenSingleton.bindingSquare.filterValues { it == position + MehenSingleton.blackValueDiceRoll + 1 }.keys
                for (i in diceRollKeys){
                    MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], blueDotColor))
                }
                if (MehenSingleton.blackValueDiceRoll == 5){ when (position){
                    0 -> MehenSingleton.possibleDots.add(PossibleDot(7, 2, blueDotColor))
                    6 -> MehenSingleton.possibleDots.add(PossibleDot(1, 2, blueDotColor))
                    12 -> MehenSingleton.possibleDots.add(PossibleDot(1, 4, blueDotColor))
                    18 -> MehenSingleton.possibleDots.add(PossibleDot(7, 2, blueDotColor))
                    24 -> MehenSingleton.possibleDots.add(PossibleDot(6, 5, blueDotColor))
                    28 -> MehenSingleton.possibleDots.add(PossibleDot(6, 3, blueDotColor))
                    34 -> MehenSingleton.possibleDots.add(PossibleDot(2, 3, blueDotColor))
                    36 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(2, 3, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 5, blueDotColor))
                    }
                    42 -> MehenSingleton.possibleDots.add(PossibleDot(5, 6, blueDotColor))
                    46 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(5, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 6, blueDotColor))
                    }
                    48 -> MehenSingleton.possibleDots.add(PossibleDot(5, 4, blueDotColor))
                    52 -> MehenSingleton.possibleDots.add(PossibleDot(3, 4, blueDotColor))
                    54 -> MehenSingleton.possibleDots.add(PossibleDot(3, 4, blueDotColor))
                    58 -> MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                    60 -> MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                    62 -> MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                } }
            }
        } else {
            if (player == Player.WHITE){
                for (dot in position - 2*(MehenSingleton.whiteValueDiceRoll + 1) - MehenSingleton.memoryWhite until position - 2*(MehenSingleton.whiteValueDiceRoll + 1)){
                    val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                    for (i in keys){
                        MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], yellowDotColor))
                    }
                }
                for (dot in position - MehenSingleton.memoryWhite until position){
                    val whiteDot = position - 2*(MehenSingleton.whiteValueDiceRoll + 1)
                    if (dot != whiteDot){
                        val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                        for (i in keys){ MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], yellowDotColor)) }
                    }
                }
                val diceRollKeys = MehenSingleton.bindingSquare.filterValues { it == position - 2*(MehenSingleton.whiteValueDiceRoll + 1) }.keys
                for (i in diceRollKeys){
                    MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], blueDotColor))
                }
                if (MehenSingleton.whiteValueDiceRoll == 5){ when (position){
                    64 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(3, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 6, blueDotColor))
                    }
                    62 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(2, 3, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                    }
                    60 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(6, 3, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(6, 5, blueDotColor))
                    }
                    58 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(6, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(4, 7, blueDotColor))
                    }
                    54 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(1, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(3, 4, blueDotColor))
                    }
                    52 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(3, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(1, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(1, 2, blueDotColor))
                    }
                    48 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(5, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(7, 2, blueDotColor))
                    }
                    46 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(5, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 6, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(7, 6, blueDotColor))
                    }
                    42 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(5, 6, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(3, 8, blueDotColor))
                    }
                    36 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(0, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 3, blueDotColor))
                    }
                    34 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(2, 1, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 3, blueDotColor))
                    }
                    28 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(6, 3, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(0, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(1, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(3, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(4, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(0, 9, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(1, 9, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 9, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(3, 9, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(4, 9, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 9, blueDotColor))
                    }
                    24 -> { MehenSingleton.possibleDots.add(PossibleDot(6, 5, blueDotColor)) }
                    18 -> { MehenSingleton.possibleDots.add(PossibleDot(4, 7, blueDotColor)) }
                    12 -> { MehenSingleton.possibleDots.add(PossibleDot(1, 4, blueDotColor)) }
                    6-> { MehenSingleton.possibleDots.add(PossibleDot(1, 2, blueDotColor)) }
                } }
            } else {
                for (dot in position - 2*(MehenSingleton.blackValueDiceRoll + 1) - MehenSingleton.memoryBlack until position - 2*(MehenSingleton.blackValueDiceRoll + 1)){
                    val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                    for (i in keys){
                        MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], yellowDotColor))
                    }
                }
                for (dot in position - MehenSingleton.memoryBlack until position){
                    val blackDot = position - 2*(MehenSingleton.blackValueDiceRoll + 1)
                    if (dot != blackDot){
                        val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                        for (i in keys){ MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], yellowDotColor)) }
                    }
                }
                val diceRollKeys = MehenSingleton.bindingSquare.filterValues { it == position - 2*(MehenSingleton.blackValueDiceRoll + 1) }.keys
                for (i in diceRollKeys){
                    MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], blueDotColor))
                }
                if (MehenSingleton.blackValueDiceRoll == 5){ when (position){
                    64 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(3, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 6, blueDotColor))
                    }
                    62 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(2, 3, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                    }
                    60 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(6, 3, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(6, 5, blueDotColor))
                    }
                    58 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(4, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(6, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(4, 7, blueDotColor))
                    }
                    54 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(1, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(3, 4, blueDotColor))
                    }
                    52 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(3, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(1, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(1, 2, blueDotColor))
                    }
                    48 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(5, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(7, 2, blueDotColor))
                    }
                    46 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(5, 4, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 6, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(7, 6, blueDotColor))
                    }
                    42 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(5, 6, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(3, 8, blueDotColor))
                    }
                    36 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(0, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 5, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 3, blueDotColor))
                    }
                    34 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(2, 1, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 3, blueDotColor))
                    }
                    28 -> {
                        MehenSingleton.possibleDots.add(PossibleDot(6, 3, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(0, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(1, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(3, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(4, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 0, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(0, 9, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(1, 9, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(2, 9, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(3, 9, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(4, 9, blueDotColor))
                        MehenSingleton.possibleDots.add(PossibleDot(5, 9, blueDotColor))
                    }
                    24 -> { MehenSingleton.possibleDots.add(PossibleDot(6, 5, blueDotColor)) }
                    18 -> { MehenSingleton.possibleDots.add(PossibleDot(4, 7, blueDotColor)) }
                    12 -> { MehenSingleton.possibleDots.add(PossibleDot(1, 4, blueDotColor)) }
                    6-> { MehenSingleton.possibleDots.add(PossibleDot(1, 2, blueDotColor)) }
                } }
            }
        }
    }

    private fun randomDiceValue(): Int {
        return Random.nextInt(6)
    }

    private fun drawMehenBoard(canvas: Canvas){
        //draw squares
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
            if (i[2] != outBoardColor && i[2] != yellowColor && i[2] != blueColor){
                paint.color = blackColor
            } else { paint.color = i[2] }
            paint.style = Paint.Style.STROKE
            canvas.drawRect(
                originX+(i[1])*cellSide,
                originY+(i[0])*cellSide,
                originX+(1+i[1])*cellSide,
                originY+(1+i[0])*cellSide,
                paint)
        }
        //draw lines
        paintLine.color = blackColor
        paintLine.strokeWidth = 10f
        paintLine.style = Paint.Style.STROKE
        canvas.drawLine(originX+8*cellSide, originY+1*cellSide, originX+1*cellSide/2, originY+1*cellSide, paintLine)
        canvas.drawArc(originX+0*cellSide, originY+1*cellSide, originX+1*cellSide, originY+2*cellSide, 180f, 90f, false, paintLine)
        canvas.drawLine(originX+0*cellSide, originY+3*cellSide/2, originX+0*cellSide, originY+17*cellSide/2, paintLine)
        canvas.drawArc(originX+0*cellSide, originY+8*cellSide, originX+cellSide, originY+9*cellSide, 90f, 90f, false, paintLine)
        canvas.drawLine(originX+cellSide/2, originY+9*cellSide, originX+15*cellSide/2, originY+9*cellSide, paintLine)
        canvas.drawArc(originX+7*cellSide, originY+8*cellSide, originX+8*cellSide, originY+9*cellSide, 0f, 90f, false, paintLine)
        canvas.drawLine(originX+cellSide*8, originY+17*cellSide/2, originX+8*cellSide, originY+5*cellSide/2, paintLine)
        canvas.drawArc(originX+7*cellSide, originY+2*cellSide, originX+8*cellSide, originY+3*cellSide, -90f, 90f, false, paintLine)
        canvas.drawLine(originX+cellSide*8, originY+2*cellSide, originX+3*cellSide/2, originY+2*cellSide, paintLine)
        canvas.drawArc(originX+1*cellSide, originY+2*cellSide, originX+2*cellSide, originY+3*cellSide, 180f, 90f, false, paintLine)
        canvas.drawLine(originX+cellSide, originY+5*cellSide/2, originX+cellSide, originY+15*cellSide/2, paintLine)
        canvas.drawArc(originX+1*cellSide, originY+7*cellSide, originX+2*cellSide, originY+8*cellSide, 90f, 90f, false, paintLine)
        canvas.drawLine(originX+3*cellSide/2, originY+8*cellSide, originX+13*cellSide/2, originY+8*cellSide, paintLine)
        canvas.drawArc(originX+6*cellSide, originY+7*cellSide, originX+7*cellSide, originY+8*cellSide, 0f, 90f, false, paintLine)
        canvas.drawLine(originX+7*cellSide, originY+15*cellSide/2, originX+7*cellSide, originY+7*cellSide/2, paintLine)
        canvas.drawArc(originX+6*cellSide, originY+3*cellSide, originX+7*cellSide, originY+4*cellSide, -90f, 90f, false, paintLine)
        canvas.drawLine(originX+13*cellSide/2, originY+3*cellSide, originX+5*cellSide/2, originY+3*cellSide, paintLine)
        canvas.drawArc(originX+2*cellSide, originY+3*cellSide, originX+3*cellSide, originY+4*cellSide, 180f, 90f, false, paintLine)
        canvas.drawLine(originX+2*cellSide, originY+7*cellSide/2, originX+2*cellSide, originY+13*cellSide/2, paintLine)
        canvas.drawArc(originX+2*cellSide, originY+6*cellSide, originX+3*cellSide, originY+7*cellSide, 90f, 90f, false, paintLine)
        canvas.drawLine(originX+5*cellSide/2, originY+7*cellSide, originX+11*cellSide/2, originY+7*cellSide, paintLine)
        canvas.drawArc(originX+5*cellSide, originY+6*cellSide, originX+6*cellSide, originY+7*cellSide, 0f, 90f, false, paintLine)
        canvas.drawLine(originX+6*cellSide, originY+13*cellSide/2, originX+6*cellSide, originY+9*cellSide/2, paintLine)
        canvas.drawArc(originX+5*cellSide, originY+4*cellSide, originX+6*cellSide, originY+5*cellSide, -90f, 90f, false, paintLine)
        canvas.drawLine(originX+11*cellSide/2, originY+4*cellSide, originX+7*cellSide/2, originY+4*cellSide, paintLine)
        canvas.drawArc(originX+3*cellSide, originY+4*cellSide, originX+4*cellSide, originY+5*cellSide, 180f, 90f, false, paintLine)
        canvas.drawLine(originX+3*cellSide, originY+9*cellSide/2, originX+3*cellSide, originY+11*cellSide/2, paintLine)
        canvas.drawArc(originX+3*cellSide, originY+5*cellSide, originX+4*cellSide, originY+6*cellSide, 90f, 90f, false, paintLine)
        canvas.drawLine(originX+7*cellSide/2, originY+6*cellSide, originX+9*cellSide/2, originY+6*cellSide, paintLine)
        canvas.drawArc(originX+4*cellSide, originY+5*cellSide, originX+5*cellSide, originY+6*cellSide, -90f, 180f, false, paintLine)
        canvas.drawLine(originX+4*cellSide, originY+5*cellSide, originX+9*cellSide/2, originY+5*cellSide, paintLine)
    }
}
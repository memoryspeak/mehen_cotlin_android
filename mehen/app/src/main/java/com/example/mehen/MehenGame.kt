package com.example.mehen


import android.media.MediaPlayer
import android.view.View
import java.security.AccessController.getContext
import kotlin.random.Random

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager


object MehenGame{
    private var piecesBox = mutableSetOf<MehenPiece>()
    private const val basicDotColor: String = "#FF6B8E23"
    private const val additionalDotColor: String = "#E6BC8F8F"
    private val listOfGreenDotColor: List<Int> = listOf<Int>(0, 6, 12, 18, 24, 28, 34, 36, 42, 46, 48, 52, 54, 58, 60, 62, 64)

    init { reset() }

    fun clear(){ piecesBox.clear() }

    fun addPiece(piece: MehenPiece) { piecesBox.add(piece) }

    private fun canMove(from: Square, to: Square): Boolean {
        if (from.col == to.col && from.row == to.row) { return  false }
        else if (0 > to.row) { return  false }
        else if (9 < to.row) { return  false }
        else if (7 == to.col && 0 == to.row) { return  false }
        else if (7 == to.col && 9 == to.row) { return  false }
        else if (6 == to.col && 0 == to.row) { return  false }
        else if (6 == to.col && 9 == to.row) { return  false }
        pieceAt(from) ?: return false
        return true
    }

    fun movePiece(from: Square, to: Square) {
        if (canMove(from, to)) {
            movePiece(from.col, from.row, to.col, to.row)
        }
    }

    private fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromCol == toCol && fromRow == toRow) return
        val setColRow = mutableListOf<List<Int>>()
        for (item in MehenSingleton.possibleDots){ setColRow.add(listOf<Int>(item.col, item.row)) }
        if (listOf<Int>(toCol, 9 - toRow) !in setColRow) return
        val movingPiece = pieceAt(fromCol, fromRow) ?: return
        val itPiece = pieceAt(toCol, toRow)
        val dotFromSquare = MehenSingleton.bindingSquare[listOf(9 - fromRow, fromCol)]?.let { it.toInt() }!!
        val dotToSquare = MehenSingleton.bindingSquare[listOf(9 - toRow, toCol)]?.let { it.toInt() }!!
        var identy: Int = 0
        for (i in MehenSingleton.possibleDots){ if (i.col == toCol && i.row == 9-toRow && i.dotColor == "#FF6B8E23"){ identy += 1 } }

        if (itPiece != null){
            if (movingPiece.mehenman == Mehenman.LION){
                if (movingPiece.player == itPiece.player){
                    //если лев встречает своего игрока - меняются местами, иначе - съедает
                    piecesBox.remove(movingPiece)
                    addPiece(movingPiece.copy(col = toCol, row = toRow))
                    piecesBox.remove(itPiece)
                    addPiece(itPiece.copy(col = fromCol, row = fromRow))
                } else {
                    piecesBox.remove(itPiece)
                    piecesBox.remove(movingPiece)
                    addPiece(movingPiece.copy(col = toCol, row = toRow))
                }
            } else {
                //если ходящий игрок является пешеходом
                if (toRow == 4 && toCol == 4) {
                    //если ходим на центральную клетку
                    if (movingPiece.player == Player.WHITE){
                        //если ходящий игрок - белый
                        if (MehenSingleton.memoryWhite >= 2){
                            //если хватает доп.очков
                            piecesBox.remove(movingPiece)
                            addPiece(movingPiece.copy(col = toCol, row = toRow, mehenman = Mehenman.LION, resID = R.drawable.white_lion))
                            MehenSingleton.memoryWhite -= 2
                        } else {
                            //если не хватает доп.очков
                            piecesBox.remove(movingPiece)
                            addPiece(movingPiece.copy(col = toCol, row = toRow))
                        }
                    } else {
                        //если ходящий игрок - черный
                        if (MehenSingleton.memoryBlack >= 2){
                            //если хватает доп.очков
                            piecesBox.remove(movingPiece)
                            addPiece(movingPiece.copy(col = toCol, row = toRow, mehenman = Mehenman.LION, resID = R.drawable.black_lion))
                            MehenSingleton.memoryBlack -= 2
                        } else {
                            //если не хватает доп.очков
                            piecesBox.remove(movingPiece)
                            addPiece(movingPiece.copy(col = toCol, row = toRow))
                        }
                    }
                    //в любом случае тот, кто был на клетке, переносится назад
                    piecesBox.remove(itPiece)
                    addPiece(itPiece.copy(col = fromCol, row = fromRow))
                } else {
                    //если ходим не на центральную клетку
                    if (movingPiece.player != itPiece.player){
                        piecesBox.remove(movingPiece)
                        addPiece(movingPiece.copy(col = toCol, row = toRow))
                        piecesBox.remove(itPiece)
                        addPiece(itPiece.copy(col = fromCol, row = fromRow))
                    } else {
                        if (movingPiece.mehenman != itPiece.mehenman){
                            piecesBox.remove(movingPiece)
                            addPiece(movingPiece.copy(col = toCol, row = toRow))
                            piecesBox.remove(itPiece)
                            addPiece(itPiece.copy(col = fromCol, row = fromRow))
                        } else return
                    }
                }
            }
        } else {
            if (toRow == 4 && toCol == 4){
                if (movingPiece.mehenman == Mehenman.LION){
                    piecesBox.remove(movingPiece)
                    addPiece(movingPiece.copy(col = toCol, row = toRow))
                } else {
                    if (movingPiece.player == Player.WHITE){
                        if (MehenSingleton.memoryWhite >= 2){
                            piecesBox.remove(movingPiece)
                            addPiece(movingPiece.copy(col = toCol, row = toRow, mehenman = Mehenman.LION, resID = R.drawable.white_lion))
                            MehenSingleton.memoryWhite -= 2
                        } else {
                            piecesBox.remove(movingPiece)
                            addPiece(movingPiece.copy(col = toCol, row = toRow))
                        }
                    } else {
                        if (MehenSingleton.memoryBlack >= 2){
                            piecesBox.remove(movingPiece)
                            addPiece(movingPiece.copy(col = toCol, row = toRow, mehenman = Mehenman.LION, resID = R.drawable.black_lion))
                            MehenSingleton.memoryBlack -= 2
                        } else {
                            piecesBox.remove(movingPiece)
                            addPiece(movingPiece.copy(col = toCol, row = toRow))
                        }
                    }
                }
            } else {
                piecesBox.remove(movingPiece)
                addPiece(movingPiece.copy(col = toCol, row = toRow))
            }
        }

        if (MehenSingleton.soundEffect){ MehenSingleton.soundEngine.play(MehenSingleton.turnEffect, 1f, 1f, 1, 0, 1f) }

        if (dotToSquare == 4){
            val fourPiece: MehenPiece? = pieceAt(toCol, toRow)
            val twentyTwoPiece: MehenPiece? = pieceAt(7, 1)
            if (fourPiece != null) {
                piecesBox.remove(fourPiece)
                piecesBox.remove(twentyTwoPiece)
                addPiece(fourPiece.copy(col = 7, row = 1))
                if (twentyTwoPiece != null) {
                    addPiece(twentyTwoPiece.copy(col = 4, row = 8))
                }

            }
        }
        if (dotToSquare == 22){
            val twentyTwoPiece: MehenPiece? = pieceAt(toCol, toRow)
            val fourPiece: MehenPiece? = pieceAt(4, 8)
            if (twentyTwoPiece != null){
                piecesBox.remove(twentyTwoPiece)
                piecesBox.remove(fourPiece)
                addPiece(twentyTwoPiece.copy(col = 4, row = 8))
                if (fourPiece != null){
                    addPiece(fourPiece.copy(col = 7, row = 1))
                }
            }
        }
        if (dotFromSquare == 22){
            val twentyTwoPiece: MehenPiece? = pieceAt(fromCol, fromRow)
            val fourPiece: MehenPiece? = pieceAt(4, 8)
            if (fourPiece != null){
                piecesBox.remove(fourPiece)
                piecesBox.remove(twentyTwoPiece)
                addPiece(fourPiece.copy(col = 7, row = 1))
                if (twentyTwoPiece != null){
                    addPiece(twentyTwoPiece.copy(col = 4, row = 8))
                }
            }
        }
        if (dotFromSquare == 4){
            val fourPiece: MehenPiece? = pieceAt(fromCol, fromRow)
            val twentyTwoPiece: MehenPiece? = pieceAt(7, 1)
            if (twentyTwoPiece != null){
                piecesBox.remove(twentyTwoPiece)
                piecesBox.remove(fourPiece)
                addPiece(twentyTwoPiece.copy(col = 4, row = 8))
                if (fourPiece != null){
                    addPiece(fourPiece.copy(col = 7, row = 1))
                }
            }
        }

        if (movingPiece.player == Player.BLACK && MehenSingleton.canBlackMove){
            if (movingPiece.mehenman == Mehenman.WALKER){
                if (identy == 0){
                    if (dotToSquare < dotFromSquare + MehenSingleton.blackValueDiceRoll + 1){
                        MehenSingleton.memoryBlack -= dotToSquare - dotFromSquare
                    } else {
                        if (dotFromSquare + MehenSingleton.blackValueDiceRoll + 1 != dotToSquare ) {
                            if (dotFromSquare + MehenSingleton.blackValueDiceRoll + 1 + MehenSingleton.memoryBlack >= dotToSquare){
                                MehenSingleton.memoryBlack -= dotToSquare - dotFromSquare - MehenSingleton.blackValueDiceRoll - 1
                            }
                        }
                    }
                }
            } else {
                if (identy == 0){
                    if (dotToSquare > dotFromSquare - 2*MehenSingleton.blackValueDiceRoll - 2 && dotToSquare < dotFromSquare){
                        MehenSingleton.memoryBlack -= - dotToSquare + dotFromSquare
                    } else {
                        if (dotFromSquare - 2*MehenSingleton.blackValueDiceRoll - 2 != dotToSquare ) {
                            if (dotFromSquare - 2*MehenSingleton.blackValueDiceRoll - 2 - MehenSingleton.memoryBlack <= dotToSquare && dotToSquare < dotFromSquare){
                                MehenSingleton.memoryBlack -= -dotToSquare + dotFromSquare - 2*MehenSingleton.blackValueDiceRoll - 2
                            }
                        }
                    }
                }
            }
            MehenSingleton.canBlackMove = false
            MehenSingleton.canWhiteDiceRoll = true
        }

        if (movingPiece.player == Player.WHITE && MehenSingleton.canWhiteMove){
            if (movingPiece.mehenman == Mehenman.WALKER){
                if (identy == 0){
                    if (dotToSquare < dotFromSquare + MehenSingleton.whiteValueDiceRoll + 1){
                        MehenSingleton.memoryWhite -= dotToSquare - dotFromSquare
                    } else {
                        if (dotFromSquare + MehenSingleton.whiteValueDiceRoll + 1 != dotToSquare ) {
                            if (dotFromSquare + MehenSingleton.whiteValueDiceRoll + 1 + MehenSingleton.memoryWhite >= dotToSquare){
                                MehenSingleton.memoryWhite -= dotToSquare - dotFromSquare - MehenSingleton.whiteValueDiceRoll - 1
                            }
                        }
                    }
                }
            } else {
                if (identy == 0){
                    println(dotFromSquare - 2*MehenSingleton.whiteValueDiceRoll - 2)
                    if (dotToSquare > dotFromSquare - 2*MehenSingleton.whiteValueDiceRoll - 2 && dotToSquare < dotFromSquare){
                        MehenSingleton.memoryWhite -= - dotToSquare + dotFromSquare
                    } else {
                        if (dotFromSquare - 2*MehenSingleton.whiteValueDiceRoll - 2 != dotToSquare ) {
                            if (dotFromSquare - 2*MehenSingleton.whiteValueDiceRoll - 2 - MehenSingleton.memoryWhite <= dotToSquare && dotToSquare < dotFromSquare){
                                MehenSingleton.memoryWhite -= -dotToSquare + dotFromSquare - 2*MehenSingleton.whiteValueDiceRoll - 2
                            }
                        }
                    }
                }
            }
            MehenSingleton.canWhiteMove = false
            MehenSingleton.canBlackDiceRoll = true
        }

        MehenSingleton.possibleDots.clear()
        MehenSingleton.viewPossibleDot = false
        MehenSingleton.outlineList.clear()

        if (MehenSingleton.robot) {
            MehenSingleton.canRobotMove = !MehenSingleton.canRobotMove
            if (MehenSingleton.canRobotMove){
                robot()
            }
        }
    }

    private fun robot(){
        val mapOfSquare: Map<String, Square>
        if (MehenSingleton.soundEffect){ MehenSingleton.soundEngine.play(MehenSingleton.dicerollEffect, 1f, 1f, 1, 0, 1f) }
        if (MehenSingleton.canWhiteDiceRoll){
            MehenSingleton.whiteValueDiceRoll = Random.nextInt(6)
            if (MehenSingleton.whiteValueDiceRoll == 0) {
                MehenSingleton.memoryWhite += 1
                if (MehenSingleton.soundEffect){ MehenSingleton.soundEngine.play(MehenSingleton.magicEffect, 1f, 1f, 1, 0, 1f) }
                robot()
            } else {
                if (isFinish(Player.WHITE) == 0) {
                    MehenSingleton.game = false
                    MehenSingleton.alertBlackWon.show(MehenSingleton.manager, "blackWon")
                    return
                }
                MehenSingleton.canWhiteMove = true
                MehenSingleton.canBlackMove = false
                MehenSingleton.canWhiteDiceRoll = false
                mapOfSquare = robotIQ(Player.WHITE)
                mapOfSquare["fromSquare"]?.let { mapOfSquare["toSquare"]?.let { it1 ->
                    movePiece(it,
                        it1
                    )
                } }
            }
        } else {
            MehenSingleton.blackValueDiceRoll = Random.nextInt(6)
            if (MehenSingleton.blackValueDiceRoll == 0) {
                MehenSingleton.memoryBlack +=1
                if (MehenSingleton.soundEffect){ MehenSingleton.soundEngine.play(MehenSingleton.magicEffect, 1f, 1f, 1, 0, 1f) }
                robot()
            } else {
                if (isFinish(Player.BLACK) == 0) {
                    MehenSingleton.game = false
                    MehenSingleton.alertWhiteWon.show(MehenSingleton.manager, "whiteWon")
                    return
                }
                MehenSingleton.canBlackMove = true
                MehenSingleton.canWhiteMove = false
                MehenSingleton.canBlackDiceRoll = false
                mapOfSquare = robotIQ(Player.BLACK)
                mapOfSquare["fromSquare"]?.let { mapOfSquare["toSquare"]?.let { it1 ->
                    movePiece(it,
                        it1
                    )
                } }
            }
        }
    }

    private fun robotIQ(player: Player): Map<String, Square> {
        var colFromSquare: Int = -1
        var rowFromSquare: Int = -1
        var colToSquare: Int = -1
        var rowToSquare: Int = -1
        val selfPieceSet = mutableListOf<MehenPiece>()
        val rivalPieceSet = mutableListOf<MehenPiece>()
        val priorityForSelfPieces = mutableListOf<Int>()
        val priorityForRivalPieces = mutableListOf<Int>()
        for (piece in piecesBox){
            if (piece.player == player) {
                selfPieceSet.add(piece)
                priorityForSelfPieces.add(0)
            } else {
                rivalPieceSet.add(piece)
                priorityForRivalPieces.add(0)
            }
        }

        when (MehenSingleton.robotIQ){
            0 -> {
                while (true){
                    val randomElement = selfPieceSet[Random.nextInt(selfPieceSet.size)]
                    colFromSquare = randomElement.col
                    rowFromSquare = randomElement.row
                    MehenSingleton.selectedFigure.clear()
                    MehenSingleton.selectedFigure.add(colFromSquare)
                    MehenSingleton.selectedFigure.add(rowFromSquare)
                    MehenSingleton.possibleDots.clear()
                    MehenSingleton.bindingSquare[listOf<Int>(9 - rowFromSquare, colFromSquare)]?.let { it ->
                        findPossibleDots(it, randomElement.player, randomElement.mehenman)
                    }
                    if (MehenSingleton.possibleDots.size != 0){
                        break
                    }
                }
                val randomPossibleDot = MehenSingleton.possibleDots[Random.nextInt(MehenSingleton.possibleDots.size)]
                colToSquare = randomPossibleDot.col
                rowToSquare  = 9 - randomPossibleDot.row
        }
            1 -> {
                selfPieceSet.forEachIndexed { index, item ->
                    MehenSingleton.possibleDots.clear()
                    MehenSingleton.bindingSquare[listOf<Int>(9 - item.row, item.col)]?.let { it ->
                        findPossibleDots(it, item.player, item.mehenman)
                        //если игрок расположен на 22, 28,  клетке +1
                        if (it == 22){ priorityForSelfPieces[index] += 1 }
                        if (it == 28){ priorityForSelfPieces[index] += 1 }

                    }
                    //задаем приоритет по наличию возможных ходов
                    if (MehenSingleton.possibleDots.size != 0){
                        priorityForSelfPieces[index] += 1
                        for (dot in MehenSingleton.possibleDots){
                            //если есть зеленые possibleDots - прибавляем приоритет
                            if (dot.dotColor == basicDotColor){
                                priorityForSelfPieces[index] += 1
                            }
                            MehenSingleton.bindingSquare[listOf<Int>(9 - dot.row, dot.col)]?.let { it ->
                                //если возможный ход попадает в список зеленых клеток - прибавляем приоритет
                                if (it in listOfGreenDotColor){
                                    priorityForSelfPieces[index] += 1
                                    //если при этом possibleDot является basicDotColor
                                    if (dot.dotColor == basicDotColor){
                                        priorityForSelfPieces[index] += 1
                                    }
                                }
                                //если возможный ход попадает на 4 позицию и при этом игрок является пешеходом +1
                                if (it == 4){
                                    priorityForSelfPieces[index] += 1
                                    //если при этом possibleDot является basicDotColor
                                    if (dot.dotColor == basicDotColor){
                                        priorityForSelfPieces[index] += 1
                                    }
                                }
                                val keys = MehenSingleton.bindingSquare.filterValues { it == it }.keys
                                for (key in keys){
                                    //если возможный ход попадает на клетку, где стоит игрок соперника
                                    pieceAt(Square(key[1], 9 - key[0]))?.let{
                                        priorityForSelfPieces[index] += 1
                                    }
                                }
                            }
                        }
                    }
                    MehenSingleton.possibleDots.clear()
                }
                val maxPriorityValue: Int = priorityForSelfPieces.maxOrNull() ?: 0
                val maxPriorityElement = selfPieceSet[priorityForSelfPieces.indexOf(maxPriorityValue)]
                colFromSquare = maxPriorityElement.col
                rowFromSquare = maxPriorityElement.row
                MehenSingleton.selectedFigure.clear()
                MehenSingleton.selectedFigure.add(colFromSquare)
                MehenSingleton.selectedFigure.add(rowFromSquare)
                MehenSingleton.possibleDots.clear()
                MehenSingleton.bindingSquare[listOf<Int>(9 - rowFromSquare, colFromSquare)]?.let { it ->
                    findPossibleDots(it, maxPriorityElement.player, maxPriorityElement.mehenman)
                }
                val listOfPriorityDots = mutableListOf<Int>()
                for (dot in MehenSingleton.possibleDots){
                    if (dot.dotColor == basicDotColor) {
                        listOfPriorityDots.add(1)
                    } else {
                        listOfPriorityDots.add(0)
                    }
                }
                listOfPriorityDots.forEachIndexed { index, element ->
                    //если на possibleDot находится пешка
                    pieceAt(Square(MehenSingleton.possibleDots[index].col, 9 - MehenSingleton.possibleDots[index].row))?.let{
                        listOfPriorityDots[index] += 1
                    }
                    MehenSingleton.bindingSquare[listOf<Int>(9 - MehenSingleton.possibleDots[index].row, MehenSingleton.possibleDots[index].col)]?.let { it ->
                        //если possibleDot попадает на зеленую клетку
                        if (it in listOfGreenDotColor){
                            listOfPriorityDots[index] += 1
                        }
                    }
                }
                val maxPriorityPossibleValue: Int = listOfPriorityDots.maxOrNull() ?: 0
                val maxPriorityPossibleDot = MehenSingleton.possibleDots[listOfPriorityDots.indexOf(maxPriorityPossibleValue)]
                colToSquare = maxPriorityPossibleDot.col
                rowToSquare  = 9 - maxPriorityPossibleDot.row
            }
            2 ->{}
        }
        return mapOf("fromSquare" to Square(colFromSquare, rowFromSquare), "toSquare" to Square(colToSquare, rowToSquare))
    }

    fun isFinish(player: Player): Int {
        var counter: Int = 0
        for (piece in piecesBox){
            if (piece.player == player){
                MehenSingleton.possibleDots.clear()
                MehenSingleton.bindingSquare[listOf<Int>(9 - piece.row, piece.col)]?.let { it ->
                    findPossibleDots(it, player, piece.mehenman)
                }
                counter += if (MehenSingleton.possibleDots.size != 0){ 1 } else { 0 }
            }
        }
        return counter
    }


    fun findPossibleDots(position: Int, player: Player, mehenman: Mehenman){
        var playerAt: Player? = null
        var mehenmanAt: Mehenman? = null

        fun addPossibleDot(i: List<Int>, itPlayer: Player, itMehenman: Mehenman, itColor: String){
            pieceAt(Square(i[1], 9 - i[0]))?.let{
                playerAt = it.player
                mehenmanAt = it.mehenman
            }
            if (playerAt == itPlayer && mehenmanAt == itMehenman){
                playerAt = null
                mehenmanAt = null
            } else {
                MehenSingleton.possibleDots.add(PossibleDot(i[1], i[0], itColor))
                playerAt = null
                mehenmanAt = null
            }
        }

        if (mehenman == Mehenman.WALKER){
            if (player == Player.WHITE){
                for (dot in position + MehenSingleton.whiteValueDiceRoll + 2 until position + MehenSingleton.whiteValueDiceRoll + MehenSingleton.memoryWhite + 2){
                    val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                    for (i in keys){
                        addPossibleDot(i, Player.WHITE, Mehenman.WALKER, additionalDotColor)
                    }
                }
                for (dot in position + 1 until position + MehenSingleton.memoryWhite + 1){
                    val whiteDot = position + MehenSingleton.whiteValueDiceRoll + 1
                    if (dot != whiteDot){
                        val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                        for (i in keys){
                            addPossibleDot(i, Player.WHITE, Mehenman.WALKER, additionalDotColor)
                        }
                    }
                }
                val diceRollKeys = MehenSingleton.bindingSquare.filterValues { it == position + MehenSingleton.whiteValueDiceRoll + 1 }.keys
                for (i in diceRollKeys){
                    addPossibleDot(i, Player.WHITE, Mehenman.WALKER, basicDotColor)
                }
                if (MehenSingleton.whiteValueDiceRoll == 5){ when (position){
                    0 -> addPossibleDot(listOf(2, 7), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    6 -> addPossibleDot(listOf(2, 1), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    12 -> addPossibleDot(listOf(4, 1), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    18 -> addPossibleDot(listOf(7, 4), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    24 -> addPossibleDot(listOf(5, 6), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    28 -> addPossibleDot(listOf(3, 6), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    34 -> addPossibleDot(listOf(3, 2), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    36 -> {
                        addPossibleDot(listOf(3, 2), Player.WHITE, Mehenman.WALKER, basicDotColor)
                        addPossibleDot(listOf(5, 2), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    }
                    42 -> addPossibleDot(listOf(6, 5), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    46 -> {
                        addPossibleDot(listOf(4, 5), Player.WHITE, Mehenman.WALKER, basicDotColor)
                        addPossibleDot(listOf(6, 5), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    }
                    48 -> addPossibleDot(listOf(4, 5), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    52 -> addPossibleDot(listOf(4, 3), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    54 -> addPossibleDot(listOf(4, 3), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    58 -> addPossibleDot(listOf(5, 4), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    60 -> addPossibleDot(listOf(5, 4), Player.WHITE, Mehenman.WALKER, basicDotColor)
                    62 -> addPossibleDot(listOf(5, 4), Player.WHITE, Mehenman.WALKER, basicDotColor)
                } }
            } else {
                for (dot in position + MehenSingleton.blackValueDiceRoll + 2 until position + MehenSingleton.blackValueDiceRoll + MehenSingleton.memoryBlack + 2){
                    val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                    for (i in keys){
                        addPossibleDot(i, Player.BLACK, Mehenman.WALKER, additionalDotColor)
                    }
                }
                for (dot in position + 1 until position + MehenSingleton.memoryBlack + 1){
                    val blackDot = position + MehenSingleton.blackValueDiceRoll + 1
                    if (dot != blackDot){
                        val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                        for (i in keys){
                            addPossibleDot(i, Player.BLACK, Mehenman.WALKER, additionalDotColor)
                        }
                    }
                }
                val diceRollKeys = MehenSingleton.bindingSquare.filterValues { it == position + MehenSingleton.blackValueDiceRoll + 1 }.keys
                for (i in diceRollKeys){
                    addPossibleDot(i, Player.BLACK, Mehenman.WALKER, basicDotColor)
                }
                if (MehenSingleton.blackValueDiceRoll == 5){ when (position){
                    0 -> addPossibleDot(listOf(2, 7), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    6 -> addPossibleDot(listOf(2, 1), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    12 -> addPossibleDot(listOf(4, 1), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    18 -> addPossibleDot(listOf(7, 4), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    24 -> addPossibleDot(listOf(5, 6), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    28 -> addPossibleDot(listOf(3, 6), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    34 -> addPossibleDot(listOf(3, 2), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    36 -> {
                        addPossibleDot(listOf(3, 2), Player.BLACK, Mehenman.WALKER, basicDotColor)
                        addPossibleDot(listOf(5, 2), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    }
                    42 -> addPossibleDot(listOf(6, 5), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    46 -> {
                        addPossibleDot(listOf(4, 5), Player.BLACK, Mehenman.WALKER, basicDotColor)
                        addPossibleDot(listOf(6, 5), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    }
                    48 -> addPossibleDot(listOf(4, 5), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    52 -> addPossibleDot(listOf(4, 3), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    54 -> addPossibleDot(listOf(4, 3), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    58 -> addPossibleDot(listOf(5, 4), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    60 -> addPossibleDot(listOf(5, 4), Player.BLACK, Mehenman.WALKER, basicDotColor)
                    62 -> addPossibleDot(listOf(5, 4), Player.BLACK, Mehenman.WALKER, basicDotColor)
                } }
            }
        } else {
            if (player == Player.WHITE){
                for (dot in position - 2*(MehenSingleton.whiteValueDiceRoll + 1) - MehenSingleton.memoryWhite until position - 2*(MehenSingleton.whiteValueDiceRoll + 1)){
                    val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                    for (i in keys){
                        addPossibleDot(i, Player.WHITE, Mehenman.LION, additionalDotColor)
                    }
                }
                for (dot in position - MehenSingleton.memoryWhite until position){
                    val whiteDot = position - 2*(MehenSingleton.whiteValueDiceRoll + 1)
                    if (dot != whiteDot){
                        val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                        for (i in keys){
                            addPossibleDot(i, Player.WHITE, Mehenman.LION, additionalDotColor)
                        }
                    }
                }
                val diceRollKeys = MehenSingleton.bindingSquare.filterValues { it == position - 2*(MehenSingleton.whiteValueDiceRoll + 1) }.keys
                for (i in diceRollKeys){
                    addPossibleDot(i, Player.WHITE, Mehenman.LION, basicDotColor)
                }
                if (MehenSingleton.whiteValueDiceRoll == 5){ when (position){
                    64 -> {
                        addPossibleDot(listOf(4, 3), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(4, 5), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(6, 5), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    62 -> {
                        addPossibleDot(listOf(3, 2), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(5, 2), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(5, 4), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    60 -> {
                        addPossibleDot(listOf(5, 4), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(3, 6), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(5, 6), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    58 -> {
                        addPossibleDot(listOf(5, 4), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(5, 6), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(7, 4), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    54 -> {
                        addPossibleDot(listOf(4, 1), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(4, 3), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    52 -> {
                        addPossibleDot(listOf(4, 3), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(4, 1), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(2, 1), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    48 -> {
                        addPossibleDot(listOf(4, 5), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(2, 7), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    46 -> {
                        addPossibleDot(listOf(4, 5), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(6, 5), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(6, 7), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    42 -> {
                        addPossibleDot(listOf(6, 5), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(8, 3), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    36 -> {
                        addPossibleDot(listOf(5, 0), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(5, 2), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(3, 2), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    34 -> {
                        addPossibleDot(listOf(1, 2), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(3, 2), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    28 -> {
                        addPossibleDot(listOf(3, 6), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 0), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 1), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 2), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 3), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 4), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 5), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 0), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 1), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 2), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 3), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 4), Player.WHITE, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 5), Player.WHITE, Mehenman.LION, basicDotColor)
                    }
                    24 -> { addPossibleDot(listOf(5, 6), Player.WHITE, Mehenman.LION, basicDotColor) }
                    18 -> { addPossibleDot(listOf(7, 4), Player.WHITE, Mehenman.LION, basicDotColor) }
                    12 -> { addPossibleDot(listOf(4, 1), Player.WHITE, Mehenman.LION, basicDotColor) }
                    6-> { addPossibleDot(listOf(2, 1), Player.WHITE, Mehenman.LION, basicDotColor) }
                    0 -> { addPossibleDot(listOf(2, 7), Player.WHITE, Mehenman.LION, basicDotColor) }
                } }
            } else {
                for (dot in position - 2*(MehenSingleton.blackValueDiceRoll + 1) - MehenSingleton.memoryBlack until position - 2*(MehenSingleton.blackValueDiceRoll + 1)){
                    val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                    for (i in keys){
                        addPossibleDot(i, Player.BLACK, Mehenman.LION, additionalDotColor)
                    }
                }
                for (dot in position - MehenSingleton.memoryBlack until position){
                    val blackDot = position - 2*(MehenSingleton.blackValueDiceRoll + 1)
                    if (dot != blackDot){
                        val keys = MehenSingleton.bindingSquare.filterValues { it == dot }.keys
                        for (i in keys){
                            addPossibleDot(i, Player.BLACK, Mehenman.LION, additionalDotColor)
                        }
                    }
                }
                val diceRollKeys = MehenSingleton.bindingSquare.filterValues { it == position - 2*(MehenSingleton.blackValueDiceRoll + 1) }.keys
                for (i in diceRollKeys){
                    addPossibleDot(i, Player.BLACK, Mehenman.LION, basicDotColor)
                }
                if (MehenSingleton.blackValueDiceRoll == 5){ when (position){
                    64 -> {
                        addPossibleDot(listOf(4, 3), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(4, 5), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(6, 5), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    62 -> {
                        addPossibleDot(listOf(3, 2), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(5, 2), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(5, 4), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    60 -> {
                        addPossibleDot(listOf(5, 4), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(3, 6), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(5, 6), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    58 -> {
                        addPossibleDot(listOf(5, 4), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(5, 6), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(7, 4), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    54 -> {
                        addPossibleDot(listOf(4, 1), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(4, 3), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    52 -> {
                        addPossibleDot(listOf(4, 3), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(4, 1), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(2, 1), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    48 -> {
                        addPossibleDot(listOf(4, 5), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(2, 7), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    46 -> {
                        addPossibleDot(listOf(4, 5), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(6, 5), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(6, 7), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    42 -> {
                        addPossibleDot(listOf(6, 5), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(8, 3), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    36 -> {
                        addPossibleDot(listOf(5, 0), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(5, 2), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(3, 2), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    34 -> {
                        addPossibleDot(listOf(1, 2), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(3, 2), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    28 -> {
                        addPossibleDot(listOf(3, 6), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 0), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 1), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 2), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 3), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 4), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(0, 5), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 0), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 1), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 2), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 3), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 4), Player.BLACK, Mehenman.LION, basicDotColor)
                        addPossibleDot(listOf(9, 5), Player.BLACK, Mehenman.LION, basicDotColor)
                    }
                    24 -> { addPossibleDot(listOf(5, 6), Player.BLACK, Mehenman.LION, basicDotColor) }
                    18 -> { addPossibleDot(listOf(7, 4), Player.BLACK, Mehenman.LION, basicDotColor) }
                    12 -> { addPossibleDot(listOf(4, 1), Player.BLACK, Mehenman.LION, basicDotColor) }
                    6-> { addPossibleDot(listOf(2, 1), Player.BLACK, Mehenman.LION, basicDotColor) }
                    0 -> { addPossibleDot(listOf(2, 7), Player.BLACK, Mehenman.LION, basicDotColor) }
                } }
            }
        }
    }

    fun reset() {
        if (MehenSingleton.game){
            if (MehenSingleton.soundEffect){ MehenSingleton.soundEngine.play(MehenSingleton.startgameEffect, 1f, 1f, 1, 0, 1f) }
        }

        clear()
        for (i in 0 until 6) {
            addPiece(MehenPiece(i, 0, Player.WHITE, Mehenman.WALKER, R.drawable.white_goose))
            addPiece(MehenPiece(i, 9, Player.BLACK, Mehenman.WALKER, R.drawable.black_goose))
        }
        MehenSingleton.memoryBlack = 0
        MehenSingleton.memoryWhite = 0
        MehenSingleton.canWhiteMove = false
        MehenSingleton.canBlackMove = false
        MehenSingleton.canWhiteDiceRoll = true
        MehenSingleton.canBlackDiceRoll = false
        MehenSingleton.whiteValueDiceRoll = 5
        MehenSingleton.blackValueDiceRoll = 5
        MehenSingleton.possibleDots.clear()
        MehenSingleton.viewPossibleDot = false
        MehenSingleton.outlineList.clear()
        MehenSingleton.selectedFigure.clear()
    }

    fun pieceAt(square: Square): MehenPiece? {
        return pieceAt(square.col, square.row)
    }

    private fun pieceAt(col: Int, row: Int): MehenPiece? {
        for (piece in piecesBox) {
            if (col == piece.col && row == piece.row) {
                return piece
            }
        }
        return null
    }

    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0) {
            desc += "$row"
            for (col in 0..7) {
                val piece = pieceAt(col, row)
                if (piece == null) {
                    desc += " ."
                } else {
                    val white = piece.player == Player.WHITE
                    desc += " "
                    desc += when (piece.mehenman) {
                        Mehenman.WALKER -> {
                            if (white) "o" else "x"
                        }
                        Mehenman.LION -> {
                            if (white) "O" else "X"
                        }
                    }
                }

            }
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"
        return desc
    }
}
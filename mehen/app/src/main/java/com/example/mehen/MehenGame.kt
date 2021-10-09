package com.example.mehen

object MehenGame {
    private var piecesBox = mutableSetOf<MehenPiece>()

    init {
        reset()
    }

    fun clear(){
        piecesBox.clear()
    }

    fun addPiece(piece: MehenPiece) {
        piecesBox.add(piece)
    }

    private fun canWalkerMove(from: Square, to: Square): Boolean {
        if (true) {
            return true
        }
        return false
    }

    private fun canLionMove(from: Square, to: Square): Boolean {
        if (true) {
            return true
        }
        return false
    }

    fun canMove(from: Square, to: Square): Boolean {
        if (from.col == to.col && from.row == to.row) { return  false }
        else if (0 > to.row) { return  false }
        else if (9 < to.row) { return  false }
        else if (7 == to.col && 0 == to.row) { return  false }
        else if (7 == to.col && 9 == to.row) { return  false }
        else if (6 == to.col && 0 == to.row) { return  false }
        else if (6 == to.col && 9 == to.row) { return  false }
        val movingPiece = pieceAt(from) ?: return false
        return when(movingPiece.mehenman) {
            Mehenman.WALKER -> canWalkerMove(from, to)
            Mehenman.LION -> canLionMove(from, to)
        }
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
                    } else return
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
    }

    fun reset() {
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
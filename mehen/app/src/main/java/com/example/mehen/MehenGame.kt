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
        val movingPiece = pieceAt(fromCol, fromRow) ?: return
        val itPiece = pieceAt(toCol, toRow)

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
        if (movingPiece.player == Player.BLACK && MehenSingleton.canBlackMove){
            MehenSingleton.canBlackMove = false
            MehenSingleton.canWhiteDiceRoll = true
        }
        if (movingPiece.player == Player.WHITE && MehenSingleton.canWhiteMove){
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
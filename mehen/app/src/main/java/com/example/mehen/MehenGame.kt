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

        pieceAt(toCol, toRow)?.let {
            if (it.player == movingPiece.player) {
                return
            }
            piecesBox.remove(it)
        }

        piecesBox.remove(movingPiece)
        addPiece(movingPiece.copy(col = toCol, row = toRow))
    }

    fun reset() {
        clear()
        for (i in 0 until 6) {
            addPiece(MehenPiece(i, 0, Player.WHITE, Mehenman.WALKER, R.drawable.white_goose))
            addPiece(MehenPiece(i, 9, Player.BLACK, Mehenman.WALKER, R.drawable.black_goose))
        }
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
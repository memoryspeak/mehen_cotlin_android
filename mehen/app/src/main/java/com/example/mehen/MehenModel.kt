package com.example.mehen

class MehenModel {
    var piecesBox = mutableSetOf<MehenPiece>()

    init {
        reset()
        //piecesBox.add(MehenPiece(0, 0, MehenPlayer.WHITE, MehenRank.WALKER))
        //piecesBox.add(MehenPiece(0, 7, MehenPlayer.BLACK, MehenRank.LION))
    }

    private fun reset() {
        piecesBox.removeAll(piecesBox)
    }

    private fun pieceAt(col: Int, row: Int) : MehenPiece? {
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
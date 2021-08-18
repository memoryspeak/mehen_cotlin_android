package com.example.mehen

interface MehenDelegate {
    fun pieceAt(square: Square) : MehenPiece?
    fun movePiece(from: Square, to: Square)
}
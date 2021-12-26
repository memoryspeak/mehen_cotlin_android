package com.example.mehen

import com.google.firebase.auth.FirebaseAuth

interface MehenDelegate {
    fun pieceAt(square: Square) : MehenPiece?
    fun movePiece(from: Square, to: Square)
    fun findPossibleDots(position: Int, player: Player, mehenman: Mehenman)
    fun isFinish(player: Player) : Int
    /*fun addAuthStateListener(authStateListener: FirebaseAuth.AuthStateListener)*/
}
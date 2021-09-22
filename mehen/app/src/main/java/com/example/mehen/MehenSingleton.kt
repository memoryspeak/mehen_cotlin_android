package com.example.mehen

object MehenSingleton {
    var memoryWhite: Int = 0
    var memoryBlack: Int = 0
    var canWhiteMove: Boolean = false
    var canBlackMove: Boolean = false
    var canWhiteDiceRoll: Boolean = true
    var canBlackDiceRoll: Boolean = false
    var whiteValueDiceRoll: Int = 5
    var blackValueDiceRoll: Int = 5

    var viewPossibleDot: Boolean = false
    var possibleDots = mutableListOf<PossibleDot>()

    val bindingSquare = mapOf<Set<Int>, Int>(
        setOf<Int>(0, 0) to 0,
        setOf<Int>(0, 1) to 0,
        setOf<Int>(0, 2) to 0,
        setOf<Int>(0, 3) to 0,
        setOf<Int>(0, 4) to 0,
        setOf<Int>(0, 5) to 0,
        setOf<Int>(0, 6) to 0,
        setOf<Int>(0, 7) to 0,
        setOf<Int>(1, 7) to 1,
        setOf<Int>(1, 6) to 2,
        setOf<Int>(1, 5) to 3,
        setOf<Int>(1, 4) to 4,
        setOf<Int>(1, 3) to 5,
        setOf<Int>(1, 2) to 6,
        setOf<Int>(1, 1) to 7,
        setOf<Int>(1, 0) to 8,
        setOf<Int>(2, 0) to 9,
        setOf<Int>(3, 0) to 10,
        setOf<Int>(4, 0) to 11,
        setOf<Int>(5, 0) to 12,
        setOf<Int>(6, 0) to 13,
        setOf<Int>(7, 0) to 14,
        setOf<Int>(8, 0) to 15,
        setOf<Int>(8, 1) to 16,
        setOf<Int>(8, 2) to 17,
        setOf<Int>(8, 3) to 18,
        setOf<Int>(8, 4) to 19,
        setOf<Int>(8, 5) to 20,
        setOf<Int>(8, 6) to 21,
        setOf<Int>(8, 7) to 22,
        setOf<Int>(7, 7) to 23,
        setOf<Int>(6, 7) to 24,
        setOf<Int>(5, 7) to 25,
        setOf<Int>(4, 7) to 26,
        setOf<Int>(3, 7) to 27,
        setOf<Int>(2, 7) to 28,
        setOf<Int>(2, 6) to 29,
        setOf<Int>(2, 5) to 30,
        setOf<Int>(2, 4) to 31,
        setOf<Int>(2, 3) to 32,
        setOf<Int>(2, 2) to 33,
        setOf<Int>(2, 1) to 34,
        setOf<Int>(3, 1) to 35,
        setOf<Int>(4, 1) to 36,
        setOf<Int>(5, 1) to 37,
        setOf<Int>(6, 1) to 38,
        setOf<Int>(7, 1) to 39,
        setOf<Int>(7, 2) to 40,
        setOf<Int>(7, 3) to 41,
        setOf<Int>(7, 4) to 42,
        setOf<Int>(7, 5) to 43,
        setOf<Int>(7, 6) to 44,
        setOf<Int>(6, 6) to 45,
        setOf<Int>(5, 6) to 46,
        setOf<Int>(4, 6) to 47,
        setOf<Int>(3, 6) to 48,
        setOf<Int>(3, 5) to 49,
        setOf<Int>(3, 4) to 50,
        setOf<Int>(3, 3) to 51,
        setOf<Int>(3, 2) to 52,
        setOf<Int>(4, 2) to 53,
        setOf<Int>(5, 2) to 54,
        setOf<Int>(6, 2) to 55,
        setOf<Int>(6, 3) to 56,
        setOf<Int>(6, 4) to 57,
        setOf<Int>(6, 5) to 58,
        setOf<Int>(5, 5) to 59,
        setOf<Int>(4, 5) to 60,
        setOf<Int>(4, 4) to 61,
        setOf<Int>(4, 3) to 62,
        setOf<Int>(5, 3) to 63,
        setOf<Int>(5, 4) to 64,
        setOf<Int>(9, 0) to 0,
        setOf<Int>(9, 1) to 0,
        setOf<Int>(9, 2) to 0,
        setOf<Int>(9, 3) to 0,
        setOf<Int>(9, 4) to 0,
        setOf<Int>(9, 5) to 0,
        setOf<Int>(9, 6) to 0,
        setOf<Int>(9, 7) to 0
    )
}
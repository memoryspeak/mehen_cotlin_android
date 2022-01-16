package com.example.mehen

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import java.net.ServerSocket
import kotlin.properties.Delegates

object MehenSingleton {
    lateinit var activityMainIntent: Intent
    lateinit var activityLoginIntent: Intent
    lateinit var activityRegisterIntent: Intent
    lateinit var activityNetworkIntent: Intent

    var login: String? = ""
    var email: String? = ""
    var emailVerified: Boolean? = false
    var userID: String? = ""
    var rating: String? = ""
    lateinit var gameNameSelf: String

    var gameNamePlaying: String = ""
    var networkGame: Boolean = false
    var networkWhiteUserName: String = ""
    var networkBlackUserName: String = ""
    var networkMemoryWhite: Int = 0
    var networkMemoryBlack: Int = 0
    var networkCanWhiteMove: Boolean = false
    var networkCanBlackMove: Boolean = false
    var networkCanWhiteDiceRoll: Boolean = false
    var networkCanBlackDiceRoll: Boolean = false
    var networkWhiteValueDiceRoll: Int = 5
    var networkBlackValueDiceRoll: Int = 5
    var fromColfromRowtoColtoRow: String = ""
    /*var networkBoardString: String = ""*/
    /*var networkBoardMap = mutableMapOf<List<Int>, String>(
        listOf<Int>(0, 0) to "11",
        listOf<Int>(0, 1) to "11",
        listOf<Int>(0, 2) to "11",
        listOf<Int>(0, 3) to "11",
        listOf<Int>(0, 4) to "11",
        listOf<Int>(0, 5) to "11",
        listOf<Int>(1, 7) to "00",
        listOf<Int>(1, 6) to "00",
        listOf<Int>(1, 5) to "00",
        listOf<Int>(1, 4) to "00",
        listOf<Int>(1, 3) to "00",
        listOf<Int>(1, 2) to "00",
        listOf<Int>(1, 1) to "00",
        listOf<Int>(1, 0) to "00",
        listOf<Int>(2, 0) to "00",
        listOf<Int>(3, 0) to "00",
        listOf<Int>(4, 0) to "00",
        listOf<Int>(5, 0) to "00",
        listOf<Int>(6, 0) to "00",
        listOf<Int>(7, 0) to "00",
        listOf<Int>(8, 0) to "00",
        listOf<Int>(8, 1) to "00",
        listOf<Int>(8, 2) to "00",
        listOf<Int>(8, 3) to "00",
        listOf<Int>(8, 4) to "00",
        listOf<Int>(8, 5) to "00",
        listOf<Int>(8, 6) to "00",
        listOf<Int>(8, 7) to "00",
        listOf<Int>(7, 7) to "00",
        listOf<Int>(6, 7) to "00",
        listOf<Int>(5, 7) to "00",
        listOf<Int>(4, 7) to "00",
        listOf<Int>(3, 7) to "00",
        listOf<Int>(2, 7) to "00",
        listOf<Int>(2, 6) to "00",
        listOf<Int>(2, 5) to "00",
        listOf<Int>(2, 4) to "00",
        listOf<Int>(2, 3) to "00",
        listOf<Int>(2, 2) to "00",
        listOf<Int>(2, 1) to "00",
        listOf<Int>(3, 1) to "00",
        listOf<Int>(4, 1) to "00",
        listOf<Int>(5, 1) to "00",
        listOf<Int>(6, 1) to "00",
        listOf<Int>(7, 1) to "00",
        listOf<Int>(7, 2) to "00",
        listOf<Int>(7, 3) to "00",
        listOf<Int>(7, 4) to "00",
        listOf<Int>(7, 5) to "00",
        listOf<Int>(7, 6) to "00",
        listOf<Int>(6, 6) to "00",
        listOf<Int>(5, 6) to "00",
        listOf<Int>(4, 6) to "00",
        listOf<Int>(3, 6) to "00",
        listOf<Int>(3, 5) to "00",
        listOf<Int>(3, 4) to "00",
        listOf<Int>(3, 3) to "00",
        listOf<Int>(3, 2) to "00",
        listOf<Int>(4, 2) to "00",
        listOf<Int>(5, 2) to "00",
        listOf<Int>(6, 2) to "00",
        listOf<Int>(6, 3) to "00",
        listOf<Int>(6, 4) to "00",
        listOf<Int>(6, 5) to "00",
        listOf<Int>(5, 5) to "00",
        listOf<Int>(4, 5) to "00",
        listOf<Int>(4, 4) to "00",
        listOf<Int>(4, 3) to "00",
        listOf<Int>(5, 3) to "00",
        listOf<Int>(5, 4) to "00",
        listOf<Int>(9, 0) to "21",
        listOf<Int>(9, 1) to "21",
        listOf<Int>(9, 2) to "21",
        listOf<Int>(9, 3) to "21",
        listOf<Int>(9, 4) to "21",
        listOf<Int>(9, 5) to "21"
    )*/


    const val memoryLimit: Int = 100

    //lateinit var db: DataBaseHandler

    lateinit var alertNewGame: MehenGameSelectionDialogFragment
    lateinit var alertRobotGame: MehenGameSelectionDialogFragment
    var selectedItemOfNewGame: Int = 0

    var serverSocket: ServerSocket? = null
    lateinit var mehenView: MehenView

    var game: Boolean = false
    var robot: Boolean = false
    var canRobotMove: Boolean = false
    var robotIQ: Int = 1

    lateinit var manager: FragmentManager
    lateinit var alertWhiteWon: MehenDialogFragment
    lateinit var alertBlackWon: MehenDialogFragment
    lateinit var alertEmailSend: MehenDialogFragment

    var soundEffect: Boolean = true
    val soundEngine = SoundEngine()
    var magicEffect: Int = 0
    var turnEffect: Int = 0
    var startgameEffect: Int = 0
    var dicerollEffect: Int = 0

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

    var outlineList = mutableListOf<Int>()

    var selectedFigure = mutableListOf<Int>()

    val bindingSquare = mapOf<List<Int>, Int>(
        listOf<Int>(0, 0) to 0,
        listOf<Int>(0, 1) to 0,
        listOf<Int>(0, 2) to 0,
        listOf<Int>(0, 3) to 0,
        listOf<Int>(0, 4) to 0,
        listOf<Int>(0, 5) to 0,
        listOf<Int>(1, 7) to 1,
        listOf<Int>(1, 6) to 2,
        listOf<Int>(1, 5) to 3,
        listOf<Int>(1, 4) to 4,
        listOf<Int>(1, 3) to 5,
        listOf<Int>(1, 2) to 6,
        listOf<Int>(1, 1) to 7,
        listOf<Int>(1, 0) to 8,
        listOf<Int>(2, 0) to 9,
        listOf<Int>(3, 0) to 10,
        listOf<Int>(4, 0) to 11,
        listOf<Int>(5, 0) to 12,
        listOf<Int>(6, 0) to 13,
        listOf<Int>(7, 0) to 14,
        listOf<Int>(8, 0) to 15,
        listOf<Int>(8, 1) to 16,
        listOf<Int>(8, 2) to 17,
        listOf<Int>(8, 3) to 18,
        listOf<Int>(8, 4) to 19,
        listOf<Int>(8, 5) to 20,
        listOf<Int>(8, 6) to 21,
        listOf<Int>(8, 7) to 22,
        listOf<Int>(7, 7) to 23,
        listOf<Int>(6, 7) to 24,
        listOf<Int>(5, 7) to 25,
        listOf<Int>(4, 7) to 26,
        listOf<Int>(3, 7) to 27,
        listOf<Int>(2, 7) to 28,
        listOf<Int>(2, 6) to 29,
        listOf<Int>(2, 5) to 30,
        listOf<Int>(2, 4) to 31,
        listOf<Int>(2, 3) to 32,
        listOf<Int>(2, 2) to 33,
        listOf<Int>(2, 1) to 34,
        listOf<Int>(3, 1) to 35,
        listOf<Int>(4, 1) to 36,
        listOf<Int>(5, 1) to 37,
        listOf<Int>(6, 1) to 38,
        listOf<Int>(7, 1) to 39,
        listOf<Int>(7, 2) to 40,
        listOf<Int>(7, 3) to 41,
        listOf<Int>(7, 4) to 42,
        listOf<Int>(7, 5) to 43,
        listOf<Int>(7, 6) to 44,
        listOf<Int>(6, 6) to 45,
        listOf<Int>(5, 6) to 46,
        listOf<Int>(4, 6) to 47,
        listOf<Int>(3, 6) to 48,
        listOf<Int>(3, 5) to 49,
        listOf<Int>(3, 4) to 50,
        listOf<Int>(3, 3) to 51,
        listOf<Int>(3, 2) to 52,
        listOf<Int>(4, 2) to 53,
        listOf<Int>(5, 2) to 54,
        listOf<Int>(6, 2) to 55,
        listOf<Int>(6, 3) to 56,
        listOf<Int>(6, 4) to 57,
        listOf<Int>(6, 5) to 58,
        listOf<Int>(5, 5) to 59,
        listOf<Int>(4, 5) to 60,
        listOf<Int>(4, 4) to 61,
        listOf<Int>(4, 3) to 62,
        listOf<Int>(5, 3) to 63,
        listOf<Int>(5, 4) to 64,
        listOf<Int>(9, 0) to 0,
        listOf<Int>(9, 1) to 0,
        listOf<Int>(9, 2) to 0,
        listOf<Int>(9, 3) to 0,
        listOf<Int>(9, 4) to 0,
        listOf<Int>(9, 5) to 0
    )
}
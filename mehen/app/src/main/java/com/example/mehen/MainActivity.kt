package com.example.mehen

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.PrintWriter
import java.net.ConnectException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.*
import java.util.concurrent.Executors

private const val TAG = "MainActivity"

class MainActivity() : AppCompatActivity(), MehenDelegate {
    private val socketHost = "127.0.0.1"
    private val socketPort: Int = 50000
    private val socketGuestPort: Int = 50001 // used for socket server on emulator
    private lateinit var mehenView: MehenView
//    private lateinit var resetButton: Button
//    private lateinit var listenButton: Button
//    private lateinit var connectButton: Button
    private var printWriter: PrintWriter? = null
    private var serverSocket: ServerSocket? = null
    private val isEmulator = Build.FINGERPRINT.contains("generic")

    override fun onCreate(savedInstanceState: Bundle?) {
        MehenSingleton.magicEffect = MehenSingleton.soundEngine.load(this, R.raw.magic, 1)
        MehenSingleton.turnEffect = MehenSingleton.soundEngine.load(this, R.raw.turn, 1)
        MehenSingleton.startgameEffect = MehenSingleton.soundEngine.load(this, R.raw.startgame, 1)
        MehenSingleton.dicerollEffect = MehenSingleton.soundEngine.load(this, R.raw.diceroll, 1)

        setTheme(R.style.Theme_Mehen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        MehenSingleton.magicEffect = MehenSingleton.soundEngine.load(this, R.raw.magic, 1)
//        MehenSingleton.turnEffect = MehenSingleton.soundEngine.load(this, R.raw.turn, 1)
//        MehenSingleton.startgameEffect = MehenSingleton.soundEngine.load(this, R.raw.startgame, 1)
//        MehenSingleton.dicerollEffect = MehenSingleton.soundEngine.load(this, R.raw.diceroll, 1)

        mehenView = findViewById<MehenView>(R.id.mehen_view)
//        resetButton = findViewById<Button>(R.id.reset_button)
//        listenButton = findViewById<Button>(R.id.listen_button)
//        connectButton = findViewById<Button>(R.id.connect_button)
        mehenView.mehenDelegate = this

//        resetButton.setOnClickListener {
//            MehenGame.reset()
//            mehenView.invalidate()
//            serverSocket?.close()
//            listenButton.isEnabled = true
//        }

//        listenButton.setOnClickListener {
//            listenButton.isEnabled = false
//            val port = if (isEmulator) socketGuestPort else socketPort
//            Toast.makeText(this, "listening on $port", Toast.LENGTH_SHORT).show()
//            Executors.newSingleThreadExecutor().execute {
//                ServerSocket(port).let { srvSkt ->
//                    serverSocket = srvSkt
//                    try {
//                        val socket = srvSkt.accept()
//                        receiveMove(socket)
//                    } catch (e: SocketException) {
//                        // ignored, socket closed
//                    }
//                }
//            }
//        }
//
//        connectButton.setOnClickListener {
//            Log.d(TAG, "socket client connecting ...")
//            Executors.newSingleThreadExecutor().execute {
//                try {
//                    val socket = Socket(socketHost, socketPort)
//                    receiveMove(socket)
//                } catch (e: ConnectException) {
//                    runOnUiThread {
//                        Toast.makeText(this, "connection failed", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.right_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.opposite -> {
                MehenSingleton.game = true
                MehenSingleton.robot = false
                MehenSingleton.canRobotMove = false
                MehenGame.reset()
                mehenView.invalidate()
                serverSocket?.close()
//                listenButton.isEnabled = true
            }
            R.id.robot -> {
                MehenSingleton.game = true
                MehenSingleton.robot = true
                MehenSingleton.canRobotMove = false
                MehenGame.reset()
                mehenView.invalidate()
                serverSocket?.close()
            }
        }
        return true
    }

    private fun receiveMove(socket: Socket) {
        val scanner = Scanner(socket.getInputStream())
        printWriter = PrintWriter(socket.getOutputStream(), true)
        while (scanner.hasNextLine()) {
            val move = scanner.nextLine().split(",").map { it.toInt() }
            runOnUiThread {
                MehenGame.movePiece(Square(move[0], move[1]), Square(move[2], move[3]))
                mehenView.invalidate()
            }
        }
    }

    override fun pieceAt(square: Square): MehenPiece? = MehenGame.pieceAt(square)

    override fun movePiece(from: Square, to: Square) {
        MehenGame.movePiece(from, to)
        mehenView.invalidate()

        printWriter?.let {
            val moveStr = "${from.col},${from.row},${to.col},${to.row}"
            Executors.newSingleThreadExecutor().execute {
                it.println(moveStr)
            }
        }
    }

    override fun findPossibleDots(position: Int, player: Player, mehenman: Mehenman) {
        MehenGame.findPossibleDots(position, player, mehenman)
    }
}
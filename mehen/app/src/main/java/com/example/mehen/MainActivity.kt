package com.example.mehen

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.net.Socket
import java.sql.Connection
import java.util.*

//private const val TAG = "MainActivity"

class MainActivity() : AppCompatActivity(), MehenDelegate {
    //private var printWriter: PrintWriter? = null
    //private val isEmulator = Build.FINGERPRINT.contains("generic")


    override fun onCreate(savedInstanceState: Bundle?) {
        MehenSingleton.clientSocket = ClientSocket()
        //MehenSingleton.db = DataBaseHandler(this)
        //MehenSingleton.mainActivityIntent = Intent(this, MainActivity::class.java)
        MehenSingleton.activityMainIntent = Intent(this,MainActivity::class.java)
        MehenSingleton.activityLoginIntent = Intent(this, LoginActivity::class.java)
        MehenSingleton.activityRegisterIntent = Intent(this, RegisterActivity::class.java)
        MehenSingleton.activityNetworkIntent = Intent(this, NetworkActivity::class.java)

        MehenSingleton.manager = supportFragmentManager
        MehenSingleton.alertWhiteWon = MehenDialogFragment(
            "White won!",
            "Congratulations!",
            R.drawable.white_lion,
            "OK")
        MehenSingleton.alertBlackWon = MehenDialogFragment(
            "Black won!",
            "Congratulations!",
            R.drawable.black_lion,
            "OK")
        MehenSingleton.alertEmailSend = MehenDialogFragment(
            "Congratulations!",
            "A confirmation link has been sent to the specified email address.\n" +
                    "Follow the link to complete registration.",
            R.drawable.lion,
            "OK")
        MehenSingleton.alertNewGame = MehenGameSelectionDialogFragment(false)
        MehenSingleton.alertRobotGame = MehenGameSelectionDialogFragment(true)

        MehenSingleton.magicEffect = MehenSingleton.soundEngine.load(this, R.raw.magic, 1)
        MehenSingleton.turnEffect = MehenSingleton.soundEngine.load(this, R.raw.turn, 1)
        MehenSingleton.startgameEffect = MehenSingleton.soundEngine.load(this, R.raw.startgame, 1)
        MehenSingleton.dicerollEffect = MehenSingleton.soundEngine.load(this, R.raw.diceroll, 1)

        val auth: FirebaseAuth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser != null){
            MehenSingleton.login = currentUser.displayName
            MehenSingleton.email = currentUser.email
            MehenSingleton.emailVerified = currentUser.isEmailVerified
            MehenSingleton.userID = currentUser.uid

            FirebaseDatabase.getInstance().getReference("users/${MehenSingleton.userID}/rating")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        MehenSingleton.rating = snapshot.value.toString()
                        if (MehenSingleton.emailVerified == true){
                            if (MehenSingleton.login == null){
                                MehenSingleton.login = ""
                            }
                            this@MainActivity.title = "${MehenSingleton.login} @ ${MehenSingleton.rating}"
                        } else {
                            currentUser.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        MehenSingleton.alertEmailSend.show(MehenSingleton.manager, "mailSend")
                                    }
                                }
                        }
                    }
                })
        } else {
            MehenSingleton.login = ""
            MehenSingleton.email = ""
            MehenSingleton.emailVerified = false
            MehenSingleton.userID = ""
            MehenSingleton.rating = ""
        }

        setTheme(R.style.Theme_Mehen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MehenSingleton.mehenView = findViewById<MehenView>(R.id.mehen_view)
        MehenSingleton.mehenView.mehenDelegate = this

        if (MehenSingleton.networkGame){
            val blackPlayerLinearLayout = findViewById<LinearLayout>(R.id.black_player)
            val whitePlayerLinearLayout = findViewById<LinearLayout>(R.id.white_player)

            val blackPlayerButton: Button = Button(this)
            blackPlayerButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            val whitePlayerButton: Button = Button(this)
            whitePlayerButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            blackPlayerButton.text = MehenSingleton.networkBlackUserName
            blackPlayerButton.gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
            //blackPlayerButton.setPadding(paddingButton, paddingButton, paddingButton, paddingButton)
            blackPlayerButton.setBackgroundColor(Color.parseColor("#FFFFFF"))

            whitePlayerButton.text = MehenSingleton.networkWhiteUserName
            whitePlayerButton.gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
            //whitePlayerButton.setPadding(paddingButton, paddingButton, paddingButton, paddingButton)
            whitePlayerButton.setBackgroundColor(Color.parseColor("#FFFFFF"))

            blackPlayerLinearLayout.addView(blackPlayerButton)
            whitePlayerLinearLayout.addView(whitePlayerButton)

            val reference = FirebaseDatabase.getInstance().getReference("games/${MehenSingleton.gameNamePlaying}")
            reference.child("memoryWhite")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val snapshotValueToString = snapshot.value.toString()
                        if (snapshotValueToString.isNotEmpty() && snapshotValueToString != "null"){
                            MehenSingleton.networkMemoryWhite = snapshotValueToString.toInt()
                            MehenSingleton.mehenView.invalidate()
                        }
                    }
                })
            reference.child("memoryBlack")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val snapshotValueToString = snapshot.value.toString()
                        if (snapshotValueToString.isNotEmpty() && snapshotValueToString != "null"){
                            MehenSingleton.networkMemoryBlack = snapshotValueToString.toInt()
                            MehenSingleton.mehenView.invalidate()
                        }
                    }
                })
            reference.child("canWhiteDiceRoll")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val snapshotValue = snapshot.value
                        if (snapshotValue.toString().isNotEmpty() && snapshotValue.toString() != "null"){
                            MehenSingleton.networkCanWhiteDiceRoll = snapshotValue as Boolean
                            MehenSingleton.mehenView.invalidate()
                        }
                    }
                })
            reference.child("canBlackDiceRoll")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val snapshotValue = snapshot.value
                        if (snapshotValue.toString().isNotEmpty() && snapshotValue.toString() != "null"){
                            MehenSingleton.networkCanBlackDiceRoll = snapshotValue as Boolean
                            MehenSingleton.mehenView.invalidate()
                        }
                    }
                })
            reference.child("canWhiteMove")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val snapshotValue = snapshot.value
                        if (snapshotValue.toString().isNotEmpty() && snapshotValue.toString() != "null"){
                            /*if (MehenSingleton.networkWhiteUserName == MehenSingleton.gameNameSelf){
                                if (MehenSingleton.networkCanWhiteMove == snapshotValue as Boolean){
                                    MehenSingleton.networkCanBlackDiceRoll = true
                                    //networkPush("canWhiteDiceRoll", MehenSingleton.networkCanWhiteDiceRoll)
                                    reference.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onCancelled(error: DatabaseError) {
                                            println(error)
                                        }
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            reference.child("canBlackDiceRoll").setValue(MehenSingleton.networkCanBlackDiceRoll)
                                        }
                                    })
                                }
                            } else {
                                MehenSingleton.networkCanWhiteMove = snapshotValue as Boolean
                            }*/
                            MehenSingleton.networkCanWhiteMove = snapshotValue as Boolean
                            MehenSingleton.mehenView.invalidate()
                        }
                    }
                })
            reference.child("canBlackMove")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val snapshotValue = snapshot.value
                        if (snapshotValue.toString().isNotEmpty() && snapshotValue.toString() != "null"){
                            /*if (MehenSingleton.networkBlackUserName == MehenSingleton.gameNameSelf){
                                if (MehenSingleton.networkCanBlackMove == snapshotValue as Boolean){
                                    MehenSingleton.networkCanWhiteDiceRoll = true
                                    //networkPush("canWhiteDiceRoll", MehenSingleton.networkCanWhiteDiceRoll)
                                    reference.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onCancelled(error: DatabaseError) {
                                            println(error)
                                        }
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            reference.child("canWhiteDiceRoll").setValue(MehenSingleton.networkCanWhiteDiceRoll)
                                        }
                                    })
                                }
                            } else {
                                MehenSingleton.networkCanBlackMove = snapshotValue as Boolean
                            }*/
                            MehenSingleton.networkCanBlackMove = snapshotValue as Boolean
                            MehenSingleton.mehenView.invalidate()
                        }
                    }
                })
            reference.child("whiteValueDiceRoll")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val snapshotValueToString = snapshot.value.toString()
                        if (snapshotValueToString.isNotEmpty() && snapshotValueToString != "null"){
                            MehenSingleton.networkWhiteValueDiceRoll = snapshotValueToString.toInt()
                            MehenSingleton.mehenView.invalidate()
                        }
                    }
                })
            reference.child("blackValueDiceRoll")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val snapshotValueToString = snapshot.value.toString()
                        if (snapshotValueToString.isNotEmpty() && snapshotValueToString != "null"){
                            MehenSingleton.networkBlackValueDiceRoll = snapshotValueToString.toInt()
                            MehenSingleton.mehenView.invalidate()
                        }
                    }
                })
            reference.child("fromColfromRowtoColtoRow")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val fromColFromRowToColToRow = snapshot.value.toString()
                        if (fromColFromRowToColToRow.isNotEmpty() && fromColFromRowToColToRow != "null"){
                            /*if (MehenSingleton.fromColFromRowToColToRow != fromColFromRowToColToRow){
                                MehenSingleton.fromColFromRowToColToRow = fromColFromRowToColToRow
                                val arrayOffromColfromRowtoColtoRow = fromColFromRowToColToRow.split("-").toTypedArray()
                                //println("GOOOOO!!!!")
                                movePiece(
                                    Square(arrayOffromColfromRowtoColtoRow[0].toInt(), arrayOffromColfromRowtoColtoRow[1].toInt()),
                                    Square(arrayOffromColfromRowtoColtoRow[2].toInt(), arrayOffromColfromRowtoColtoRow[3].toInt())
                                )
                                //MehenSingleton.fromColfromRowtoColtoRow = fromColfromRowtoColtoRow
                            }*/
                            /*val arrayOffromColfromRowtoColtoRow = fromColFromRowToColToRow.split("-").toTypedArray()
                            //println("GOOOOO!!!!")
                            movePiece(
                                Square(arrayOffromColfromRowtoColtoRow[0].toInt(), arrayOffromColfromRowtoColtoRow[1].toInt()),
                                Square(arrayOffromColfromRowtoColtoRow[2].toInt(), arrayOffromColfromRowtoColtoRow[3].toInt())
                            )*/
                            if (MehenSingleton.gameNamePlaying != MehenSingleton.gameNameSelf){
                                if (MehenSingleton.networkCanWhiteMove){
                                    val arrayOffromColfromRowtoColtoRow = fromColFromRowToColToRow.split("-").toTypedArray()
                                    movePiece(
                                        Square(arrayOffromColfromRowtoColtoRow[0].toInt(), arrayOffromColfromRowtoColtoRow[1].toInt()),
                                        Square(arrayOffromColfromRowtoColtoRow[2].toInt(), arrayOffromColfromRowtoColtoRow[3].toInt())
                                    )
                                }
                            } else {
                                if (MehenSingleton.networkCanBlackMove){
                                    val arrayOffromColfromRowtoColtoRow = fromColFromRowToColToRow.split("-").toTypedArray()
                                    movePiece(
                                        Square(arrayOffromColfromRowtoColtoRow[0].toInt(), arrayOffromColfromRowtoColtoRow[1].toInt()),
                                        Square(arrayOffromColfromRowtoColtoRow[2].toInt(), arrayOffromColfromRowtoColtoRow[3].toInt())
                                    )
                                }
                            }
                        }
                    }
                })
            /*reference
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //println(snapshot.child("canWhiteDiceRoll").value as Boolean)
                        //snapshot.value.toString().isNotEmpty() && snapshot.value.toString() != "null"
                        MehenSingleton.networkMemoryWhite = snapshot.child("memoryWhite").value.toString().toInt()
                        MehenSingleton.networkMemoryBlack = snapshot.child("memoryBlack").value.toString().toInt()
                        MehenSingleton.networkCanWhiteDiceRoll = snapshot.child("canWhiteDiceRoll").value as Boolean
                        MehenSingleton.networkCanBlackDiceRoll = snapshot.child("canBlackDiceRoll").value as Boolean
                        MehenSingleton.networkCanWhiteMove = snapshot.child("canWhiteMove").value as Boolean
                        MehenSingleton.networkCanBlackMove = snapshot.child("canBlackMove").value as Boolean
                        MehenSingleton.networkWhiteValueDiceRoll = snapshot.child("whiteValueDiceRoll").value.toString().toInt()
                        MehenSingleton.networkBlackValueDiceRoll = snapshot.child("blackValueDiceRoll").value.toString().toInt()

                        val fromColfromRowtoColtoRow = snapshot.child("fromColfromRowtoColtoRow").value.toString()
                        if (MehenSingleton.fromColfromRowtoColtoRow != fromColfromRowtoColtoRow){
                            val arrayOffromColfromRowtoColtoRow = fromColfromRowtoColtoRow.split("-").toTypedArray()
                            movePiece(
                                Square(arrayOffromColfromRowtoColtoRow[0].toInt(), arrayOffromColfromRowtoColtoRow[1].toInt()),
                                Square(arrayOffromColfromRowtoColtoRow[2].toInt(), arrayOffromColfromRowtoColtoRow[3].toInt())
                            )
                            MehenSingleton.fromColfromRowtoColtoRow = snapshot.child("fromColfromRowtoColtoRow").value.toString()
                        }
                    }
                })*/
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.right_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            /*R.id.new_game -> {
                MehenSingleton.selectedItemOfNewGame = 0
                MehenSingleton.alertNewGame.show(MehenSingleton.manager, "newGame")
            }*/
            R.id.account -> {
                startActivity(MehenSingleton.activityLoginIntent)
                finish()
            }
            R.id.new_game_opposite_each_over -> {
                startActivity(MehenSingleton.activityMainIntent)
                finish()
                MehenSingleton.selectedItemOfNewGame = 0
                MehenSingleton.game = true
                MehenSingleton.networkGame = false
                MehenSingleton.robot = false
                MehenSingleton.canRobotMove = false
                MehenGame.reset()
                MehenSingleton.mehenView.invalidate()
            }
            R.id.new_game_online -> {
                MehenSingleton.selectedItemOfNewGame = 1
                startActivity(MehenSingleton.activityNetworkIntent)
                finish()
            }
            R.id.new_game_with_robot -> {
                MehenSingleton.selectedItemOfNewGame = 2
                MehenSingleton.alertRobotGame.show(MehenSingleton.manager, "newRobotGame")
            }
            R.id.settings ->{
                println("settings")
                MehenSingleton.clientSocket.Connection(MehenSingleton.HOST, MehenSingleton.PORT)
                var connect = false
                Thread {
                    try {
                        MehenSingleton.clientSocket.openConnection()
                        // ?????????????????????????????? ???????????? ?? UI ????????????
                        runOnUiThread {
                            //mBtnSend.setEnabled(true)
                            //mBtnClose.setEnabled(true)
                        }
                        println("???????????????????? ??????????????????????")
                        connect = true

                        println("???????????????? ??????????????????")
                        Thread {
                            try {
                                var text: String
                                text = "{\"url\": \"http://yandex.ru\"}"
                                if (text.trim { it <= ' ' }.isEmpty()) text = "{\"url\": \"http://yandex.ru\"}"
                                // ???????????????????? ??????????????????
                                MehenSingleton.clientSocket.sendData(text.toByteArray())

                                MehenSingleton.clientSocket.closeConnection()
                                // ???????????????????????? ????????????
                                //mBtnSend .setEnabled(false)
                                //mBtnClose.setEnabled(false)
                                println("???????????????????? ??????????????")
                            } catch (e: Exception) {
                                println(e.message!!)
                            }
                        }.start()

                    } catch (e: Exception) {
                        println(e.message!!)
                        //println("???????????????????? ???? ??????????????????????")
                        //MehenSingleton.clientSocket = null
                        connect = false
                    }
                }.start()

                /*if (!connect) {
                    println("???????????????????? ???? ??????????????????????")
                } else {
                    println("???????????????? ??????????????????")
                    Thread {
                        try {
                            var text: String
                            text = "{\"url\": \"http://yandex.ru\"}"
                            if (text.trim { it <= ' ' }.isEmpty()) text = "{\"url\": \"http://yandex.ru\"}"
                            // ???????????????????? ??????????????????
                            MehenSingleton.clientSocket.sendData(text.toByteArray())
                        } catch (e: Exception) {
                            println(e.message!!)
                        }
                    }.start()
                }

                // ???????????????? ????????????????????
                MehenSingleton.clientSocket.closeConnection()
                // ???????????????????????? ????????????
                //mBtnSend .setEnabled(false)
                //mBtnClose.setEnabled(false)
                println("???????????????????? ??????????????")*/
            }
        }
        return true
    }

    /*override fun connection(host: String, port: Int) {

    }
    private fun sendMessageOnServerSocket(socket: Socket) {

        // ???????????????? ??????????????????????
        // ???????????????? ??????????????????????
        mConnect = connection(MehenSingleton.HOST, MehenSingleton.PORT)
        // ???????????????? ???????????? ?? ?????????????????? ????????????
        // ???????????????? ???????????? ?? ?????????????????? ????????????
        Thread {
            try {
                mConnect.openConnection()
                // ?????????????????????????????? ???????????? ?? UI ????????????
                runOnUiThread {
                    mBtnSend.setEnabled(true)
                    mBtnClose.setEnabled(true)
                }
                Log.d(LOG_TAG, "???????????????????? ??????????????????????")
                Log.d(
                    LOG_TAG, "(mConnect != null) = "
                            + (mConnect != null)
                )
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message!!)
                mConnect = null
            }
        }.start()

        if (mConnect == null) {
            Log.d(LOG_TAG, "???????????????????? ???? ??????????????????????")
        } else {
            Log.d(LOG_TAG, "???????????????? ??????????????????")
            Thread {
                try {
                    var text: String
                    text = mEdit.getText().toString()
                    if (text.trim { it <= ' ' }.length == 0) text = "Test message"
                    // ???????????????????? ??????????????????
                    mConnect.sendData(text.toByteArray())
                } catch (e: Exception) {
                    Log.e(LOG_TAG, e.message!!)
                }
            }.start()
        }

        // ???????????????? ????????????????????
        mConnect.closeConnection()
        // ???????????????????????? ????????????
        mBtnSend .setEnabled(false)
        mBtnClose.setEnabled(false)
        Log.d(LOG_TAG, "???????????????????? ??????????????")
    }*/
    /*private fun receiveMove(socket: Socket) {
        val scanner = Scanner(socket.getInputStream())
        printWriter = PrintWriter(socket.getOutputStream(), true)
        while (scanner.hasNextLine()) {
            val move = scanner.nextLine().split(",").map { it.toInt() }
            runOnUiThread {
                MehenGame.movePiece(Square(move[0], move[1]), Square(move[2], move[3]))
                MehenSingleton.mehenView.invalidate()
            }
        }
    }*/

    override fun pieceAt(square: Square): MehenPiece? = MehenGame.pieceAt(square)

    override fun movePiece(from: Square, to: Square) {
        MehenGame.movePiece(from, to)
        MehenSingleton.mehenView.invalidate()

        /*printWriter?.let {
            val moveStr = "${from.col},${from.row},${to.col},${to.row}"
            Executors.newSingleThreadExecutor().execute {
                it.println(moveStr)
            }
        }*/
    }

    override fun findPossibleDots(position: Int, player: Player, mehenman: Mehenman) {
        MehenGame.findPossibleDots(position, player, mehenman)
    }

    override fun isFinish(player: Player): Int{
        return MehenGame.isFinish(player)
    }
}
package com.example.mehen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.ktx.Firebase
import java.io.PrintWriter
import java.net.ConnectException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.*
import java.util.concurrent.Executors

//private const val TAG = "MainActivity"

class MainActivity() : AppCompatActivity(), MehenDelegate {
    //private var printWriter: PrintWriter? = null
    //private val isEmulator = Build.FINGERPRINT.contains("generic")


    override fun onCreate(savedInstanceState: Bundle?) {
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

            println(MehenSingleton.login)
            println(MehenSingleton.email)
            println(MehenSingleton.emailVerified)
            println(MehenSingleton.userID)

            if (MehenSingleton.emailVerified == true){
                if (MehenSingleton.login == null){
                    MehenSingleton.login = ""
                }
                this.title = "mehen @ ${MehenSingleton.login}"
            } else {
                currentUser.sendEmailVerification()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            MehenSingleton.alertEmailSend.show(MehenSingleton.manager, "mailSend")
                        }
                    }
            }
        } else {
            MehenSingleton.login = ""
            MehenSingleton.email = ""
            MehenSingleton.emailVerified = false
            MehenSingleton.userID = ""
        }



        /*//AuthUI.getInstance().signOut(this)
        val user: FirebaseUser? = Firebase.auth.currentUser
        if (user != null) {
            MehenSingleton.login = user.displayName
            MehenSingleton.email = user.email
            MehenSingleton.emailVerified = user.isEmailVerified

            println(MehenSingleton.login)
            println(MehenSingleton.email)
            println(MehenSingleton.emailVerified)

            if (MehenSingleton.emailVerified == true){
                //val intent = Intent(this,MainActivity::class.java); startActivity(intent); finish()
                if (MehenSingleton.login == null){
                    MehenSingleton.login = ""
                }
                this.title = "mehen @ ${MehenSingleton.login}"
            } else {
                user.signInWithEmailAndPassword
            }
        } else {
            MehenSingleton.login = ""
            MehenSingleton.email = ""
            MehenSingleton.emailVerified = false
        }*/




//        this.title = "mehen @ ${MehenSingleton.login}"
        /*FirebaseAuth.AuthStateListener{
            val user = Firebase.auth.currentUser
            if (user != null) {
                MehenSingleton.login = user.displayName
                MehenSingleton.email = user.email
                MehenSingleton.emailVerified = user.isEmailVerified
                this.title = "mehen @ ${MehenSingleton.login}"
            } else {
                this.title = "mehen @ ${MehenSingleton.login}"
            } }.onAuthStateChanged(FirebaseAuth.getInstance())*/
        /*addAuthStateListener ( FirebaseAuth.AuthStateListener{
            val user = Firebase.auth.currentUser
            if (user != null) {
                MehenSingleton.login = user.displayName
                MehenSingleton.email = user.email
                MehenSingleton.emailVerified = user.isEmailVerified
                this.title = "mehen @ ${MehenSingleton.login}"
            } else {
                this.title = "mehen @ ${MehenSingleton.login}"
            }
        })*/




        /*val login = user.displayName
        val email = user.email
        val emailVerified = user.isEmailVerified
        println(login)
        println(email)
        println(emailVerified)
        AuthUI.getInstance().signOut(this)
        println(login)
        println(email)
        println(emailVerified)*/




        setTheme(R.style.Theme_Mehen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MehenSingleton.mehenView = findViewById<MehenView>(R.id.mehen_view)
        MehenSingleton.mehenView.mehenDelegate = this

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.right_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.new_game -> {
                MehenSingleton.selectedItemOfNewGame = 0
                MehenSingleton.alertNewGame.show(MehenSingleton.manager, "newGame")
            }
            R.id.account -> {
                startActivity(MehenSingleton.activityLoginIntent)
            }
        }
        return true
    }

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
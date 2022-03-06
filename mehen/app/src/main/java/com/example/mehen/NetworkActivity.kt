package com.example.mehen

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.inflate
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import java.util.*
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.widget.Toast
import androidx.core.view.marginBottom
import com.firebase.ui.auth.data.model.User
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NetworkActivity: AppCompatActivity() {
    private val paddingButton = 24
    private lateinit var scrollNetworkLayout: LinearLayout
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Mehen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)


        this.title = "${MehenSingleton.login} @ ${MehenSingleton.rating}"
        MehenSingleton.gameNameSelf = MehenSingleton.login + " @ " + MehenSingleton.rating

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.mehen_bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        scrollNetworkLayout = findViewById<LinearLayout>(R.id.scroll_network)

        MehenFirebaseDataBaseGames.showElements(
            {dataSnapshot ->
                //ниже прописать, что нужно сделать с интерфейсом при отображении элементов
                showElements(dataSnapshot)},
            {callError ->
                //ниже прописать, как визуально отображать ошибку при чтении базы данных игр...
                println(callError)})
    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.random_game -> {
                println("randomGame")
                return@OnNavigationItemSelectedListener true
            }
            R.id.remove_game -> {
                println("removeGame")
                removeGame(MehenSingleton.gameNameSelf)
                return@OnNavigationItemSelectedListener true
            }
            R.id.add_game -> {
                //println("addGame")
                /*var boardString = ""
                MehenSingleton.networkBoardMap.forEach { it ->
                    boardString += it.key[0].toString() + it.key[1].toString() + it.value
                }
                val obj = MehenFirebaseDataBaseGameObject(
                    0,
                    0,
                    false,
                    false,
                    false,
                    false,
                    5,
                    5,
                    boardString,
                    "",
                )*/
                val obj = MehenFirebaseDataBaseGameObject(
                    0,
                    0,
                    false,
                    false,
                    false,
                    false,
                    5,
                    5,
                    "",
                    "",
                )
                MehenFirebaseDataBaseGames.addElement(obj, MehenSingleton.gameNameSelf) { e ->
                    //ниже прописать, как обрабатывать визуально ошибку при добавлении данных - отображать в окно, нпрмр
                    println(e)
                }
                val reference = FirebaseDatabase.getInstance().getReference("games/${MehenSingleton.gameNameSelf}")
                reference.child("blackUserName")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@NetworkActivity,
                                error.message,
                                Toast.LENGTH_LONG).show()
                        }
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.value.toString().isNotEmpty() && snapshot.value.toString() != "null"){
                                println(snapshot)
                                MehenSingleton.networkGame = true
                                MehenSingleton.networkBlackUserName = snapshot.value.toString()
                                MehenSingleton.networkWhiteUserName = MehenSingleton.gameNameSelf
                                MehenSingleton.gameNamePlaying = MehenSingleton.gameNameSelf
                                startActivity(MehenSingleton.activityMainIntent)
                                finish()
                            }
                        }
                    })
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun removeGame(gameName:String){
        MehenFirebaseDataBaseGames.removeList(gameName) {
            //ниже прописать, что нужно сделать с интерфейсом при удалении игры -- очистить окно, нпрмр
            println(gameName)
        }
    }

    private fun showElements(dataSnapshot: DataSnapshot){
        scrollNetworkLayout.removeAllViews()

        val games = dataSnapshot.children


        games.forEach{
            val gameButton: Button = Button(this)
            gameButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            val drawable: Drawable? = if (it.key.toString() != MehenSingleton.gameNameSelf){
                if (it.child("blackUserName").value.toString() == ""){
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_person_add_24_green)
                } else {
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_live_tv_24)
                }
            } else {
                if (it.child("blackUserName").value.toString() == ""){
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_person_add_disabled_24_green)
                } else {
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_live_tv_24)
                }
            }
            gameButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)

            gameButton.text = "${it.key.toString()}"
            gameButton.gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
            gameButton.setPadding(paddingButton, paddingButton, paddingButton, paddingButton)
            gameButton.setBackgroundColor(Color.parseColor("#FFFFFF"))

            scrollNetworkLayout.addView(gameButton)

            val lineButton: Button = Button(this)
            lineButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)
            scrollNetworkLayout.addView(lineButton)

            gameButton.setOnClickListener {
                if (gameButton.text != MehenSingleton.gameNameSelf){
                    val reference = FirebaseDatabase.getInstance().getReference("games/${gameButton.text}")
                    reference.child("blackUserName").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@NetworkActivity,
                                    error.message,
                                    Toast.LENGTH_LONG).show()
                            }
                            override fun onDataChange(snapshot: DataSnapshot) {
                                reference.child("blackUserName").setValue(MehenSingleton.gameNameSelf)
                                reference.child("canWhiteDiceRoll").setValue(true)
                                MehenSingleton.networkCanWhiteDiceRoll = true
                                MehenSingleton.networkGame = true
                                MehenSingleton.networkBlackUserName = MehenSingleton.gameNameSelf
                                MehenSingleton.networkWhiteUserName = gameButton.text as String
                                MehenSingleton.gameNamePlaying = MehenSingleton.networkWhiteUserName
                                startActivity(MehenSingleton.activityMainIntent)
                                finish()
                            }
                        })
                } else {
                    removeGame(MehenSingleton.gameNameSelf)
                }
            }
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
            }
        }
        return true
    }
}


/**class MainActivity : AppCompatActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContentView(R.network_game_layout.xml.activity_main)
val navView: BottomNavigationView = findViewById(R.id.nav_view)
val navController = findNavController(R.id.nav_host_fragment)
// Passing each menu ID as a set of Ids because each
// menu should be considered as top level destinations.
val appBarConfiguration = AppBarConfiguration(setOf(
R.id.navigation_home,
R.id.navigation_dashboard,
R.id.navigation_notifications,
R.id.navigation_fourth,
R.id.navigation_fifth))
setupActionBarWithNavController(navController, appBarConfiguration)
navView.setupWithNavController(navController)
}
}*/
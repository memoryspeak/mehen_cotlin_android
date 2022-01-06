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
        MehenSingleton.gameName = MehenSingleton.login + " @ " + MehenSingleton.rating

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
                removeGame(MehenSingleton.gameName)
                return@OnNavigationItemSelectedListener true
            }
            R.id.add_game -> {
                //println("addGame")
                var boardString = ""
                MehenSingleton.networkBoard.forEach { it ->
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
                )
                MehenFirebaseDataBaseGames.addElement(obj, MehenSingleton.gameName) { e ->
                    //ниже прописать, как обрабатывать визуально ошибку при добавлении данных - отображать в окно, нпрмр
                    println(e)
                }
                val reference = FirebaseDatabase.getInstance().getReference("games/${MehenSingleton.gameName}")
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
            val newButton: Button = Button(this)
            newButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            val drawable: Drawable? = if (it.key.toString() != MehenSingleton.gameName){
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
            newButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)

            newButton.text = "${it.key.toString()}"
            newButton.gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
            newButton.setPadding(paddingButton, paddingButton, paddingButton, paddingButton)
            newButton.setBackgroundColor(Color.parseColor("#FFFFFF"))

            scrollNetworkLayout.addView(newButton)

            val lineButton: Button = Button(this)
            lineButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)
            scrollNetworkLayout.addView(lineButton)

            newButton.setOnClickListener {
                if (newButton.text != MehenSingleton.gameName){
                    val reference = FirebaseDatabase.getInstance().getReference("games/${newButton.text}")
                    reference.child("blackUserName").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@NetworkActivity,
                                    error.message,
                                    Toast.LENGTH_LONG).show()
                            }
                            override fun onDataChange(snapshot: DataSnapshot) {
                                reference.child("blackUserName").setValue(MehenSingleton.gameName)
                                reference.child("canWhiteDiceRoll").setValue(true)
                                MehenSingleton.networkCanWhiteDiceRoll = true
                                MehenSingleton.networkGame = true
                                startActivity(MehenSingleton.activityMainIntent)
                                finish()
                            }
                        })
                } else {
                    removeGame(MehenSingleton.gameName)
                }
            }
        }
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
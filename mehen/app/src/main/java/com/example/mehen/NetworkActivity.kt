package com.example.mehen

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import java.util.*
import android.widget.LinearLayout




class NetworkActivity: AppCompatActivity() {
    private lateinit var scrollNetworkLayout: LinearLayout
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Mehen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

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
                MehenSingleton.userID?.let { it1 -> MehenFirebaseDataBaseGames.removeList(it1) {
                    //ниже прописать, что нужно сделать с интерфейсом при удалении игры -- очистить окно, нпрмр
                    println(it1)
                }
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.add_game -> {
                println("addGame")
                val obj = MehenFirebaseDataBaseGameObject(
                    MehenSingleton.login,
                    Date(System.currentTimeMillis()),
                    "")
                MehenSingleton.userID?.let { it1 -> MehenFirebaseDataBaseGames.addElement(obj, it1) { e ->
                    //ниже прописать, как обрабатывать визуально ошибку при добавлении данных - отображать в окно, нпрмр
                    println(e)
                }
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    private fun showElements(dataSnapshot: DataSnapshot){
        for (i in 1..100){
            val textViewGameElement: TextView = TextView(this)
            textViewGameElement.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            textViewGameElement.text = "New TextView+$i"

            scrollNetworkLayout.addView(textViewGameElement)
        }
        /*al textViewGameElement: TextView = TextView(this)
        textViewGameElement.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textViewGameElement.text = "New TextView"

        scrollNetworkLayout.addView(textViewGameElement)*/
    }
}

/**class MainActivity : AppCompatActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContentView(R.layout.activity_main)
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
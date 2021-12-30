package com.example.mehen

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class NetworkActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Mehen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.mehen_bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        MehenFirebaseDataBaseGames.showElements(
            {dataSnapshot ->
                //ниже прописать, что нужно сделать с интерфейсом при отображении элементов
                println(dataSnapshot)},
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
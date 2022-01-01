package com.example.mehen

import android.graphics.Color
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
import androidx.core.view.marginBottom
import com.firebase.ui.auth.data.model.User


class NetworkActivity: AppCompatActivity() {
    private val paddingButton = 24
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
        scrollNetworkLayout.removeAllViews()

        val games = dataSnapshot.children


        games.forEach{
            /*val linearLayout: LinearLayout = LinearLayout(this)
            val textViewGameElement: TextView = TextView(this)
            val joinButton: Button = Button(this)
            linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            textViewGameElement.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            joinButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            textViewGameElement.text = "New TextView+$i"
            //textViewGameElement.gravity = LinearLayout.TEXT_ALIGNMENT_VIEW_START
            //textViewGameElement.textSize = 30f
            //textViewGameElement.setBackgroundColor(Color.parseColor("#FFBC8F8F"))
            //textViewGameElement.setPadding(20, 20, 20, 20)
            //textViewGameElement.shadowRadius

            joinButton.text = "Join"
            //joinButton.gravity = LinearLayout.TEXT_ALIGNMENT_VIEW_END
            //joinButton.textSize = 30f

            linearLayout.addView(textViewGameElement)
            linearLayout.addView(joinButton)

            val newLinearLayout = LayoutInflater.from(this).inflate(R.layout.network_game_layout, scrollNetworkLayout, false)
            //scrollNetworkLayout.addView(linearLayout)
            //newLinearLayout.text = "New TextView+$i"*/


            val newButton: Button = Button(this)
            newButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            val drawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_person_add_24_green)
            newButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            newButton.text = "${it.key.toString()}"
            newButton.gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
            newButton.setPadding(paddingButton, paddingButton, paddingButton, paddingButton)
            newButton.setBackgroundColor(Color.parseColor("#FFFFFF"))


            scrollNetworkLayout.addView(newButton)

            val lineButton: Button = Button(this)
            lineButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)
            scrollNetworkLayout.addView(lineButton)
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
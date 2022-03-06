package com.example.mehen

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.firebase.ui.auth.util.ExtraConstants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

import com.google.firebase.FirebaseError
import com.google.firebase.database.*
import java.util.*


//import com.google.firebase.quickstart.auth.R

class RegisterActivity : AppCompatActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Mehen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val activityRegisterEditLoginEditText = findViewById<EditText>(R.id.activity_register_edit_login)
        val activityRegisterEditMailEditText = findViewById<EditText>(R.id.activity_register_edit_mail)
        val activityRegisterEditPasswordEditText = findViewById<EditText>(R.id.activity_register_edit_password)
        val activityRegisterSignInButton = findViewById<Button>(R.id.activity_register_sign_in)

        var loginToText: String = ""
        var isLoginValidate: Boolean = false
        
        val db = FirebaseDatabase.getInstance()

        activityRegisterEditLoginEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                loginToText = activityRegisterEditLoginEditText.text.toString().trim { it <= ' ' }
                val firebaseRef = db.getReference("usernames/$loginToText")
                firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@RegisterActivity,
                            error.message,
                            Toast.LENGTH_LONG).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value != null){
                            isLoginValidate = false
                            activityRegisterEditLoginEditText.setTextColor(Color.parseColor("#FF0000"))
                        } else {
                            isLoginValidate = true
                            activityRegisterEditLoginEditText.setTextColor(Color.parseColor("#008000"))
                        }
                    }
                })
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        activityRegisterSignInButton.setOnClickListener {
            if (isLoginValidate){
                val mailToText = activityRegisterEditMailEditText.text.toString().trim { it <= ' ' }
                val passwordToText = activityRegisterEditPasswordEditText.text.toString().trim { it <= ' ' }

                if (TextUtils.isEmpty(loginToText) || TextUtils.isEmpty(mailToText) || TextUtils.isEmpty(passwordToText)){
                    Toast.makeText(
                        this,
                        "Please enter your details",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    //var fireBaseUser: FirebaseUser? = null
                    FirebaseAuth
                        .getInstance()
                        .createUserWithEmailAndPassword(mailToText, passwordToText)
                        .addOnCompleteListener(OnCompleteListener <AuthResult> { task ->
                            if (task.isSuccessful){
                                val fireBaseUser = task.result!!.user!!
                                val profileUpdates = userProfileChangeRequest { displayName = "$loginToText" }
                                fireBaseUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            db.getReference("users/${fireBaseUser.uid}").setValue(
                                                MehenFirebaseDataBaseUserObject(
                                                    fireBaseUser.email.toString(),
                                                    1500,
                                                    loginToText))
                                            db.getReference("usernames/${loginToText}").setValue(
                                                fireBaseUser.uid.toString())
                                            /*отправить письмо по эл.почте!!!!!*/
                                            fireBaseUser.sendEmailVerification()
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        Toast.makeText(this@RegisterActivity,
                                                            "A confirmation link has been sent to the specified email address.\n" +
                                                                    "Follow the link to complete registration.",
                                                            Toast.LENGTH_LONG).show()
                                                        startActivity(MehenSingleton.activityLoginIntent)
                                                        finish()
                                                    }
                                                }
                                        } else {
                                            Toast.makeText(this@RegisterActivity,
                                                task.exception!!.message.toString(),
                                                Toast.LENGTH_LONG).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_LONG).show()
                            }
                        })

                    //println(fireBaseUser?.uid)
                    //println(fireBaseUser?.displayName)
                    //println(fireBaseUser?.email)
                    //val reference = db.getReference("users/$mehenGameLogin")
                    
                    
                    
                    /*val firebaseRef = FirebaseDatabase.getInstance().reference
                    firebaseRef.child("usernames").child(loginToText).runTransaction(
                        object : Transaction.Handler{
                            override fun doTransaction(currentData: MutableData): Transaction.Result {
                                if (currentData.value == null) {
                                    println(currentData)
                                    var fireBaseUser: FirebaseUser? = null
                                    FirebaseAuth
                                        .getInstance()
                                        .createUserWithEmailAndPassword(mailToText, passwordToText)
                                        .addOnCompleteListener(OnCompleteListener <AuthResult> { task ->
                                            if (task.isSuccessful){
                                                fireBaseUser = task.result!!.user!!
                                            } else {
                                                Toast.makeText(
                                                    this@RegisterActivity,
                                                    task.exception!!.message.toString(),
                                                    Toast.LENGTH_LONG).show()
                                            }
                                        })
                                    val profileUpdates = userProfileChangeRequest { displayName = "$loginToText" }
                                    fireBaseUser?.updateProfile(profileUpdates)
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                /*отправить письмо по эл.почте!!!!!*/
                                                fireBaseUser?.sendEmailVerification()!!
                                                    .addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            Toast.makeText(this@RegisterActivity,
                                                                "A confirmation link has been sent to the specified email address.\n" +
                                                                    "Follow the link to complete registration.",
                                                                Toast.LENGTH_LONG).show()
                                                        }
                                                    }
                                            } else {
                                                Toast.makeText(this@RegisterActivity,
                                                    task.exception!!.message.toString(),
                                                    Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    currentData.value = fireBaseUser?.uid
                                    return Transaction.success(currentData)
                                }
                                return Transaction.abort()
                            }
                            override fun onComplete(
                                error: DatabaseError?,
                                committed: Boolean,
                                currentData: DataSnapshot?
                            ) {
                                if (committed) {
                                    println(currentData)
                                    startActivity(MehenSingleton.activityLoginIntent)
                                    finish()
                                } else {
                                    println(error)
                                }
                            }

                        }
                    )*/


                    /*FirebaseAuth
                        .getInstance()
                        .createUserWithEmailAndPassword(mailToText, passwordToText)
                        .addOnCompleteListener(
                            OnCompleteListener <AuthResult> { task ->
                                if (task.isSuccessful){
                                    val fireBaseUser: FirebaseUser = task.result!!.user!!
                                    val profileUpdates = userProfileChangeRequest { displayName = "$loginToText" }




                                    val fireBase : FirebaseDatabase by lazy {
                                        val db = FirebaseDatabase.getInstance()
                                        db.setPersistenceEnabled(true)
                                        db
                                    }
                                    val firebaseRef = fireBase.reference
                                    firebaseRef.child("usernames").child(loginToText).runTransaction(object:
                                        Transaction.Handler {
                                        override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                            if (mutableData.value == null) {
                                                mutableData.value = fireBaseUser.uid
                                                return Transaction.success(mutableData)
                                            }
                                            return Transaction.abort()
                                        }

                                        override fun onComplete(
                                            error: DatabaseError?,
                                            committed: Boolean,
                                            currentData: DataSnapshot?
                                        ) {
                                            if (committed) {
                                                fireBaseUser.updateProfile(profileUpdates)
                                                    .addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            /*отправить письмо по эл.почте!!!!!*/
                                                            fireBaseUser.sendEmailVerification()
                                                                .addOnCompleteListener { task ->
                                                                    if (task.isSuccessful) {
                                                                        /*Toast.makeText(
                                                                            this,
                                                                            "A confirmation link has been sent to the specified email address.\n" +
                                                                                    "Follow the link to complete registration.",
                                                                            Toast.LENGTH_LONG
                                                                        ).show()*/
                                                                    }
                                                                }
                                                        } else {
                                                            /*Toast.makeText(
                                                                this,
                                                                task.exception!!.message.toString(),
                                                                Toast.LENGTH_LONG
                                                            ).show()*/
                                                        }
                                                    }
                                            } else {
                                                println("some trouble")
                                                // username exists
                                            }
                                        }


                                        /*fireBaseUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                /*отправить письмо по эл.почте!!!!!*/
                                                fireBaseUser.sendEmailVerification()
                                                    .addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            //println(task)
                                                            //MehenSingleton.alertEmailSend.show(MehenSingleton.manager, "firebaseMailSend")
                                                            Toast.makeText(
                                                                this,
                                                                "A confirmation link has been sent to the specified email address.\n" +
                                                                        "Follow the link to complete registration.",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                    }
                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    task.exception!!.message.toString(),
                                                    Toast.LENGTH_LONG).show()
                                            }
                                        }*/
                                    })
                                    //startActivity(MehenSingleton.activityLoginIntent)
                                    //finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_LONG).show()
                                }
                            }
                        )*/
                }
            } else {
                Toast.makeText(
                    this,
                    "Username is taken. Please enter another name",
                    Toast.LENGTH_LONG
                ).show()
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

/*class RegisterActivity : AppCompatActivity () {
    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    // [END auth_fui_create_launcher]
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Mehen)
        super.onCreate(savedInstanceState)
        setContentView(R.network_game_layout.xml.activity_register)
    }
    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.PhoneBuilder().build(),
//            AuthUI.IdpConfig.GoogleBuilder().build(),
//            AuthUI.IdpConfig.FacebookBuilder().build(),
//            AuthUI.IdpConfig.TwitterBuilder().build(),
        )
        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_create_intent]
    }
    // [START auth_fui_result]
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
    // [END auth_fui_result]
    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_signout]
    }
    private fun delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_delete]
    }
    private fun themeAndLogo() {
        val providers = emptyList<AuthUI.IdpConfig>()
        // [START auth_fui_theme_logo]
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            //.setLogo(R.drawable.logo) // Set logo drawable
            //.setTheme(R.style.MySuperAppTheme) // Set theme
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_theme_logo]
    }
    private fun privacyAndTerms() {
        val providers = emptyList<AuthUI.IdpConfig>()
        // [START auth_fui_pp_tos]
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTosAndPrivacyPolicyUrls(
                "https://example.com/terms.html",
                "https://example.com/privacy.html")
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_pp_tos]
    }
    open fun emailLink() {
        // [START auth_fui_email_link]
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName( /* yourPackageName= */
                "...",  /* installIfNotAvailable= */
                true,  /* minimumVersion= */
                null)
            .setHandleCodeInApp(true) // This must be set to true
            .setUrl("https://google.com") // This URL needs to be whitelisted
            .build()
        val providers = listOf(
            EmailBuilder()
                .enableEmailLinkSignIn()
                .setActionCodeSettings(actionCodeSettings)
                .build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_email_link]
    }
    open fun catchEmailLink() {
        val providers: List<IdpConfig> = emptyList()
        // [START auth_fui_email_link_catch]
        if (AuthUI.canHandleIntent(intent)) {
            val extras = intent.extras ?: return
            val link = extras.getString(ExtraConstants.EMAIL_LINK_SIGN_IN)
            if (link != null) {
                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setEmailLink(link)
                    .setAvailableProviders(providers)
                    .build()
                signInLauncher.launch(signInIntent)
            }
        }
        // [END auth_fui_email_link_catch]
    }
}*/
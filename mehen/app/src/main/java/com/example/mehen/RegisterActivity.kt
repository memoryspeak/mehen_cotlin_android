package com.example.mehen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        activityRegisterSignInButton.setOnClickListener {
            val loginToText = activityRegisterEditLoginEditText.text.toString().trim { it <= ' ' }
            val mailToText = activityRegisterEditMailEditText.text.toString().trim { it <= ' ' }
            val passwordToText = activityRegisterEditPasswordEditText.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(loginToText) || TextUtils.isEmpty(mailToText) || TextUtils.isEmpty(passwordToText)){
                Toast.makeText(
                    this,
                    "Please enter your details",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                FirebaseAuth
                    .getInstance()
                    .createUserWithEmailAndPassword(mailToText, passwordToText)
                    .addOnCompleteListener(
                        OnCompleteListener <AuthResult> { task ->
                            if (task.isSuccessful){
                                val fireBaseUser: FirebaseUser = task.result!!.user!!
                                val profileUpdates = userProfileChangeRequest { displayName = "$loginToText" }

                                fireBaseUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            /*отправить письмо по эл.почте!!!!!*/
                                            fireBaseUser.sendEmailVerification()
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        MehenSingleton.alertEmailSend.show(MehenSingleton.manager, "mailSend")
                                                    }
                                                }
                                        } else {
                                            Toast.makeText(
                                                this,
                                                task.exception!!.message.toString(),
                                                Toast.LENGTH_LONG).show()
                                        }
                                    }
                                startActivity(MehenSingleton.activityLoginIntent)
                                //finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    )
            }
        }

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
        setContentView(R.layout.activity_register)
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
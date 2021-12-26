package com.example.mehen

import android.app.Dialog
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import com.firebase.ui.auth.AuthUI.getApplicationContext

class MehenDialogFragment(
    private val title: String,
    private val message: String,
    private val icon: Int,
    private val textButton: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setPositiveButton(textButton) {
                        dialog, id ->  dialog.cancel()
                    //dialog, id ->  startActivity(MehenSingleton.mehenIntent)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
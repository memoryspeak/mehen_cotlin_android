package com.example.mehen

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MehenGameSelectionDialogFragment(private val robotValue: Boolean): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val typeOfGame = arrayOf("Opposite Each Over", "Network Play", "With Robot")
        val typeOfRobotGame = arrayOf("Easy", "Medium", "Hard")
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            if (robotValue){
                builder.setTitle("Robot Select")
                builder.setSingleChoiceItems(typeOfRobotGame, 0){dialog, item: Int ->
                    MehenSingleton.robotIQ = item
                }
                builder.setPositiveButton("OK") { dialog, id ->
                    dialog.cancel()
                    MehenSingleton.game = true
                    MehenSingleton.robot = true
                    MehenSingleton.canRobotMove = false
                    MehenGame.reset()
                    MehenSingleton.mehenView.invalidate()
                    MehenSingleton.serverSocket?.close()
                }
                    .setNegativeButton("Cancel") { dialog, id ->
                        dialog.cancel()
                    }
            } else {
                builder.setTitle("New Game")
                builder.setSingleChoiceItems(typeOfGame, 0){dialog, item: Int ->
                    MehenSingleton.selectedItemOfNewGame = item
                }
                builder.setPositiveButton("OK") { dialog, id ->
                    when (MehenSingleton.selectedItemOfNewGame){
                        0 -> {
                            dialog.cancel()
                            MehenSingleton.game = true
                            MehenSingleton.robot = false
                            MehenSingleton.canRobotMove = false
                            MehenGame.reset()
                            MehenSingleton.mehenView.invalidate()
                            MehenSingleton.serverSocket?.close()
//                listenButton.isEnabled = true
                        }
                        1 -> {
                            dialog.cancel()
                        }
                        2 -> {
                            dialog.cancel()
                            MehenSingleton.alertRobotGame.show(MehenSingleton.manager, "newRobotGame")
                        }
                    }
                }
                    .setNegativeButton("Cancel") { dialog, id ->
                        dialog.cancel()
                    }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
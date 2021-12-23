package com.example.mehen

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.io.PrintWriter
import java.net.Socket
import java.util.*
import java.util.concurrent.Executors

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
//                            Executors.newSingleThreadExecutor().execute {
//                                println("socketConnection...")
//                                val socket = Socket("192.168.10.5", 80)
//                                println("...socketConnection")
//                                val scanner = Scanner(socket.getInputStream())
//                                println(scanner)
//                                println(scanner.hasNextLine())
//                                val printWriter = PrintWriter(socket.getOutputStream())
//                                println(printWriter.hashCode())
//                                while (scanner.hasNextLine()) {
//                                    println(scanner.hasNextLine())
//                                }
//
//                            }
                            println("Connection...")

                            val randomIntent = Intent(requireContext(), FirebaseUIActivity::class.java)
                            startActivity(randomIntent)

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
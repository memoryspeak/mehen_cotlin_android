package com.example.mehen

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object MehenFirebaseDataBaseGames {
    /*private val fireBase : FirebaseDatabase by lazy {
        val db = FirebaseDatabase.getInstance()
        db.setPersistenceEnabled(true)
        db
    }*/
    private val fireBase = FirebaseDatabase.getInstance()

    fun removeList (mehenGameID: String, function: () -> Unit) {
        val reference = fireBase.getReference("games/$mehenGameID")
        //reference.removeValue({databaseError, databaseReference -> function() })
        reference.removeValue { _, _ -> function() }
    }

    fun addElement (mehenObject: MehenFirebaseDataBaseGameObject, mehenGameName: String, callError: (String) -> Unit) {
        val reference = fireBase.getReference("games/$mehenGameName")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                callError(error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                reference.setValue(mehenObject)
            }
        })
    }
    fun showElements (call: (DataSnapshot) -> Unit, callError: (String) -> Unit) {
        val reference = fireBase.getReference("games")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                callError(error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                call(snapshot)
            }
        })
    }
}
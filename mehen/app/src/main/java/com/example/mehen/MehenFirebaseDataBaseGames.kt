package com.example.mehen

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object MehenFirebaseDataBaseGames {
    private val fireBase : FirebaseDatabase by lazy {
        val db = FirebaseDatabase.getInstance()
        db.setPersistenceEnabled(true)
        db
    }

    fun removeList (function: () -> Unit) {
        val reference = fireBase.getReference("games/1")
        reference.removeValue({datavaseError, datavaseReference -> function() })

    }

    fun addElement (mehenObject: MehenFirebaseDataBaseGameObject, callError: (String) -> Unit) {
        val reference = fireBase.getReference("games")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                callError(p0.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                reference.push().setValue(mehenObject)
            }
        })
    }
}
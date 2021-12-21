package com.example.mehen

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
const val DATABASE_NAME = "ML_MEHEN_DATABASE"
const val TABLE_NAME = "VALUES"
const val COL_ID = "ID"
const val COL_GAME_SET = "GAME_SET"
const val COL_WHOSE_MOVE = "WHOSE_MOVE"
const val COL_GAME_RESULT = "GAME_RESULT"

class DataBaseHandler (var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME +
                " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_GAME_SET + " INTEGER" +
                COL_WHOSE_MOVE + " INTEGER" +
                COL_GAME_RESULT + " INTEGER)"
        db?.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //onCreate(db);
    }
//    fun insertData(user: User) {
//        val database = this.writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put(COL_NAME, user.name)
//        contentValues.put(COL_AGE, user.age)
//        val result = database.insert(TABLENAME, null, contentValues)
//        if (result == (0).toLong()) {
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
//        }
//        else {
//            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
//        }
//    }
//    fun readData(): MutableList<User> {
//        val list: MutableList<User> = ArrayList()
//        val db = this.readableDatabase
//        val query = "Select * from $TABLENAME"
//        val result = db.rawQuery(query, null)
//        if (result.moveToFirst()) {
//            do {
//                val user = User()
//                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
//                user.name = result.getString(result.getColumnIndex(COL_NAME))
//                user.age = result.getString(result.getColumnIndex(COL_AGE)).toInt()
//                list.add(user)
//            }
//            while (result.moveToNext())
//        }
//        return list
//    }
}
package com.abx.cellsystem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, Constants.DATABASE_NAME,
    null, Constants.DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE ${Constants.ENTITY_CELULAR}" +
                "(${Constants.PROPERTY_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Constants.PROPERTY_NROIMEI} VARCHAR(15), " +
                "${Constants.PROPERTY_NROCEL} VARCHAR(9))"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun getAllCelular(): MutableList<Celular> {
        val celulares: MutableList<Celular> = mutableListOf()

        val database = this.readableDatabase
        val query = "SELECT * FROM ${Constants.ENTITY_CELULAR}"
        val result = database.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val celular = Celular()
                celular.id = result.getLong(result.getColumnIndex(Constants.PROPERTY_ID))
                celular.nroImei =
                    result.getString(result.getColumnIndex(Constants.PROPERTY_NROIMEI))
                celular.nroCelular =
                    result.getString(result.getColumnIndex(Constants.PROPERTY_NROCEL))

                celulares.add(celular)

            } while (result.moveToNext())
        }
        return celulares
    }

    fun insertCelular(celular: Celular): Long {
        val database = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(Constants.PROPERTY_NROIMEI, celular.nroImei)
            put(Constants.PROPERTY_NROCEL, celular.nroCelular)

        }
        val resultId = database.insert(Constants.ENTITY_CELULAR, null, contentValues)
        return resultId
    }

    fun updateCelular(celular: Celular): Boolean {
        val database = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(Constants.PROPERTY_NROIMEI, celular.nroImei)
            put(Constants.PROPERTY_NROCEL, celular.nroCelular)

        }
        val result = database.update(
            Constants.ENTITY_CELULAR,
            contentValues,
            "${Constants.PROPERTY_ID}=${celular.id}", null
        )
        return result == Constants.TRUE
    }

    fun deleteCelular(celular: Celular): Boolean {
        val database = this.writableDatabase
        val result = database.delete(
            Constants.ENTITY_CELULAR,
            "${Constants.PROPERTY_ID}=${celular.id}",
            null
        )

        return result == Constants.TRUE
    }
}
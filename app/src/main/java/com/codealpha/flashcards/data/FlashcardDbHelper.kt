package com.codealpha.flashcards.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Lightweight local storage for the deck. Plain SQLite is used instead of a
 * heavier persistence library so the whole data layer is easy to read and
 * has as few moving parts as possible.
 */
class FlashcardDbHelper(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "flashcards.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "flashcards"
        const val COL_ID = "id"
        const val COL_QUESTION = "question"
        const val COL_ANSWER = "answer"
        const val COL_POSITION = "position"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_QUESTION TEXT NOT NULL,
                $COL_ANSWER TEXT NOT NULL,
                $COL_POSITION INTEGER NOT NULL
            )
            """.trimIndent()
        )
        // Seed a starter deck so first-time users have cards to study right away.
        seedFlashcards.forEachIndexed { index, card ->
            val values = ContentValues().apply {
                put(COL_QUESTION, card.question)
                put(COL_ANSWER, card.answer)
                put(COL_POSITION, index)
            }
            db.insert(TABLE_NAME, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}

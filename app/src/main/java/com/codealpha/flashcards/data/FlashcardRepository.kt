package com.codealpha.flashcards.data

import android.content.ContentValues
import android.content.Context
import android.util.Log

/**
 * All reads/writes go through here and are wrapped defensively: if something
 * ever goes wrong with the on-device database, we log it and return a safe
 * fallback instead of letting an exception crash the app.
 */
class FlashcardRepository(context: Context) {

    private val dbHelper = FlashcardDbHelper(context)
    private val tag = "FlashcardRepository"

    fun getAllCards(): List<Flashcard> {
        return try {
            val db = dbHelper.readableDatabase
            val cards = mutableListOf<Flashcard>()
            db.query(
                FlashcardDbHelper.TABLE_NAME,
                null, null, null, null, null,
                "${FlashcardDbHelper.COL_POSITION} ASC"
            ).use { cursor ->
                val idIdx = cursor.getColumnIndexOrThrow(FlashcardDbHelper.COL_ID)
                val qIdx = cursor.getColumnIndexOrThrow(FlashcardDbHelper.COL_QUESTION)
                val aIdx = cursor.getColumnIndexOrThrow(FlashcardDbHelper.COL_ANSWER)
                while (cursor.moveToNext()) {
                    cards.add(
                        Flashcard(
                            id = cursor.getLong(idIdx),
                            question = cursor.getString(qIdx),
                            answer = cursor.getString(aIdx)
                        )
                    )
                }
            }
            cards
        } catch (e: Exception) {
            Log.e(tag, "Failed to load flashcards", e)
            emptyList()
        }
    }

    fun addCard(question: String, answer: String): Boolean {
        return try {
            val db = dbHelper.writableDatabase
            val nextPosition = getAllCards().size
            val values = ContentValues().apply {
                put(FlashcardDbHelper.COL_QUESTION, question)
                put(FlashcardDbHelper.COL_ANSWER, answer)
                put(FlashcardDbHelper.COL_POSITION, nextPosition)
            }
            db.insert(FlashcardDbHelper.TABLE_NAME, null, values) != -1L
        } catch (e: Exception) {
            Log.e(tag, "Failed to add flashcard", e)
            false
        }
    }

    fun updateCard(id: Long, question: String, answer: String): Boolean {
        return try {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(FlashcardDbHelper.COL_QUESTION, question)
                put(FlashcardDbHelper.COL_ANSWER, answer)
            }
            db.update(
                FlashcardDbHelper.TABLE_NAME,
                values,
                "${FlashcardDbHelper.COL_ID} = ?",
                arrayOf(id.toString())
            ) > 0
        } catch (e: Exception) {
            Log.e(tag, "Failed to update flashcard", e)
            false
        }
    }

    fun deleteCard(id: Long): Boolean {
        return try {
            val db = dbHelper.writableDatabase
            db.delete(
                FlashcardDbHelper.TABLE_NAME,
                "${FlashcardDbHelper.COL_ID} = ?",
                arrayOf(id.toString())
            ) > 0
        } catch (e: Exception) {
            Log.e(tag, "Failed to delete flashcard", e)
            false
        }
    }
}

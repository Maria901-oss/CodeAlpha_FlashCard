package com.codealpha.flashcards.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.codealpha.flashcards.data.Flashcard
import com.codealpha.flashcards.data.FlashcardRepository

class FlashcardViewModel(private val repository: FlashcardRepository) : ViewModel() {

    var cards by mutableStateOf<List<Flashcard>>(emptyList())
        private set

    var currentIndex by mutableStateOf(0)
        private set

    var isFlipped by mutableStateOf(false)
        private set

    var toastMessage by mutableStateOf<String?>(null)
        private set

    init {
        refresh()
    }

    val currentCard: Flashcard?
        get() = cards.getOrNull(currentIndex)

    private fun refresh() {
        cards = repository.getAllCards()
        if (currentIndex >= cards.size) {
            currentIndex = if (cards.isEmpty()) 0 else cards.size - 1
        }
    }

    fun flip() {
        if (cards.isEmpty()) return
        isFlipped = !isFlipped
    }

    fun next() {
        if (cards.isEmpty()) return
        currentIndex = (currentIndex + 1) % cards.size
        isFlipped = false
    }

    fun previous() {
        if (cards.isEmpty()) return
        currentIndex = (currentIndex - 1 + cards.size) % cards.size
        isFlipped = false
    }

    fun shuffle() {
        if (cards.size < 2) return
        val currentId = currentCard?.id
        cards = cards.shuffled()
        val newIndex = cards.indexOfFirst { it.id == currentId }
        currentIndex = if (newIndex >= 0) newIndex else 0
        isFlipped = false
        showToast("Deck shuffled")
    }

    fun addCard(question: String, answer: String) {
        val q = question.trim()
        val a = answer.trim()
        if (q.isEmpty() || a.isEmpty()) {
            showToast("Please fill in both the question and the answer")
            return
        }
        val ok = repository.addCard(q, a)
        if (ok) {
            refresh()
            currentIndex = cards.size - 1
            isFlipped = false
            showToast("Card added")
        } else {
            showToast("Couldn't add the card, please try again")
        }
    }

    fun updateCard(id: Long, question: String, answer: String) {
        val q = question.trim()
        val a = answer.trim()
        if (q.isEmpty() || a.isEmpty()) {
            showToast("Please fill in both the question and the answer")
            return
        }
        val ok = repository.updateCard(id, q, a)
        if (ok) {
            refresh()
            showToast("Card updated")
        } else {
            showToast("Couldn't update the card, please try again")
        }
    }

    fun deleteCard(id: Long) {
        val ok = repository.deleteCard(id)
        if (ok) {
            refresh()
            showToast("Card deleted")
        } else {
            showToast("Couldn't delete the card, please try again")
        }
    }

    fun showToast(message: String) {
        toastMessage = message
    }

    fun consumeToast() {
        toastMessage = null
    }
}

class FlashcardViewModelFactory(
    private val repository: FlashcardRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FlashcardViewModel(repository) as T
    }
}

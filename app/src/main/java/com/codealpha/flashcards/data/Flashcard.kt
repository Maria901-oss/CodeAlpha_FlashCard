package com.codealpha.flashcards.data

data class Flashcard(
    val id: Long = 0,
    val question: String,
    val answer: String
)

/**
 * Starter deck shown the first time the app runs, so a new user never
 * lands on a completely empty screen. Users can edit or delete every one
 * of these, and add as many of their own as they like.
 */
val seedFlashcards = listOf(
    Flashcard(question = "What is the capital of France?", answer = "Paris"),
    Flashcard(question = "What is H2O commonly known as?", answer = "Water"),
    Flashcard(question = "Who wrote 'Romeo and Juliet'?", answer = "William Shakespeare"),
    Flashcard(question = "What is the largest planet in our solar system?", answer = "Jupiter"),
    Flashcard(question = "What is the chemical symbol for gold?", answer = "Au"),
    Flashcard(question = "How many continents are there on Earth?", answer = "Seven"),
    Flashcard(question = "What is the powerhouse of the cell?", answer = "The mitochondria"),
    Flashcard(question = "Who painted the Mona Lisa?", answer = "Leonardo da Vinci"),
    Flashcard(question = "What is the smallest prime number?", answer = "2"),
    Flashcard(question = "What language runs natively in an Android app built with Kotlin?", answer = "Kotlin (compiled to JVM bytecode)"),
    Flashcard(question = "What does 'HTTP' stand for?", answer = "HyperText Transfer Protocol"),
    Flashcard(question = "What is the freezing point of water in Celsius?", answer = "0°C"),
    Flashcard(question = "Who is known as the father of computers?", answer = "Charles Babbage"),
    Flashcard(question = "What is the longest river in the world?", answer = "The Nile"),
    Flashcard(question = "What gas do plants absorb from the atmosphere for photosynthesis?", answer = "Carbon dioxide")
)

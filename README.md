# Flashcard Quiz App

A native Android flashcard application developed using Kotlin and Jetpack Compose. The application allows users to create and manage flashcards and test their knowledge through an interactive quiz interface.

## Features

* Interactive flashcard interface with flip animation.
* Show and hide answers.
* Next and Previous card navigation.
* Progress bar and card counter.
* Add new flashcards.
* Edit existing flashcards.
* Delete flashcards.
* Shuffle flashcards.
* Includes 15 pre-loaded sample flashcards.
* Stores flashcards locally on the device using SQLite.
* Flashcards remain available after closing and reopening the application.
* Handles empty or unavailable data with appropriate empty states.

## Technologies Used

* Kotlin
* Jetpack Compose
* Android Studio
* Android SDK
* SQLite

## Project Structure

```text
app/src/main/java/com/codealpha/flashcards/
├── MainActivity.kt
├── data/
│   ├── Flashcard.kt
│   ├── FlashcardDbHelper.kt
│   └── FlashcardRepository.kt
└── ui/
    ├── FlashcardViewModel.kt
    ├── FlashcardScreen.kt
    └── theme/
```

## Application Overview

The Flashcard Quiz App provides an interactive learning experience where users can review flashcards, reveal answers, navigate between cards, and manage their personal flashcard collection. Local SQLite storage ensures that user-created flashcards remain available between sessions.

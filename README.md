# Study Deck — Flashcard Quiz App (Android)

A native Android flashcard app for CodeAlpha App Development Task 1, built with Kotlin and Jetpack Compose.

## Features
- Flip animation: tap the card, or press **Show Answer**, to reveal the answer
- **Next / Previous** navigation with a progress bar and card counter
- **Add, edit, delete** flashcards from the "Manage cards" sheet
- **Shuffle deck**
- Starts with **15 sample flashcards** pre-loaded, so it's never empty
- All cards saved locally on-device (SQLite) — they persist after closing the app
- All storage operations are wrapped in try/catch, and every screen has a safe empty state, so the app does not crash on missing or bad data

## How to open and run
1. Install **Android Studio** (Koala or newer) if you don't have it.
2. Open Android Studio → **Open** → select this project folder (`FlashcardQuizApp`).
3. Let Gradle sync finish (needs an internet connection the first time, to download dependencies).
4. Click the green **Run ▶** button, with an emulator or a physical device (Android 8.0 / API 26 or higher) selected.

## Project structure
```
app/src/main/java/com/codealpha/flashcards/
├── MainActivity.kt              # app entry point
├── data/
│   ├── Flashcard.kt              # data model + starter deck
│   ├── FlashcardDbHelper.kt      # SQLite table creation
│   └── FlashcardRepository.kt    # CRUD operations
└── ui/
    ├── FlashcardViewModel.kt     # screen state
    ├── FlashcardScreen.kt        # all Compose UI
    └── theme/                    # colors, typography
```

## Submitting to CodeAlpha
1. Create a GitHub repo named `CodeAlpha_FlashcardQuizApp`.
2. Push this whole folder to it.
3. Record a short screen recording of the app running (flip, next/previous, add/edit/delete a card) and post it on LinkedIn tagging @CodeAlpha, with the repo link.
4. Submit through the internship's submission form.

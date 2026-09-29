package com.codealpha.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.codealpha.flashcards.data.FlashcardRepository
import com.codealpha.flashcards.ui.FlashcardScreen
import com.codealpha.flashcards.ui.FlashcardViewModel
import com.codealpha.flashcards.ui.FlashcardViewModelFactory
import com.codealpha.flashcards.ui.theme.Background
import com.codealpha.flashcards.ui.theme.StudyDeckTheme

class MainActivity : ComponentActivity() {

    private val viewModel: FlashcardViewModel by viewModels {
        FlashcardViewModelFactory(FlashcardRepository(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyDeckTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background
                ) {
                    FlashcardScreen(viewModel = viewModel)
                }
            }
        }
    }
}

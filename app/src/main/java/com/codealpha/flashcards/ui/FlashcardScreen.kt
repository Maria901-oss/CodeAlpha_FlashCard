package com.codealpha.flashcards.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codealpha.flashcards.data.Flashcard
import com.codealpha.flashcards.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(viewModel: FlashcardViewModel) {
    var showManageSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.toastMessage) {
        val message = viewModel.toastMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.consumeToast()
        }
    }

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Study Deck", color = White, fontFamily = SerifFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
                        Text(
                            text = "${viewModel.cards.size} ${if (viewModel.cards.size == 1) "card" else "cards"}",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (viewModel.cards.isEmpty()) {
                EmptyState(onAddClick = { showManageSheet = true })
            } else {
                StudyContent(viewModel = viewModel, onManageClick = { showManageSheet = true })
            }
        }
    }

    if (showManageSheet) {
        ManageCardsSheet(
            viewModel = viewModel,
            onDismiss = { showManageSheet = false }
        )
    }
}

@Composable
private fun EmptyState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "No flashcards yet",
            color = White,
            fontFamily = SerifFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Add your first card to start studying.",
            color = TextMuted,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Color(0xFF1B1500))
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("Add a flashcard")
        }
    }
}

@Composable
private fun StudyContent(viewModel: FlashcardViewModel, onManageClick: () -> Unit) {
    val card = viewModel.currentCard ?: return
    val total = viewModel.cards.size
    val progress = (viewModel.currentIndex + 1f) / total

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))

        // Progress row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Card ${viewModel.currentIndex + 1} of $total",
                color = TextMuted,
                fontSize = 13.sp
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .height(3.dp)
                    .background(LineColor, RoundedCornerShape(2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .background(Gold, RoundedCornerShape(2.dp))
                )
            }
            Text("${(progress * 100).toInt()}%", color = TextMuted, fontSize = 13.sp)
        }

        Spacer(Modifier.height(20.dp))

        FlipCard(
            question = card.question,
            answer = card.answer,
            isFlipped = viewModel.isFlipped,
            onClick = { viewModel.flip() }
        )

        Spacer(Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { viewModel.previous() },
                enabled = total > 1,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = White),
                modifier = Modifier.weight(1f)
            ) { Text("← Previous") }

            OutlinedButton(
                onClick = { viewModel.next() },
                enabled = total > 1,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = White),
                modifier = Modifier.weight(1f)
            ) { Text("Next →") }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { viewModel.flip() },
            colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Color(0xFF1B1500)),
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (viewModel.isFlipped) "Show Question" else "Show Answer") }

        Spacer(Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TextButton(onClick = { viewModel.shuffle() }) {
                Text("Shuffle deck", color = TextMuted, fontSize = 13.sp)
            }
            TextButton(onClick = onManageClick) {
                Text("Manage cards", color = TextMuted, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun FlipCard(
    question: String,
    answer: String,
    isFlipped: Boolean,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 420),
        label = "cardFlip"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .shadow(elevation = 14.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(if (rotation <= 90f) CardFront else CardBack)
            .clickable(onClick = onClick)
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        if (rotation <= 90f) {
            CardFace(label = "Question", text = question)
        } else {
            Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                CardFace(label = "Answer", text = answer)
            }
        }
    }
}

@Composable
private fun CardFace(label: String, text: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = label,
            color = InkSoft,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.TopStart)
        )
        Text(
            text = text,
            color = Ink,
            fontFamily = SerifFamily,
            fontSize = 21.sp,
            lineHeight = 28.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageCardsSheet(viewModel: FlashcardViewModel, onDismiss: () -> Unit) {
    var editingCard by remember { mutableStateOf<Flashcard?>(null) }
    var questionField by remember { mutableStateOf("") }
    var answerField by remember { mutableStateOf("") }

    fun resetForm() {
        editingCard = null
        questionField = ""
        answerField = ""
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BackgroundSoft
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                if (editingCard == null) "Add a flashcard" else "Edit flashcard",
                color = White,
                fontFamily = SerifFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = questionField,
                onValueChange = { questionField = it },
                label = { Text("Question (front)") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors()
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = answerField,
                onValueChange = { answerField = it },
                label = { Text("Answer (back)") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors()
            )
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        val editing = editingCard
                        if (editing == null) {
                            viewModel.addCard(questionField, answerField)
                        } else {
                            viewModel.updateCard(editing.id, questionField, answerField)
                        }
                        resetForm()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Color(0xFF1B1500))
                ) {
                    Text(if (editingCard == null) "Save card" else "Update card")
                }
                if (editingCard != null) {
                    OutlinedButton(
                        onClick = { resetForm() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted)
                    ) { Text("Cancel edit") }
                }
            }

            Spacer(Modifier.height(18.dp))
            Divider(color = LineColor)
            Spacer(Modifier.height(12.dp))

            if (viewModel.cards.isEmpty()) {
                Text("Your saved cards will appear here.", color = TextMuted, fontSize = 13.sp)
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 320.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.cards, key = { it.id }) { card ->
                        DeckListItem(
                            card = card,
                            onEdit = {
                                editingCard = card
                                questionField = card.question
                                answerField = card.answer
                            },
                            onDelete = { viewModel.deleteCard(card.id) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = White,
    unfocusedTextColor = White,
    focusedBorderColor = GoldDim,
    unfocusedBorderColor = LineColor,
    focusedLabelColor = TextMuted,
    unfocusedLabelColor = TextMuted,
    cursorColor = Gold
)

@Composable
private fun DeckListItem(card: Flashcard, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                card.question,
                color = White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2
            )
            Text(
                card.answer,
                color = TextMuted,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
        Row {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit card", tint = TextMuted, modifier = Modifier.size(18.dp))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete card", tint = Danger, modifier = Modifier.size(18.dp))
            }
        }
    }
}

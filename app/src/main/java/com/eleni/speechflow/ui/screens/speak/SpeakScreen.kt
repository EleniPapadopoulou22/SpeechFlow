package com.eleni.speechflow.ui.screens.speak

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

private val quickPhrases = listOf(
    "Γεια, μπορείς να με βοηθήσεις;",
    "Δεν μπορώ να ακούσω. Μπορείς να μιλήσεις μέσω της εφαρμογής!",
    "Ευχαριστώ πολύ!",
    "Έκτακτη ανάγκη! Καλέστε..."
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakScreen(
    onAnswerClick: () -> Unit,
    onBack: () -> Unit,
    viewModel: SpeakViewModel = viewModel(factory = SpeakViewModel.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Πληκτρολόγησε") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Πίσω")
                    }
                },
                actions = {
                    if (state.text.isNotEmpty()) {
                        IconButton(onClick = viewModel::clearText) {
                            Icon(Icons.Default.Clear, "Καθαρισμός")
                        }
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(quickPhrases) { phrase ->
                    SuggestionChip(
                        onClick = { viewModel.speakPhrase(phrase) },
                        label = { Text(phrase) },
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = state.text,
                onValueChange = viewModel::onTextChange,
                modifier = Modifier.fillMaxWidth().weight(1f),
                label = { Text("Πληκτρολόγησε εδώ...") },
                placeholder = { Text("Γράψε αυτό που θέλεις να ειπωθεί") },
                shape = MaterialTheme.shapes.medium,
                textStyle = MaterialTheme.typography.bodyLarge,
            )

            Spacer(Modifier.height(32.dp))

            val playColor = if (state.isSpeaking)
                MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.tertiary

            Spacer(Modifier.height(16.dp))
            OutlinedButton(
                onClick = onAnswerClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.AutoMirrored.Filled.Reply, null)
                Spacer(Modifier.width(8.dp))
                Text("Απάντηση;")
            }
            Spacer(Modifier.height(16.dp))

            Surface(
                modifier = Modifier.size(96.dp).clip(CircleShape),
                color = playColor,
                shadowElevation = 6.dp,
                onClick = {
                    if (state.isSpeaking) viewModel.stop()
                    else viewModel.speak()
                },
                enabled = state.isInitialized && state.text.isNotBlank(),
            )
            {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (state.isSpeaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = if (state.isSpeaking) "Διακοπή" else "Εκφώνηση",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onTertiary,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            if (!state.isInitialized) {
                Text(
                    text = "Αρχικοποίηση μηχανής εκφώνησης...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

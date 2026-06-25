package com.eleni.speechflow.ui.screens.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eleni.speechflow.data.local.TranscriptionEntity
import com.eleni.speechflow.data.local.TranscriptionType
import com.eleni.speechflow.ui.components.EmptyState
import com.eleni.speechflow.util.formatTimestamp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    viewModel: HistoryViewModel = viewModel(factory = HistoryViewModel.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDeleteAll by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ιστορικό") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Πίσω")
                    }
                },
                actions = {
                    if (!state.isEmpty) {
                        IconButton(onClick = { showDeleteAll = true }) {
                            Icon(Icons.Default.Delete, "Διαγραφή όλων")
                        }
                    }
                },
            )
        },
    ) { padding ->
        if (state.isEmpty && !state.isLoading) {
            EmptyState(
                icon = Icons.Default.History,
                title = "Καμία μεταγραφή ακόμα",
                description = "Οι μεταγραφές σου θα εμφανιστούν εδώ",
                modifier = Modifier.padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                state.groupedItems.forEach { (date, transcriptions) ->
                    stickyHeader {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Text(
                                text = date,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    items(items = transcriptions, key = { it.id }) { item ->
                        HistoryItem(item = item, onDelete = { viewModel.delete(item) })
                    }
                }
            }
        }
    }

    if (showDeleteAll) {
        AlertDialog(
            onDismissRequest = { showDeleteAll = false },
            title = { Text("Διαγραφή όλων;") },
            text  = { Text("Όλες οι αποθηκευμένες μεταγραφές θα διαγραφούν οριστικά.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAll(); showDeleteAll = false
                }) { Text("Διαγραφή") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAll = false }) { Text("Ακύρωση") }
            },
        )
    }
}

@Composable
private fun HistoryItem(item: TranscriptionEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (item.type == TranscriptionType.LISTEN)
                    Icons.Default.Hearing else Icons.Default.RecordVoiceOver,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.text, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = formatTimestamp(item.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Διαγραφή",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

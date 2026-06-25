package com.eleni.speechflow.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ρυθμίσεις") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Πίσω")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
        ) {
            Text("Γλώσσα αναγνώρισης", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            FilterChipRow(
                options = listOf("el-GR" to "Ελληνικά", "en-US" to "English"),
                selected = state.language,
                onSelect = viewModel::setLanguage,
            )

            Spacer(Modifier.height(32.dp))

            Text("Μέγεθος γραμματοσειράς: ${(state.fontScale * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium)
            Slider(
                value = state.fontScale,
                onValueChange = viewModel::setFontScale,
                valueRange = 0.8f..1.6f,
                steps = 7,
            )
            Text ("Λειτουργία εμφάνισης οθόνης", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            FilterChipRow(
                options = listOf(
                    "light" to "Φωτεινό",
                    "dark" to "Σκούρο",
                    "system" to "Σύστημα"
                ),
                selected = state.theme,
                onSelect = viewModel::setTheme,
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Ταχύτητα εκφώνησης: ${(state.speechRate * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium
            )
            Slider(
                value = state.speechRate,
                onValueChange = viewModel::setSpeechRate,
                valueRange = 0.5f..1.5f,
                steps = 9,
            )

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("Δόνηση", modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium)
                Switch(checked = state.vibration, onCheckedChange = viewModel::setVibration)
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = "SpeechFlow v1.0  Πτυχιακή Εργασία 2026",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun FilterChipRow(
    options: List<Pair<String, String>>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { (value, label) ->
            FilterChip(
                selected = selected == value,
                onClick = { onSelect(value) },
                label = { Text(label) },
            )
        }
    }
}

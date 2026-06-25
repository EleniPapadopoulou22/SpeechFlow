package com.eleni.speechflow.ui.screens.listen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.automirrored.filled.Reply


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListenScreen(
    onAnswerClick: () -> Unit,
    onBack: () -> Unit,
    viewModel: ListenViewModel = viewModel(factory = ListenViewModel.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.setPermission(granted)
        if (granted) viewModel.startListening()
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.setPermission(granted)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ομιλία") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
                    val displayText = when {
                        state.finalText.isNotBlank() -> state.finalText
                        state.partialText.isNotBlank() -> state.partialText
                        state.isListening -> "Ακούω..."
                        state.error != null -> state.error.orEmpty()
                        else -> "Πάτα το μικρόφωνο για να ξεκινήσεις"
                    }
                    val color = when {
                        state.error != null -> MaterialTheme.colorScheme.error
                        state.finalText.isBlank() && state.partialText.isBlank() ->
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                    Text(
                        text = displayText,
                        style = MaterialTheme.typography.headlineMedium,
                        color = color,
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            val micColor = if (state.isListening)
                MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.tertiary

            Surface(
                modifier = Modifier.size(96.dp).clip(CircleShape),
                color = micColor,
                shadowElevation = 6.dp,
                onClick = {
                    if (state.isListening) viewModel.stopListening()
                    else if (!state.hasPermission)
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    else viewModel.startListening()
                },
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (state.isListening) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = if (state.isListening) "Διακοπή" else "Έναρξη",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onTertiary,
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            if (state.finalText.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onAnswerClick,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.AutoMirrored.Filled.Reply, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Απάντηση;")
                }
            }
                TextButton(onClick = { viewModel.reset() }) {
                    Text("Νέα μεταγραφή")
                }
            }
        }
    }


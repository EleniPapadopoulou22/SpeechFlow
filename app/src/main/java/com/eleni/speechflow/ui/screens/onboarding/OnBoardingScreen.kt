package com.eleni.speechflow.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eleni.speechflow.SpeechFlowApplication
import com.eleni.speechflow.ui.components.PrimaryButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val app = LocalContext.current.applicationContext as SpeechFlowApplication

    val pages = remember {
        listOf(
            OnboardingPage(
                title = "Καλώς ήρθες στο SpeechFlow",
                description = "Είσαι ή γνωρίζεις κάποιον με απώλεια ακοής; Επικοινωνήστε χωρίς περιορισμούς!" +
                        "Τώρα είναι πιο απλό από ποτέ!",
                icon = Icons.Default.WavingHand,
            ),
            OnboardingPage(
                title = "Ομιλία",
                description = "Εδώ μετατρέπεις τον προφορικό λόγο σε ευανάγνωστο κείμενο!",
                icon = Icons.Default.Hearing,
            ),
            OnboardingPage(
                title = "Πληκτρολόγηση",
                description = "Γράψε το κείμενο που θέλεις να επικοινωνήσεις και άσε την εφαρμογή να εκφωνήσει το μήνυμα για εσένα!",
                icon = Icons.Default.RecordVoiceOver,
            ),
        )
    }

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
            ) { pageIndex ->
                val page = pages[pageIndex]
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Surface(
                        modifier = Modifier.size(160.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = page.icon,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                    Spacer(Modifier.height(48.dp))
                    Text(
                        text = page.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp),
                    )
                }
            }

            // Pager indicator
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                pages.indices.forEach { i ->
                    val active = pagerState.currentPage == i
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(width = if (active) 24.dp else 8.dp, height = 8.dp)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .background(
                                if (active) MaterialTheme.colorScheme.tertiary
                                else MaterialTheme.colorScheme.outline
                            ),
                    )
                }
            }

            PrimaryButton(
                text = if (pagerState.currentPage == pages.lastIndex) "Ξεκίνα" else "Επόμενο",
                onClick = {
                    if (pagerState.currentPage == pages.lastIndex) {
                        scope.launch {
                            app.settingsRepository.completeOnboarding()
                            onFinish()
                        }
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                },
            )
        }
    }
}

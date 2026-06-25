package com.eleni.speechflow.data.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

//Διαχείριση αναγνώρισης ομιλίας μέσω SpeechRecognizer API

class SpeechRecognitionManager(private val context: Context) {

    private var recognizer: SpeechRecognizer? = null

    private val _state = MutableStateFlow(RecognitionState())
    val state: StateFlow<RecognitionState> = _state.asStateFlow()

    fun isAvailable(): Boolean = SpeechRecognizer.isRecognitionAvailable(context)

    fun start(language: String = "el-GR") {
        if (!isAvailable()) {
            _state.value = _state.value.copy(error = "Η αναγνώριση ομιλίας δεν είναι διαθέσιμη")
            return
        }

        destroy()

        recognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(listener)
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        _state.value = RecognitionState(isListening = true)
        recognizer?.startListening(intent)
    }


    fun stop() {
        recognizer?.stopListening()
        _state.value = _state.value.copy(isListening = false)
    }


    fun destroy() {
        recognizer?.destroy()
        recognizer = null
    }

    private val listener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            _state.value = _state.value.copy(isReady = true)
        }
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {
            _state.value = _state.value.copy(audioLevel = rmsdB)
        }
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {
            _state.value = _state.value.copy(isReady = false)
        }

        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Σφάλμα ήχου"
                SpeechRecognizer.ERROR_NETWORK -> "Πρόβλημα δικτύου"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Λήξη χρόνου δικτύου"
                SpeechRecognizer.ERROR_NO_MATCH -> "Δεν αναγνωρίστηκε ομιλία"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Δεν ανιχνεύθηκε ομιλία"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Απαιτείται άδεια μικροφώνου"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Η μηχανή είναι απασχολημένη"
                else -> "Άγνωστο σφάλμα"
            }
            _state.value = _state.value.copy(
                isListening = false, isReady = false, error = message
            )
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(
                SpeechRecognizer.RESULTS_RECOGNITION
            )
            val text = matches?.firstOrNull().orEmpty()
            _state.value = _state.value.copy(
                isListening = false, finalText = text, partialText = ""
            )
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val matches = partialResults?.getStringArrayList(
                SpeechRecognizer.RESULTS_RECOGNITION
            )
            val text = matches?.firstOrNull().orEmpty()
            _state.value = _state.value.copy(partialText = text)
        }

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    fun reset() { _state.value = RecognitionState() }
}

data class RecognitionState(
    val isListening: Boolean = false,
    val isReady: Boolean = false,
    val partialText: String = "",
    val finalText: String = "",
    val audioLevel: Float = 0f,
    val error: String? = null,
)

package com.shlok.sam.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

enum class VoiceState {
    IDLE, LISTENING, PROCESSING, SPEAKING, ERROR
}

class VoiceEngine(private val context: Context) : RecognitionListener {

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsReady = false

    private val _voiceState = MutableStateFlow(VoiceState.IDLE)
    val voiceState: StateFlow<VoiceState> = _voiceState.asStateFlow()

    private val _transcript = MutableStateFlow("")
    val transcript: StateFlow<String> = _transcript.asStateFlow()

    private val _amplitude = MutableStateFlow(0.2f)
    val amplitude: StateFlow<Float> = _amplitude.asStateFlow()

    var onSpeechRecognized: ((String) -> Unit)? = null

    init {
        initTts()
    }

    private fun initTts() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
                isTtsReady = true
            }
        }
    }

    fun startListening(languageCode: String = "en-US") {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _voiceState.value = VoiceState.ERROR
            return
        }

        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(this@VoiceEngine)
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        try {
            speechRecognizer?.startListening(intent)
            _voiceState.value = VoiceState.LISTENING
            _transcript.value = ""
        } catch (_: Exception) {
            _voiceState.value = VoiceState.ERROR
        }
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        _voiceState.value = VoiceState.IDLE
    }

    fun speak(text: String, onComplete: (() -> Unit)? = null) {
        if (!isTtsReady) return
        _voiceState.value = VoiceState.SPEAKING
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "sam_tts_utterance")
        _voiceState.value = VoiceState.IDLE
        onComplete?.invoke()
    }

    fun stopSpeaking() {
        textToSpeech?.stop()
        _voiceState.value = VoiceState.IDLE
    }

    fun destroy() {
        speechRecognizer?.destroy()
        textToSpeech?.shutdown()
    }

    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {
        _voiceState.value = VoiceState.LISTENING
    }
    override fun onRmsChanged(rmsdB: Float) {
        val normalized = (rmsdB + 2f).coerceIn(0.1f, 1.0f)
        _amplitude.value = normalized
    }
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {
        _voiceState.value = VoiceState.PROCESSING
    }
    override fun onError(error: Int) {
        _voiceState.value = VoiceState.ERROR
    }
    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val recognizedText = matches?.firstOrNull() ?: ""
        _transcript.value = recognizedText
        _voiceState.value = VoiceState.IDLE
        if (recognizedText.isNotBlank()) {
            onSpeechRecognized?.invoke(recognizedText)
        }
    }
    override fun onPartialResults(partialResults: Bundle?) {
        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val text = matches?.firstOrNull() ?: ""
        _transcript.value = text
    }
    override fun onEvent(eventType: Int, params: Bundle?) {}
}

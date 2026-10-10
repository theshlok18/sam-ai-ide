package com.shlok.sam.ai

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class ClaudeProvider : LLMProvider {
    override val providerName: String = "Anthropic Claude"
    private val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build()
    private val gson = Gson()

    override suspend fun generateResponse(request: LLMRequest): LLMResponse = withContext(Dispatchers.IO) {
        if (request.apiKey.isBlank()) {
            return@withContext LLMResponse("", request.modelId, isSuccess = false, errorMessage = "Anthropic Claude API Key is missing.")
        }
        try {
            val url = "https://api.anthropic.com/v1/messages"
            val payload = buildPayload(request, stream = false)
            val body = payload.toString().toRequestBody("application/json".toMediaType())
            val httpRequest = Request.Builder()
                .url(url)
                .addHeader("x-api-key", request.apiKey)
                .addHeader("anthropic-version", "2023-06-01")
                .post(body)
                .build()

            val response = client.newCall(httpRequest).execute()
            val responseBody = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                return@withContext LLMResponse("", request.modelId, isSuccess = false, errorMessage = "Claude Error (${response.code}): $responseBody")
            }
            val json = gson.fromJson(responseBody, JsonObject::class.java)
            val contentArr = json.getAsJsonArray("content")
            val text = contentArr.get(0).asJsonObject.get("text").asString
            return@withContext LLMResponse(text, request.modelId, isSuccess = true)
        } catch (e: Exception) {
            return@withContext LLMResponse("", request.modelId, isSuccess = false, errorMessage = e.localizedMessage)
        }
    }

    override fun streamResponse(request: LLMRequest): Flow<LLMStreamEvent> = callbackFlow {
        if (request.apiKey.isBlank()) {
            trySend(LLMStreamEvent.Error("Anthropic Claude API Key is missing."))
            close()
            return@callbackFlow
        }
        val url = "https://api.anthropic.com/v1/messages"
        val payload = buildPayload(request, stream = true)
        val body = payload.toString().toRequestBody("application/json".toMediaType())
        val httpRequest = Request.Builder()
            .url(url)
            .addHeader("x-api-key", request.apiKey)
            .addHeader("anthropic-version", "2023-06-01")
            .post(body)
            .build()

        val call = client.newCall(httpRequest)
        val accumulated = StringBuilder()

        try {
            val response = call.execute()
            val inputStream = response.body?.byteStream()
            if (inputStream != null) {
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line = reader.readLine()
                while (line != null) {
                    if (line.startsWith("data: ")) {
                        val data = line.substring(6).trim()
                        try {
                            val json = gson.fromJson(data, JsonObject::class.java)
                            val type = json.get("type")?.asString
                            if (type == "content_block_delta") {
                                val delta = json.getAsJsonObject("delta")
                                if (delta != null && delta.has("text")) {
                                    val chunk = delta.get("text").asString
                                    accumulated.append(chunk)
                                    trySend(LLMStreamEvent.Chunk(chunk))
                                }
                            }
                        } catch (_: Exception) {}
                    }
                    line = reader.readLine()
                }
            }
            trySend(LLMStreamEvent.Complete(accumulated.toString()))
        } catch (e: Exception) {
            trySend(LLMStreamEvent.Error(e.localizedMessage ?: "Claude stream error"))
        } finally {
            close()
        }
        awaitClose { call.cancel() }
    }

    private fun buildPayload(request: LLMRequest, stream: Boolean): JsonObject {
        val root = JsonObject()
        root.addProperty("model", request.modelId)
        root.addProperty("max_tokens", request.maxTokens)
        root.addProperty("stream", stream)
        if (request.systemPrompt.isNotEmpty()) {
            root.addProperty("system", request.systemPrompt)
        }
        val messages = JsonArray()
        request.messages.forEach { msg ->
            val m = JsonObject()
            m.addProperty("role", if (msg.role == "model" || msg.role == "assistant") "assistant" else "user")
            m.addProperty("content", msg.content)
            messages.add(m)
        }
        root.add("messages", messages)
        return root
    }
}

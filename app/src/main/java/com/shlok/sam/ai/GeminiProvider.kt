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

class GeminiProvider : LLMProvider {
    override val providerName: String = "Google Gemini"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    override suspend fun generateResponse(request: LLMRequest): LLMResponse = withContext(Dispatchers.IO) {
        if (request.apiKey.isBlank()) {
            return@withContext LLMResponse(
                content = "",
                model = request.modelId,
                isSuccess = false,
                errorMessage = "Google Gemini API Key is missing. Please configure it in AI Providers."
            )
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/${request.modelId}:generateContent?key=${request.apiKey}"
            val payload = buildPayload(request)
            val body = payload.toString().toRequestBody("application/json".toMediaType())

            val httpRequest = Request.Builder()
                .url(url)
                .post(body)
                .build()

            val response = client.newCall(httpRequest).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext LLMResponse(
                    content = "",
                    model = request.modelId,
                    isSuccess = false,
                    errorMessage = "Gemini API Error (${response.code}): $responseBody"
                )
            }

            val json = gson.fromJson(responseBody, JsonObject::class.java)
            val candidates = json.getAsJsonArray("candidates")
            if (candidates != null && candidates.size() > 0) {
                val candidate = candidates.get(0).asJsonObject
                val contentObj = candidate.getAsJsonObject("content")
                val parts = contentObj.getAsJsonArray("parts")
                val text = parts.get(0).asJsonObject.get("text").asString
                return@withContext LLMResponse(content = text, model = request.modelId, isSuccess = true)
            } else {
                return@withContext LLMResponse(content = "", model = request.modelId, isSuccess = false, errorMessage = "Empty response from Gemini")
            }
        } catch (e: Exception) {
            return@withContext LLMResponse(content = "", model = request.modelId, isSuccess = false, errorMessage = e.localizedMessage ?: "Unknown network error")
        }
    }

    override fun streamResponse(request: LLMRequest): Flow<LLMStreamEvent> = callbackFlow {
        if (request.apiKey.isBlank()) {
            trySend(LLMStreamEvent.Error("Google Gemini API Key is missing. Please configure your key in Providers Settings."))
            close()
            return@callbackFlow
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/${request.modelId}:streamGenerateContent?alt=sse&key=${request.apiKey}"
        val payload = buildPayload(request)
        val body = payload.toString().toRequestBody("application/json".toMediaType())

        val httpRequest = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val call = client.newCall(httpRequest)
        val fullAccumulated = StringBuilder()

        try {
            val response = call.execute()
            if (!response.isSuccessful) {
                val err = response.body?.string() ?: "HTTP ${response.code}"
                trySend(LLMStreamEvent.Error("Gemini streaming error ($err)"))
                close()
                return@callbackFlow
            }

            val inputStream = response.body?.byteStream()
            if (inputStream != null) {
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line: String? = reader.readLine()
                while (line != null) {
                    if (line.startsWith("data: ")) {
                        val jsonStr = line.substring(6).trim()
                        if (jsonStr.isNotEmpty() && jsonStr != "[DONE]") {
                            try {
                                val json = gson.fromJson(jsonStr, JsonObject::class.java)
                                val candidates = json.getAsJsonArray("candidates")
                                if (candidates != null && candidates.size() > 0) {
                                    val parts = candidates.get(0).asJsonObject.getAsJsonObject("content").getAsJsonArray("parts")
                                    if (parts != null && parts.size() > 0) {
                                        val chunk = parts.get(0).asJsonObject.get("text").asString
                                        fullAccumulated.append(chunk)
                                        trySend(LLMStreamEvent.Chunk(chunk))
                                    }
                                }
                            } catch (_: Exception) {}
                        }
                    }
                    line = reader.readLine()
                }
            }
            trySend(LLMStreamEvent.Complete(fullAccumulated.toString()))
        } catch (e: Exception) {
            trySend(LLMStreamEvent.Error(e.localizedMessage ?: "Streaming interrupted"))
        } finally {
            close()
        }

        awaitClose { call.cancel() }
    }

    private fun buildPayload(request: LLMRequest): JsonObject {
        val root = JsonObject()
        
        // System instruction
        if (request.systemPrompt.isNotEmpty()) {
            val sysInstruction = JsonObject()
            val sysParts = JsonArray()
            val sysPart = JsonObject()
            sysPart.addProperty("text", request.systemPrompt)
            sysParts.add(sysPart)
            sysInstruction.add("parts", sysParts)
            root.add("systemInstruction", sysInstruction)
        }

        val contents = JsonArray()
        request.messages.forEach { msg ->
            val contentObj = JsonObject()
            val role = if (msg.role == "assistant" || msg.role == "model") "model" else "user"
            contentObj.addProperty("role", role)
            val parts = JsonArray()
            val part = JsonObject()
            part.addProperty("text", msg.content)
            parts.add(part)
            contentObj.add("parts", parts)
            contents.add(contentObj)
        }
        root.add("contents", contents)

        val genConfig = JsonObject()
        genConfig.addProperty("temperature", request.temperature)
        genConfig.addProperty("maxOutputTokens", request.maxTokens)
        root.add("generationConfig", genConfig)

        return root
    }
}

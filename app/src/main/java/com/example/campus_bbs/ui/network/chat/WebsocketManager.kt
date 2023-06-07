package com.example.campus_bbs.ui.network.chat

import io.ktor.client.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.Json

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


private const val WS_BASE_URL = ""
private const val PORT = 8080
class WebsocketManager(private val senderId: String, private val receiverId: String) {

    private val client = HttpClient(OkHttp) {
        engine {
            preconfigured = OkHttpClient.Builder()
                .pingInterval(20, TimeUnit.SECONDS)
                .build()
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    private var session: DefaultClientWebSocketSession ?= null

    suspend fun connect() {
        if (session == null) {
            session = client.webSocketSession {
                url("ws", WS_BASE_URL, PORT, "/chat") {
                    parameters.append("sender_id", senderId)
                    parameters.append("receiver_id", receiverId)
                }
            }
        }
    }
}
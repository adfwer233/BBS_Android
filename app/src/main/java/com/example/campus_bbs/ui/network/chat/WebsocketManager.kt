package com.example.campus_bbs.ui.network.chat

import android.util.Log
import com.example.campus_bbs.Global
import io.ktor.client.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.serialization.json.Json

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


private const val WS_BASE_URL = Global.BASE_WS_URL
private const val PORT = 8080
class WebsocketManager(jwtToken: String) {

    private val client = HttpClient(OkHttp) {
        install(Auth) {
            bearer {
                loadTokens {
                    Log.i("auth", jwtToken)
                    BearerTokens(jwtToken, jwtToken)
                }
            }
        }
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

    suspend fun connect(
        token: String,
        onConnect: () -> Unit,
        onReceive: (chatWebSocketResponse: ChatWebSocketResponse) -> Unit
    ) {
        if (session == null) {
            session = client.webSocketSession {
                url("ws", WS_BASE_URL, PORT, "/chat")
//                header("Authorization", token)
            }
        }
        send({ }, ChatWebSocketRequest(operation = "register", senderId = "", "", "", token))
        session!!.incoming.consumeAsFlow().collect {frame ->
            onReceive(session!!.converter?.deserialize(frame) as ChatWebSocketResponse)
        }
    }

    suspend fun send(onSend: () -> Unit, chatWebSocketRequest: ChatWebSocketRequest) {
        session!!.sendSerialized(chatWebSocketRequest)
        onSend()
    }

    suspend fun close() {
        session!!.close()
    }
}
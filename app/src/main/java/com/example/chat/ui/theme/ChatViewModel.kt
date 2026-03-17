package com.example.todo

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class ChatViewModel : ViewModel() {

    // Alterar o IP para sua máquina ou usar 10.0.2.2 no emulador
    private val socket: Socket = IO.socket("http://10.111.9.8:3000")

    // Lista de mensagens visível pelo Compose
    var messages = mutableStateListOf<Pair<String, String>>()
        private set

    init {
        socket.connect()

        // Recebe mensagens do servidor
        socket.on("receive_message") { args ->
            val data = args[0] as JSONObject
            val author = data.getString("author")
            val message = data.getString("message")

            // Adiciona a mensagem na lista de forma thread-safe para Compose
            messages.add(author to message)
        }
    }

    // Envia mensagem para o servidor
    fun sendMessage(text: String) {
        val json = JSONObject().apply {
            put("author", "Eu")
            put("message", text)
        }

        socket.emit("send_message", json)

        // Feedback local imediato
        messages.add("Eu" to text)
    }

    override fun onCleared() {
        super.onCleared()
        socket.disconnect() // Fecha a conexão quando a ViewModel morre
    }
}
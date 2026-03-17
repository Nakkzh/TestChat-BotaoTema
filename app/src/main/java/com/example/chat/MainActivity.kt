package com.example.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chat.ui.theme.ChatTheme
import com.example.todo.ChatViewModel

// Classe principal
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var darkTheme by remember { mutableStateOf(false) }

            ChatTheme(darkTheme = darkTheme) {
                Main(
                    darkTheme = darkTheme,
                    onToggleTheme = { darkTheme = !darkTheme }
                )
            }
        }
    }
}

// Rotas
sealed class Screen(val route: String) {
    object Chat : Screen("chat")
}

// Navegação
@Composable
fun Main(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Chat.route
    ) {
        composable(Screen.Chat.route) {
            val viewModel: ChatViewModel = viewModel()
            ChatScreen(
                viewModel = viewModel,
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme
            )
        }
    }
}

// Tela de chat
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    var textState by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // fundo correto
            .statusBarsPadding() // evita sobreposição na barra
            .padding(16.dp)
    ) {

        // Botão de tema
        Button(
            onClick = onToggleTheme,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (darkTheme) "Modo Claro" else "Modo Escuro")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de mensagens
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(viewModel.messages) { message ->
                Text(
                    text = "${message.first}: ${message.second}",
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.onBackground // melhora visibilidade
                )
            }
        }

        // Input + botão
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Digite sua mensagem...") }
            )

            Button(
                onClick = {
                    if (textState.isNotBlank()) {
                        viewModel.sendMessage(textState)
                        textState = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Enviar")
            }
        }
    }
}
package com.ucaldas.perfil

// taller 4 - diego rojas - codigo 35295

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class ActividadPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // el surface lo puse para que el fondo tome el color del tema
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaPerfil()
                }
            }
        }
    }
}

@Composable
fun PantallaPerfil() {
    // aqui uso rememberSaveable para que nada se pierda cuando giro el celular
    var publicaciones by rememberSaveable { mutableStateOf(0) }
    var nombre by rememberSaveable { mutableStateOf("Diego Rojas") }
    var editando by rememberSaveable { mutableStateOf(false) }
    var borrador by rememberSaveable { mutableStateOf("") }

    // la lista la deje con remember porque mutableStateListOf no se puede guardar con rememberSaveable asi nomas
    // antes la tenia con mutableListOf y la pantalla no se actualizaba al agregar logros
    val logros = remember { mutableStateListOf("Primer commit") }

    Column(
        // safeDrawingPadding para que no quede debajo de la barra de estado,
        // verticalScroll porque al rotar el celular no cabe todo
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Avatar(iniciales = inicialesDe(nombre))

        Spacer(modifier = Modifier.height(12.dp))

        // si el nombre queda vacio muestro un texto por defecto
        Text(
            text = nombre.ifBlank { "Usuario sin nombre" },
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Ingeniería en Informática · código 35295",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            // el padding va adentro de la card para que el color llegue hasta el borde
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Publicaciones", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$publicaciones",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // el boton de menos se desactiva cuando el contador esta en 0
                    Button(
                        onClick = { if (publicaciones > 0) publicaciones-- },
                        enabled = publicaciones > 0
                    ) { Text("-") }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { publicaciones++ }) { Text("+") }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SeccionLogros(logros = logros)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            borrador = nombre
            editando = !editando
        }) {
            Text(if (editando) "Cancelar" else "Editar perfil")
        }

        // con AnimatedVisibility el formulario aparece suave y no de golpe
        AnimatedVisibility(visible = editando) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                OutlinedTextField(
                    value = borrador,
                    onValueChange = { borrador = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        nombre = borrador.ifBlank { "Usuario sin nombre" }
                        editando = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Guardar") }
            }
        }
    }
}

@Composable
fun Avatar(iniciales: String) {
    // el orden importa: primero el tamaño, luego recorto en circulo y al final pinto el fondo
    // si pongo el background antes del clip el fondo me queda cuadrado
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun SeccionLogros(logros: MutableList<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Logros", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        logros.forEach { logro ->
            Text(text = "• $logro", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = {
            logros.add("Nuevo logro ${logros.size + 1}")
            // este log lo puse para comprobar en logcat que la lista si crece
            Log.d("Taller4", "Tamaño de la lista: ${logros.size}")
        }) {
            Text("Agregar logro")
        }
    }
}

// saco las dos primeras letras del nombre, si no hay nada pongo un ?
private fun inicialesDe(nombre: String): String =
    nombre.trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "?" }

@Preview(showBackground = true, name = "Perfil")
@Composable
fun VistaPreviaPerfil() {
    MaterialTheme { PantallaPerfil() }
}

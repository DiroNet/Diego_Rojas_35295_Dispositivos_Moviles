package com.diegoivanrojasavila.lugarescaldas

// taller 5 - diego rojas - codigo 35295

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // bonus b: subi el estado del scroll por encima del navhost
    // asi cuando vuelvo del detalle la lista sigue en la misma posicion
    val listState = rememberLazyListState()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaScreen(
                elementos = elementos,
                listState = listState,
                onElementoClick = { id ->
                    // con launchSingleTop evito que se apile el mismo detalle varias veces
                    // si toco la tarjeta muy rapido
                    navController.navigate("detalle/$id") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = "detalle/{elementoId}",
            arguments = listOf(navArgument("elementoId") { type = NavType.IntType })
        ) { backStackEntry ->
            // saco el id que me llego por la ruta y busco el lugar, sin usar !!
            val id = backStackEntry.arguments?.getInt("elementoId") ?: 0
            val elemento = elementos.firstOrNull { it.id == id }
            if (elemento != null) {
                DetalleScreen(elemento = elemento, onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun ListaScreen(
    elementos: List<Elemento>,
    listState: LazyListState,
    onElementoClick: (Int) -> Unit
) {
    // uso rememberSaveable para que el texto de busqueda siga ahi cuando vuelvo del detalle
    var query by rememberSaveable { mutableStateOf("") }

    // el filtro solo se recalcula cuando cambia lo que escribo
    val filtrados = remember(query) {
        if (query.isBlank()) elementos
        else elementos.filter { it.titulo.contains(query, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Text(
            text = "Lugares de Caldas · Diego Rojas 35295",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        LazyColumn(state = listState) {
            // la key con el id ayuda a que compose no confunda las tarjetas al filtrar
            items(filtrados, key = { it.id }) { elemento ->
                // el clickable va despues del padding para que el toque sea solo sobre la tarjeta
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onElementoClick(elemento.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icono(categoria = elemento.categoria, tamano = 48)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(elemento.titulo, style = MaterialTheme.typography.titleMedium)
                            Text(elemento.descripcionCorta, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetalleScreen(elemento: Elemento, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp)
    ) {
        Icono(categoria = elemento.categoria, tamano = 80)
        Spacer(Modifier.height(16.dp))
        Text(elemento.titulo, style = MaterialTheme.typography.headlineMedium)
        Text(elemento.categoria, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(16.dp))
        Text(elemento.descripcionLarga, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}

// circulo de color con la letra de la categoria, lo uso en la lista y en el detalle
@Composable
fun Icono(categoria: String, tamano: Int) {
    Box(
        modifier = Modifier
            .size(tamano.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iconoDe(categoria),
            fontSize = (tamano / 2).sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListaPreview() {
    MaterialTheme {
        ListaScreen(elementos = elementos, listState = rememberLazyListState(), onElementoClick = {})
    }
}

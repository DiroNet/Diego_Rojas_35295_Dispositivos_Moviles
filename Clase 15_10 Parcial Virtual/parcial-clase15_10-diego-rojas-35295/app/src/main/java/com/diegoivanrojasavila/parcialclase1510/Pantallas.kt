package com.diegoivanrojasavila.parcialclase1510

// parcial clase 15_10 - diego rojas 35295
// pantallas de lista y detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun PantallaLista(
    modeloVista: ModeloVistaPersonajes,
    estadoLista: LazyListState,
    alTocarElemento: (Int) -> Unit
) {
    val elementos by modeloVista.elementosFiltrados.collectAsStateWithLifecycle()
    val cargando by modeloVista.cargando.collectAsStateWithLifecycle()
    val textoBusqueda by modeloVista.textoBusqueda.collectAsStateWithLifecycle()
    val verVillanos by modeloVista.verVillanos.collectAsStateWithLifecycle()
    val alcance = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Text(
            text = if (verVillanos) "Villanos · Diego Rojas 35295" else "Superhéroes · Diego Rojas 35295",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )

        ContenidoLista(
            elementos = elementos,
            cargando = cargando,
            textoBusqueda = textoBusqueda,
            verVillanos = verVillanos,
            estadoLista = estadoLista,
            alCambiarBusqueda = { texto -> modeloVista.cambiarBusqueda(texto) },
            alCambiarFiltro = { villanos ->
                modeloVista.mostrarVillanos(villanos)
                // al cambiar de lista se vuelve arriba
                alcance.launch { estadoLista.scrollToItem(0) }
            },
            alTocarElemento = alTocarElemento
        )
    }
}

@Composable
fun ContenidoLista(
    elementos: List<Elemento>,
    cargando: Boolean,
    textoBusqueda: String,
    verVillanos: Boolean,
    estadoLista: LazyListState,
    alCambiarBusqueda: (String) -> Unit,
    alCambiarFiltro: (Boolean) -> Unit,
    alTocarElemento: (Int) -> Unit
) {
    Column {
        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = alCambiarBusqueda,
            label = { Text(if (verVillanos) "Buscar villano" else "Buscar héroe") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            FilterChip(
                selected = !verVillanos,
                onClick = { alCambiarFiltro(false) },
                label = { Text("Héroes") }
            )
            FilterChip(
                selected = verVillanos,
                onClick = { alCambiarFiltro(true) },
                label = { Text("Villanos") }
            )
        }

        if (cargando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(state = estadoLista) {
                items(elementos, key = { it.id }) { elemento ->
                    TarjetaElemento(
                        elemento = elemento,
                        alTocar = { alTocarElemento(elemento.id) }
                    )
                }
            }
        }
    }
}

// tarjeta con la imagen grande y el nombre abajo
@Composable
fun TarjetaElemento(
    elemento: Elemento,
    alTocar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { alTocar() }
    ) {
        AsyncImage(
            model = elemento.imagenUrl,
            contentDescription = elemento.nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        Text(
            text = elemento.nombre,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PantallaDetalle(
    modeloVista: ModeloVistaPersonajes,
    id: Int,
    alVolver: () -> Unit
) {
    val elemento by remember(id) { modeloVista.elementoPorId(id) }
        .collectAsStateWithLifecycle(initialValue = null)

    val heroe = elemento ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        AsyncImage(
            model = heroe.imagenUrl,
            contentDescription = heroe.nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        Spacer(Modifier.height(16.dp))
        Text(heroe.nombre, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(heroe.descripcion, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))
        Text("Superpoderes", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            heroe.superPoderes.forEach { poder ->
                AssistChip(onClick = {}, label = { Text(poder) })
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = alVolver) { Text("Volver") }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaLista() {
    MaterialTheme {
        ContenidoLista(
            elementos = obtenerSuperheroes().take(4),
            cargando = false,
            textoBusqueda = "",
            verVillanos = false,
            estadoLista = rememberLazyListState(),
            alCambiarBusqueda = {},
            alCambiarFiltro = {},
            alTocarElemento = {}
        )
    }
}

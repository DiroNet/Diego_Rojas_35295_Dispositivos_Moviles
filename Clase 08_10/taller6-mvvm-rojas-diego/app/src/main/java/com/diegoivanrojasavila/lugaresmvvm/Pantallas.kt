package com.diegoivanrojasavila.lugaresmvvm

// taller 6 - diego rojas - código 35295
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@Composable
fun PantallaLista(
    modeloVista: ModeloVistaHeroes,
    estadoLista: LazyListState,
    alTocarElemento: (Int) -> Unit
) {
    val elementos by modeloVista.elementosFiltrados.collectAsStateWithLifecycle()
    val cargando by modeloVista.cargando.collectAsStateWithLifecycle()
    val textoBusqueda by modeloVista.textoBusqueda.collectAsStateWithLifecycle()
    val cantidadFavoritos by modeloVista.cantidadFavoritos.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Text(
            text = "Superhéroes · Diego Rojas 35295",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        Text(
            text = "Favoritos: $cantidadFavoritos",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )

        ContenidoLista(
            elementos = elementos,
            cargando = cargando,
            textoBusqueda = textoBusqueda,
            estadoLista = estadoLista,
            alCambiarBusqueda = { texto -> modeloVista.cambiarBusqueda(texto) },
            alTocarElemento = alTocarElemento,
            alMarcarFavorito = { id -> modeloVista.marcarFavorito(id) }
        )
    }
}

@Composable
fun ContenidoLista(
    elementos: List<Elemento>,
    cargando: Boolean,
    textoBusqueda: String,
    estadoLista: LazyListState,
    alCambiarBusqueda: (String) -> Unit,
    alTocarElemento: (Int) -> Unit,
    alMarcarFavorito: (Int) -> Unit
) {
    Column {
        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = alCambiarBusqueda,
            label = { Text("Buscar héroe") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        if (cargando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(state = estadoLista) {
                items(elementos, key = { it.id }) { elemento ->
                    TarjetaElemento(
                        elemento = elemento,
                        alTocar = { alTocarElemento(elemento.id) },
                        alMarcarFavorito = { alMarcarFavorito(elemento.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaElemento(
    elemento: Elemento,
    alTocar: () -> Unit,
    alMarcarFavorito: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { alTocar() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImagenHeroe(elemento = elemento, tamano = 64.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(elemento.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${elemento.superPoderes.size} superpoderes",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            BotonFavorito(esFavorito = elemento.esFavorito, alMarcar = alMarcarFavorito)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PantallaDetalle(
    modeloVista: ModeloVistaHeroes,
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                heroe.nombre,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            BotonFavorito(
                esFavorito = heroe.esFavorito,
                alMarcar = { modeloVista.marcarFavorito(heroe.id) }
            )
        }
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

// estrella llena = favorito
@Composable
fun BotonFavorito(esFavorito: Boolean, alMarcar: () -> Unit) {
    IconToggleButton(checked = esFavorito, onCheckedChange = { alMarcar() }) {
        Text(
            text = if (esFavorito) "★" else "☆",
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// imagen del héroe cargada con coil
@Composable
fun ImagenHeroe(elemento: Elemento, tamano: Dp) {
    AsyncImage(
        model = elemento.imagenUrl,
        contentDescription = elemento.nombre,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(tamano)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
    )
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaLista() {
    MaterialTheme {
        ContenidoLista(
            elementos = obtenerSuperheroes().take(4),
            cargando = false,
            textoBusqueda = "",
            estadoLista = rememberLazyListState(),
            alCambiarBusqueda = {},
            alTocarElemento = {},
            alMarcarFavorito = {}
        )
    }
}

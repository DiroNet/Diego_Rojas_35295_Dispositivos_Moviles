package com.diegoivanrojasavila.lugaresmvvm

// taller 6 - diego rojas - código 35295
// estado y lógica de la app, sin nada de compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ModeloVistaHeroes : ViewModel() {

    // privado para que solo esta clase cambie la lista
    private val _elementos = MutableStateFlow<List<Elemento>>(emptyList())
    val elementos: StateFlow<List<Elemento>> = _elementos

    // empieza en true para mostrar la carga al abrir
    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando

    // texto del buscador
    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda

    // lista filtrada por nombre, se recalcula cuando cambia la lista o el texto
    val elementosFiltrados: StateFlow<List<Elemento>> =
        combine(_elementos, _textoBusqueda) { lista, texto ->
            if (texto.isBlank()) lista
            else lista.filter { it.nombre.contains(texto, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // bonus opción A: cantidad de favoritos calculada desde la lista
    val cantidadFavoritos: StateFlow<Int> =
        _elementos.map { lista -> lista.count { it.esFavorito } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // al rotar no se vuelve a cargar porque el modelo de vista sigue vivo
    init {
        cargarElementos()
    }

    fun cargarElementos() {
        viewModelScope.launch {
            _cargando.value = true
            delay(1000) // simula la espera de una carga
            _elementos.value = obtenerSuperheroes()
            _cargando.value = false
        }
    }

    fun cambiarBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }

    // corrección de la parte 2: se asigna una lista nueva al stateflow para que la ui se actualice
    fun marcarFavorito(id: Int) {
        _elementos.value = _elementos.value.map {
            if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
        }
    }

    // héroe por id para la pantalla de detalle
    fun elementoPorId(id: Int): Flow<Elemento?> =
        _elementos.map { lista -> lista.firstOrNull { it.id == id } }
}

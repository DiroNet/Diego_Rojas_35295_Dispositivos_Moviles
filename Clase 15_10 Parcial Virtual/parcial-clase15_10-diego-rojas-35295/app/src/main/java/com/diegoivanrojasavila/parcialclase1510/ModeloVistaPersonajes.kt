package com.diegoivanrojasavila.parcialclase1510

// parcial clase 15_10 - diego rojas 35295
// estado y lógica de la app, sin nada de compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ModeloVistaPersonajes : ViewModel() {

    // privado para que solo esta clase cambie las listas
    private val _heroes = MutableStateFlow<List<Elemento>>(emptyList())
    private val _villanos = MutableStateFlow<List<Elemento>>(emptyList())

    // empieza en true para mostrar la carga al abrir
    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando

    // texto del buscador
    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda

    // false muestra héroes, true muestra villanos
    private val _verVillanos = MutableStateFlow(false)
    val verVillanos: StateFlow<Boolean> = _verVillanos

    // lista que se ve según el filtro y lo que se escriba en el buscador
    val elementosFiltrados: StateFlow<List<Elemento>> =
        combine(_heroes, _villanos, _verVillanos, _textoBusqueda) { heroes, villanos, verVillanos, texto ->
            val lista = if (verVillanos) villanos else heroes
            if (texto.isBlank()) lista
            else lista.filter { it.nombre.contains(texto, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // al rotar no se vuelve a cargar porque el modelo de vista sigue vivo
    init {
        cargarElementos()
    }

    fun cargarElementos() {
        viewModelScope.launch {
            _cargando.value = true
            delay(1000) // simula la espera de una carga
            _heroes.value = obtenerSuperheroes()
            _villanos.value = obtenerVillanos()
            _cargando.value = false
        }
    }

    fun cambiarBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }

    fun mostrarVillanos(villanos: Boolean) {
        _verVillanos.value = villanos
    }

    // busca en las dos listas para la pantalla de detalle
    fun elementoPorId(id: Int): Flow<Elemento?> =
        combine(_heroes, _villanos) { heroes, villanos ->
            (heroes + villanos).firstOrNull { it.id == id }
        }
}

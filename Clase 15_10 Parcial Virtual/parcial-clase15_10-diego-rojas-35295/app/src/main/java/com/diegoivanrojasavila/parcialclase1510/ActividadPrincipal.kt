package com.diegoivanrojasavila.parcialclase1510

// parcial clase 15_10 - diego rojas 35295

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class ActividadPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavegacionPrincipal()
                }
            }
        }
    }
}

@Composable
fun NavegacionPrincipal() {
    val controladorNavegacion = rememberNavController()

    // un solo modelo de vista para la lista y el detalle
    val modeloVista: ModeloVistaPersonajes = viewModel()

    // guarda la posición de la lista al volver del detalle
    val estadoLista = rememberLazyListState()

    NavHost(navController = controladorNavegacion, startDestination = "lista") {
        composable("lista") {
            PantallaLista(
                modeloVista = modeloVista,
                estadoLista = estadoLista,
                alTocarElemento = { id ->
                    controladorNavegacion.navigate("detalle/$id") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = "detalle/{elementoId}",
            arguments = listOf(navArgument("elementoId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("elementoId") ?: 0
            PantallaDetalle(
                modeloVista = modeloVista,
                id = id,
                alVolver = { controladorNavegacion.popBackStack() }
            )
        }
    }
}

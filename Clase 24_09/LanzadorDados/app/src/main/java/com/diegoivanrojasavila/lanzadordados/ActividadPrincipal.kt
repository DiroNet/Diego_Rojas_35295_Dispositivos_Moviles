package com.diegoivanrojasavila.lanzadordados

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diegoivanrojasavila.lanzadordados.ui.tema.TemaLanzadorDados

// actividad principal de la aplicacion
class ActividadPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TemaLanzadorDados {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaLanzadorDados()
                }
            }
        }
    }
}

// vista previa de la aplicacion
@Preview(showBackground = true)
@Composable
fun PantallaLanzadorDados() {
    DadoConBotonEImagen(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .wrapContentSize(Alignment.Center)
    )
}

// composable que gestiona la imagen del dado y el boton de lanzamiento
@Composable
fun DadoConBotonEImagen(modifier: Modifier = Modifier) {
    var resultado by remember { mutableIntStateOf(1) }

    // seleccion del recurso de imagen segun el resultado
    val imagen = when (resultado) {
        1 -> R.drawable.dado_1
        2 -> R.drawable.dado_2
        3 -> R.drawable.dado_3
        4 -> R.drawable.dado_4
        5 -> R.drawable.dado_5
        else -> R.drawable.dado_6
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.nombre_estudiante),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(imagen),
            contentDescription = stringResource(R.string.descripcion_dado, resultado),
            modifier = Modifier.size(220.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { resultado = (1..6).random() },
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.lanzar),
                fontSize = 16.sp
            )
        }
    }
}

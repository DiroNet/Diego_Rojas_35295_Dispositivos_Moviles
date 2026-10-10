# Análisis — Taller 4

**Estudiante:** Diego Rojas · **Código:** 35295
**Proyecto:** PerfilUsuario (`com.ucaldas.perfil`)

## 1. Predicción inicial (escrita antes de programar)

1. **Si uso `remember { mutableStateOf(0) }` para un contador y roto la pantalla, ¿el valor se mantiene o se pierde?**
   Creo que se pierde, porque al rotar la Activity se vuelve a crear y `remember` solo guarda el valor mientras el composable está en la composición.

2. **Si tengo una lista creada con `remember { mutableListOf(...) }` y le agrego un elemento con `.add()`, ¿la pantalla se actualiza?**
   Creo que sí se actualiza, porque la lista está dentro de un `remember` y le estoy agregando datos.

3. **¿Qué diferencia hay entre `remember` y `rememberSaveable`?**
   `remember` guarda el valor entre recomposiciones, y `rememberSaveable` además lo guarda cuando la Activity se destruye y se vuelve a crear, por ejemplo al rotar.

## 2. Verificación de la predicción

- **Pregunta 1:** acerté. Con `remember` llevé el contador a 5, roté el emulador y volvió a 0.
- **Pregunta 2:** me equivoqué. La pantalla no se actualizó. El `remember` solo hace que la lista no se cree de nuevo en cada recomposición, pero no hace que Compose se entere de los cambios. `mutableListOf` es una lista normal de Kotlin y Compose no la observa.
- **Pregunta 3:** acerté. Lo comprobé en la Parte 3 con el contador y el nombre.

## 3. Diagnóstico del bug (Parte 2)

### Síntoma
Al presionar "Agregar logro", la lista no se actualiza en pantalla.

### ¿Por qué ocurre?
`mutableListOf` devuelve un `ArrayList` normal de Kotlin. Compose solo vuelve a dibujar (recomponer) cuando cambia un objeto de **estado** que él observa, como los que crea `mutableStateOf` o `mutableStateListOf`. Como el `ArrayList` no avisa a nadie cuando se le agrega algo, Compose nunca se entera y no recompone. El `remember` solo hace que la misma lista sobreviva entre recomposiciones, no la vuelve observable.

### Evidencia
Puse un `Log.d("Taller4", "Tamaño de la lista: ${logros.size}")` dentro del `onClick`. En Logcat el tamaño iba subiendo (2, 3, 4...) pero en la pantalla seguía apareciendo solo "Primer commit". Eso muestra que el elemento sí se agregaba; el problema era que Compose no se daba cuenta.

### Corrección aplicada
Usé la **opción A**: `remember { mutableStateListOf("Primer commit") }`. La escogí porque solo tuve que cambiar una línea y puedo seguir usando `.add()` normalmente.

### ¿Por qué funciona?
`mutableStateListOf` devuelve una `SnapshotStateList`, que es una lista observable. Cuando le agrego un elemento, el sistema de snapshots de Compose registra el cambio y marca como inválidos los composables que leen esa lista (en este caso `SeccionLogros`), así que se recomponen y aparece el nuevo logro.

## 4. Experimento de rotación (Parte 3)

### Con `remember`
Llevé el contador a 5 y roté el emulador. El contador volvió a 0.
Pasa porque al rotar, Android destruye la Activity (`onDestroy`) y crea una nueva (`onCreate`). Toda la composición arranca de cero y `remember` no tiene dónde guardar el valor anterior.

### Con `rememberSaveable`
Repetí el experimento y el contador se quedó en 5 después de rotar.

### Explicación técnica
Antes de destruir la Activity por el cambio de configuración, Android llama a `onSaveInstanceState` y guarda un `Bundle`. `rememberSaveable` escribe su valor en ese `Bundle`, y cuando la nueva Activity se crea y el composable vuelve a ejecutarse, lee el valor de ahí en lugar de usar el inicial. Por eso funciona con tipos que se pueden guardar en un `Bundle` (Int, String, Boolean...).

Usé `rememberSaveable` para `publicaciones`, `nombre`, `editando` y `borrador`.

**Limitación conocida:** la lista de logros la dejé con `remember`, porque `mutableStateListOf` no se puede guardar directamente con `rememberSaveable`. Por eso, al rotar, se pierden los logros que agregué. Se podría arreglar con un `Saver` o con un `ViewModel`.

## 5. Justificación de los Modifier usados

1. **Avatar: `.size(96.dp)` → `.clip(CircleShape)` → `.background(...)`**
   Primero le doy el tamaño, después recorto en círculo y por último pinto el fondo. Si pongo `.background()` antes de `.clip()`, el fondo se pinta cuadrado antes del recorte y el avatar no se ve redondo. Lo probé y quedó cuadrado.

2. **Column de la Card: `.fillMaxWidth()` → `.padding(20.dp)`**
   El padding va dentro de la `Card`, en la `Column` de adentro. Así el color de la tarjeta llega hasta el borde y solo el contenido queda separado. Si pongo el padding en el modifier de la `Card`, la tarjeta completa se encoge y queda espacio vacío alrededor.

3. **Column principal: `.fillMaxSize()` → `.safeDrawingPadding()` → `.verticalScroll(...)` → `.padding(16.dp)`**
   `safeDrawingPadding` evita que el contenido quede debajo de la barra de estado. `verticalScroll` lo agregué porque al rotar a horizontal no cabe todo y no se podía llegar al botón de editar. El `padding(16.dp)` va al final para que sea parte del contenido que se desplaza; si lo pongo antes del scroll, el margen queda fijo y el contenido se corta al desplazarse.

## 6. Dificultades encontradas

- Al principio no entendía por qué la lista no cambiaba si en Logcat el tamaño sí subía. Ahí entendí que `remember` no es lo mismo que "observable".
- En horizontal la pantalla no cabía, así que tuve que agregar `verticalScroll`.
- Me tocó revisar que el `OutlinedTextField` tuviera `onValueChange = { borrador = it }`, porque sin eso no deja escribir.

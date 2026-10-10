# Análisis — Taller 5

**Estudiante:** Diego Rojas · **Código:** 35295
**Proyecto:** LugaresCaldas (`com.diegoivanrojasavila.lugarescaldas`)
**Tema:** 12 lugares de Caldas en 3 categorías (Pueblo, Naturaleza, Manizales)

## 1. Predicción inicial (escrita antes de programar)

> Si el usuario toca la MISMA tarjeta tres veces rápido antes de que cargue la pantalla de detalle, ¿cuántas veces se navegará y cuántas pantallas de detalle quedarán apiladas?

Mi predicción: creo que se va a navegar una sola vez y va a quedar un solo detalle, porque es la misma ruta y pensé que Navigation se daba cuenta de que ya estaba abierta.

## 2. Verificación de la predicción

Me equivoqué. Al tocar la tarjeta tres veces rápido se navegó tres veces y quedaron tres pantallas de detalle iguales en la pila. Al presionar "Volver" tuve que presionarlo tres veces para regresar a la lista.

## 3. Cacería de bugs: navegación duplicada (Parte 3)

### Síntoma
Si toco la misma tarjeta varias veces muy rápido, se apilan varias pantallas de detalle iguales. "Volver" me lleva a otro detalle idéntico en vez de a la lista.

### Causa técnica
Cada llamada a `navController.navigate("detalle/$id")` **agrega** un destino nuevo encima de la pila (back stack), sin revisar qué hay arriba. Mientras la animación de entrada del detalle no ha terminado, la tarjeta de la lista todavía recibe toques, así que cada toque ejecuta otro `navigate()` y empuja otra copia del mismo destino.

Pila con el bug después de 3 toques:

```
lista → detalle/5 → detalle/5 → detalle/5
```

### Corrección
Agregué la opción `launchSingleTop`:

```kotlin
navController.navigate("detalle/$id") {
    launchSingleTop = true
}
```

Con `launchSingleTop = true`, antes de agregar el destino, Navigation revisa si el destino que está en el tope de la pila es el mismo (misma ruta y mismos argumentos). Si lo es, no crea otra copia y reutiliza el que ya está.

Pila después de la corrección con 3 toques:

```
lista → detalle/5
```

### Caso límite: dos tarjetas distintas
Probé tocar dos tarjetas distintas rápido (por ejemplo la 2 y la 5). En ese caso sí quedan dos detalles en la pila (`lista → detalle/2 → detalle/5`), porque las rutas tienen argumentos distintos y no son el mismo destino.

Me parece que está bien que se comporte así: `launchSingleTop` solo evita repetir **el mismo destino** en el tope, no bloquea navegar a destinos distintos, porque eso puede ser navegación legítima. Si quisiera evitarlo también, tendría que hacer otra cosa, como ignorar clics mientras la pantalla está en transición.

## 4. Otras decisiones

- **Búsqueda:** el filtro se calcula con `remember(query)`, así solo se recalcula cuando cambia el texto. El texto de búsqueda lo guardé con `rememberSaveable` porque con `remember` normal se perdía al volver del detalle (la pantalla de lista sale de la composición mientras estoy en el detalle).
- **`key = { it.id }`:** lo puse en `items` para que Compose identifique cada tarjeta por su id y no por su posición, así al filtrar no se mezclan.
- **Sin `!!`:** para leer el argumento usé `backStackEntry.arguments?.getInt("elementoId") ?: 0` y `firstOrNull`, así si el id no existe no se cae la app.
- **Bonus (opción B):** subí el `rememberLazyListState()` a `AppNavigation`, por encima del `NavHost`. Así, cuando vuelvo del detalle, la lista queda en la misma posición de scroll en la que estaba.
- **Pulido:** cada tarjeta y el detalle tienen un ícono: un círculo de color con la inicial de la categoría (P, N o M).

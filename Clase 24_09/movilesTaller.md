# Taller en Clase – Dispositivos Móviles
## Jetpack Compose: Botones, Estado e Interacción con la Interfaz de Usuario

**Fecha de entrega:** jueves 24 de septiembre de 2026, 10:00 p. m. (hora Colombia)
**Modalidad:** individual
**Medio de entrega:** enlace a repositorio público en GitHub

---

## 1. Objetivo de aprendizaje

Al finalizar este taller, el estudiante estará en capacidad de construir una aplicación Android con Jetpack Compose que reciba múltiples datos del usuario, procese esa información mediante reglas de negocio separadas de la interfaz, reaccione a cambios de estado y presente resultados desglosados de forma dinámica.

El taller se compone de dos partes: la primera consiste en completar dos recorridos guiados oficiales de Android; la segunda consiste en desarrollar una aplicación propia que integre los conceptos de ambos recorridos y los aplique a un problema real del contexto colombiano.

---

## 2. Parte 1 – Codelabs guiados (30 %)

Complete los dos recorridos oficiales. Se recomienda escribir el código en lugar de copiarlo, ya que el propósito es comprender cada instrucción.

### Codelab 1 – Agrega un botón a una app

Enlace: https://developer.android.com/courses/pathways/android-basics-compose-unit-2-pathway-2

Conceptos principales: creación de una aplicación interactiva (Dice Roller), uso del composable `Button` y su evento `onClick`, cambio de imágenes según el estado de la aplicación mediante `when`, y uso del depurador de Android Studio solo haz el punto 1 y 2 del codelab.

### Codelab 2 – Interactúa con la IU y el estado

Enlace: https://developer.android.com/courses/pathways/android-basics-compose-unit-2-pathway-3?hl=es-419

Conceptos principales: construcción de una calculadora de propinas (Tip Time), entrada de datos con `TextField`, estado en Compose con `remember` y `mutableStateOf`, elevación de estado (*state hoisting*), uso de `Switch` y configuración del teclado con `KeyboardOptions` para este ejercicio hacer los puntos 3 y 4.

### Evidencia requerida

Una captura de pantalla de cada aplicación funcionando (Dice Roller y Tip Time), en emulador o dispositivo físico. En cada captura debe verse el nombre completo del estudiante dentro de la aplicación; para ello, agregue o modifique un composable `Text` que lo muestre Adicional envie los links de Github con el codigo.

---

## 3. Parte 2 – Aplicación integradora: Calculadora de Nómina Colombiana (70 %)

### 3.1 Descripción general

El estudiante desarrollará una aplicación llamada **Calculadora de Nómina**, que permite a un trabajador colombiano estimar el pago mensual que recibirá. A partir de su salario básico y de las horas extra trabajadas en el mes, la aplicación calcula el valor de las horas extra, determina si tiene derecho al auxilio de transporte, aplica las deducciones de ley y muestra el salario neto a recibir, con un desglose de cada concepto.

Esta aplicación integra los dos codelabs de la siguiente manera:

| Concepto | Codelab de origen | Uso en la Calculadora de Nómina |
|----------|-------------------|---------------------------------|
| `Button` y evento `onClick` | Codelab 1 | Botones para calcular y para limpiar el formulario |
| Cambio de imagen con `when` | Codelab 1 | Imagen distinta según el rango salarial del trabajador |
| `TextField` y `KeyboardOptions` | Codelab 2 | Campos para salario, horas extra diurnas y horas extra nocturnas |
| `Switch` | Codelab 2 | Horas extra en domingo o festivo, y transporte suministrado por la empresa |
| Estado con `remember` y `mutableStateOf` | Codelabs 1 y 2 | Almacenamiento de entradas, interruptores, resultados y mensajes de error |
| Elevación de estado | Codelab 2 | Campo numérico y fila con interruptor reutilizables, sin estado propio |
| Formato de moneda | Codelab 2 | Presentación de todos los valores en pesos colombianos |

### 3.2 Reglas de negocio

**Aclaración importante:** para efectos del taller se utiliza un **modelo simplificado** de nómina, basado en la normativa vigente en septiembre de 2026. No incluye retención en la fuente, recargos nocturnos ordinarios, prestaciones sociales ni otros conceptos de una nómina real. Las reglas descritas en esta sección son las únicas que se deben implementar.

#### Constantes

Declare los siguientes valores como constantes (`const val`) en el código, con un comentario que indique la norma de la que provienen:

| Constante | Valor | Fuente |
|-----------|-------|--------|
| Salario mínimo mensual legal vigente (SMMLV) 2026 | $1.750.905 | Decreto 1469 de 2025 |
| Auxilio de transporte 2026 | $249.095 | Decreto 1470 de 2025 |
| Horas ordinarias mensuales | 210 | Jornada de 42 horas semanales (Ley 2101 de 2021), vigente desde el 15 de julio de 2026 |
| Porcentaje de aporte a salud (trabajador) | 4 % | Ley 100 de 1993 |
| Porcentaje de aporte a pensión (trabajador) | 4 % | Ley 100 de 1993 |
| Porcentaje del Fondo de Solidaridad Pensional | 1 % | Ley 797 de 2003 |

#### Regla 1 – Valor de la hora ordinaria

```
valorHora = salarioBasico / 210
```

#### Regla 2 – Valor de las horas extra

Cada tipo de hora extra se paga multiplicando el valor de la hora ordinaria por un factor. El factor depende de si las horas fueron trabajadas en un día hábil o en domingo o festivo:

| Tipo de hora extra | Día hábil | Domingo o festivo |
|--------------------|-----------|-------------------|
| Extra diurna | 1.25 (recargo del 25 %) | 2.15 (25 % extra + 90 % dominical) |
| Extra nocturna | 1.75 (recargo del 75 %) | 2.65 (75 % extra + 90 % dominical) |

```
pagoExtrasDiurnas   = horasDiurnas   × valorHora × factorDiurno
pagoExtrasNocturnas = horasNocturnas × valorHora × factorNocturno
totalHorasExtra     = pagoExtrasDiurnas + pagoExtrasNocturnas
```

Para simplificar, se asume que todas las horas extra del mes son del mismo tipo de día: o todas en día hábil, o todas en domingo o festivo.

#### Regla 3 – Ingreso Base de Cotización (IBC)

```
ibc = salarioBasico + totalHorasExtra
```

El auxilio de transporte **no** hace parte del IBC, porque no se considera salario.

#### Regla 4 – Auxilio de transporte

El trabajador recibe el auxilio de transporte si se cumplen **las dos** condiciones siguientes:
- Su salario básico es menor o igual a 2 SMMLV ($3.501.810).
- La empresa **no** le suministra el transporte.

En cualquier otro caso, el auxilio es $0.

#### Regla 5 – Total devengado

```
totalDevengado = ibc + auxilioTransporte
```

#### Regla 6 – Deducciones

```
aporteSalud   = ibc × 0.04
aportePension = ibc × 0.04
fondoSolidaridad = ibc × 0.01   (solo si ibc >= 4 SMMLV, es decir, $7.003.620; en otro caso es $0)
totalDeducciones = aporteSalud + aportePension + fondoSolidaridad
```

#### Regla 7 – Salario neto

```
salarioNeto = totalDevengado - totalDeducciones
```

#### Regla 8 – Rango salarial

Según el salario básico, el trabajador se clasifica en uno de tres rangos, que determinan la imagen mostrada en pantalla:

| Rango | Condición | Significado |
|-------|-----------|-------------|
| Rango 1 | Salario menor o igual a 2 SMMLV | Puede recibir auxilio de transporte |
| Rango 2 | Salario mayor que 2 SMMLV y menor que 4 SMMLV | Sin auxilio de transporte ni Fondo de Solidaridad |
| Rango 3 | Salario mayor o igual a 4 SMMLV | Aporta al Fondo de Solidaridad Pensional |

#### Restricciones de validación

- El salario básico es obligatorio y no puede ser inferior a un SMMLV.
- Las horas extra son opcionales; si el campo está vacío se toma como 0.
- Las horas extra no pueden ser negativas.
- La suma de horas extra diurnas y nocturnas no puede superar 48 en el mes (la ley permite máximo 12 horas extra semanales; para el taller se toman 4 semanas).

### 3.3 Funcionalidades de la aplicación

A continuación se describe cada funcionalidad que debe implementar la aplicación, junto con su comportamiento esperado y los elementos técnicos involucrados.

#### F1. Ingreso del salario básico

**Descripción:** la aplicación muestra un campo de texto donde el trabajador escribe su salario básico mensual.

**Comportamiento esperado:**
- La etiqueta del campo es "Salario básico mensual".
- Al tocar el campo, se abre un teclado numérico.
- La tecla de acción del teclado es "Siguiente" y, al presionarla, el foco pasa al campo de horas extra diurnas.

**Elementos técnicos:** `TextField` (o `OutlinedTextField`), `KeyboardOptions` con `keyboardType = KeyboardType.Number` e `imeAction = ImeAction.Next`, y una variable de estado de tipo `String` declarada con `remember { mutableStateOf("") }`.

#### F2. Ingreso de horas extra diurnas

**Descripción:** un segundo campo de texto permite ingresar la cantidad de horas extra diurnas trabajadas en el mes.

**Comportamiento esperado:**
- La etiqueta es "Horas extra diurnas".
- Teclado numérico con acción "Siguiente".
- Si el usuario deja el campo vacío, la aplicación lo interpreta como 0 horas.

**Elementos técnicos:** los mismos de F1.

#### F3. Ingreso de horas extra nocturnas

**Descripción:** un tercer campo de texto permite ingresar la cantidad de horas extra nocturnas trabajadas en el mes.

**Comportamiento esperado:**
- La etiqueta es "Horas extra nocturnas".
- Teclado numérico con acción "Listo"; al presionarla, se cierra el teclado.
- Si el campo está vacío, se interpreta como 0 horas.

**Elementos técnicos:** los mismos de F1, con `imeAction = ImeAction.Done`.

**Requisito de diseño para F1, F2 y F3:** los tres campos deben construirse con **un mismo composable reutilizable y sin estado propio**, que reciba como parámetros la etiqueta, el valor actual, el callback `onValueChange` y las opciones de teclado. El estado debe vivir en el composable padre. Esta es la aplicación directa de la elevación de estado vista en el Codelab 2.

Firma sugerida:

```kotlin
@Composable
fun CampoNumerico(
    @StringRes etiqueta: Int,
    valor: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
)
```

#### F4. Interruptor de domingo o festivo

**Descripción:** un `Switch` permite indicar si las horas extra del mes fueron trabajadas en domingo o festivo.

**Comportamiento esperado:**
- Por defecto está desactivado, lo que significa que las horas extra se liquidan como de día hábil.
- Al activarlo, se aplican los factores de domingo o festivo de la Regla 2.
- Junto al `Switch` debe aparecer un texto que explique su función, por ejemplo: "Horas extra en domingo o festivo".
- Si ya existe un resultado en pantalla, este se recalcula automáticamente al cambiar el interruptor.

**Elementos técnicos:** `Switch` y una variable de estado de tipo `Boolean`.

#### F5. Interruptor de transporte suministrado por la empresa

**Descripción:** un segundo `Switch` permite indicar si la empresa le suministra el transporte al trabajador.

**Comportamiento esperado:**
- Por defecto está desactivado.
- Si está activado, el trabajador no recibe auxilio de transporte, sin importar su salario (Regla 4).
- Junto al `Switch` aparece el texto "La empresa suministra el transporte".
- Si ya existe un resultado en pantalla, este se recalcula automáticamente.

**Elementos técnicos:** `Switch` y una variable de estado de tipo `Boolean`.

**Requisito de diseño para F4 y F5:** ambos interruptores deben construirse con **un mismo composable reutilizable sin estado propio** (por ejemplo, `FilaInterruptor`), que reciba la etiqueta, el valor actual y el callback `onCheckedChange`. Es el mismo patrón que `RoundTheTipRow` del Codelab 2.

#### F6. Cálculo de la nómina

**Descripción:** un botón "Calcular" ejecuta el cálculo completo aplicando las Reglas 1 a 7.

**Comportamiento esperado:**
- Al presionar el botón, la aplicación convierte los textos a números, valida los datos (ver F10) y, si son válidos, calcula todos los conceptos de la nómina.
- El cálculo **no** debe hacerse dentro del composable. Debe estar en una función independiente de la interfaz, ubicada en un archivo aparte, de modo que la lógica de negocio quede separada de la presentación.
- Se recomienda que la función devuelva un objeto con todos los valores calculados, en lugar de un solo número.

**Elementos técnicos:** `Button` con su evento `onClick`, una `data class` para el resultado y una función de cálculo separada.

Estructura sugerida:

```kotlin
data class ResultadoNomina(
    val valorHora: Double,
    val totalHorasExtra: Double,
    val auxilioTransporte: Double,
    val totalDevengado: Double,
    val aporteSalud: Double,
    val aportePension: Double,
    val fondoSolidaridad: Double,
    val totalDeducciones: Double,
    val salarioNeto: Double
)

fun calcularNomina(
    salarioBasico: Double,
    horasDiurnas: Double,
    horasNocturnas: Double,
    esDominical: Boolean,
    transporteEmpresa: Boolean
): ResultadoNomina
```

#### F7. Desglose de la nómina

**Descripción:** después de calcular, la aplicación muestra el detalle de cada concepto en un bloque de resultados.

**Comportamiento esperado:** se muestran, en este orden, las siguientes líneas:

| Sección | Concepto |
|---------|----------|
| Devengado | Valor hora ordinaria |
| Devengado | Salario básico |
| Devengado | Horas extra |
| Devengado | Auxilio de transporte |
| Devengado | **Total devengado** |
| Deducciones | Salud (4 %) |
| Deducciones | Pensión (4 %) |
| Deducciones | Fondo de Solidaridad Pensional (1 %) |
| Deducciones | **Total deducciones** |
| Resultado | **Salario neto a recibir** (resaltado con mayor tamaño de letra) |

- Los conceptos que valen $0 se muestran igualmente, para que el usuario entienda por qué no aplican.
- Antes del primer cálculo, el bloque de resultados no se muestra.

**Elementos técnicos:** composables `Text`, `Row` o `Column`, y una variable de estado que almacene el `ResultadoNomina` (puede ser de tipo `ResultadoNomina?`, iniciando en `null`).

#### F8. Formato de moneda

**Descripción:** todos los valores monetarios se muestran en formato de pesos colombianos.

**Comportamiento esperado:** los valores se presentan redondeados sin decimales y con separador de miles, por ejemplo: `$ 2.009.505`.

**Elementos técnicos:** `NumberFormat.getCurrencyInstance(Locale("es", "CO"))`, de forma similar al formateo de la propina en el Codelab 2. Configure el formato para no mostrar decimales con `maximumFractionDigits = 0`.

#### F9. Imagen según el rango salarial

**Descripción:** la aplicación muestra una imagen distinta para cada uno de los tres rangos salariales de la Regla 8, de la misma forma en que Dice Roller muestra una cara diferente del dado según el número obtenido.

**Comportamiento esperado:**
- Antes del primer cálculo no se muestra imagen, o se muestra una imagen neutral.
- Después de calcular, se muestra la imagen del rango correspondiente, acompañada de un texto corto que lo explique (por ejemplo: "Rango 1: con derecho a auxilio de transporte").
- Cada imagen tiene un `contentDescription` descriptivo tomado de `strings.xml`, por accesibilidad.
- La clasificación del rango debe hacerse en una función separada, implementada con una expresión `when`.

**Elementos técnicos:** composable `Image`, recursos en `res/drawable` (íconos vectoriales de Material o imágenes libres de derechos), una clase `enum` para los rangos y una expresión `when` que asocie cada rango con su recurso.

Estructura sugerida:

```kotlin
enum class RangoSalarial { RANGO_1, RANGO_2, RANGO_3 }

fun clasificarRango(salarioBasico: Double): RangoSalarial
```

#### F10. Validación de datos de entrada

**Descripción:** la aplicación valida los datos antes de calcular y nunca debe cerrarse inesperadamente, sin importar lo que escriba el usuario.

**Comportamiento esperado:** si alguna restricción de la sección 3.2 no se cumple, no se realiza el cálculo y se muestra un mensaje específico:

| Situación | Mensaje sugerido |
|-----------|------------------|
| Salario vacío o no numérico | "Ingrese un salario válido" |
| Salario inferior al SMMLV | "El salario no puede ser inferior al mínimo ($ 1.750.905)" |
| Horas extra negativas o no numéricas | "Las horas extra deben ser un número mayor o igual a cero" |
| Suma de horas extra mayor que 48 | "El total de horas extra no puede superar 48 en el mes" |

- Se debe marcar con error el campo afectado usando el parámetro `isError` del `TextField`.
- Al corregir el dato y volver a calcular, el mensaje desaparece.

**Elementos técnicos:** conversión segura con `toDoubleOrNull()`, operador Elvis (`?:`), lógica condicional y una variable de estado para el mensaje de error.

#### F11. Limpieza del formulario

**Descripción:** un botón "Limpiar" devuelve la aplicación a su estado inicial.

**Comportamiento esperado:** se vacían los tres campos de texto, los dos interruptores vuelven a desactivado, desaparecen el desglose, la imagen y cualquier mensaje de error.

**Elementos técnicos:** `Button` (o `OutlinedButton`) cuyo evento `onClick` reinicia todas las variables de estado.

#### F12. Recursos de texto y vista previa

**Descripción:** todos los textos visibles de la aplicación deben estar en `res/values/strings.xml`, y el proyecto debe tener una vista previa funcional.

**Comportamiento esperado:**
- No existe ningún texto escrito directamente dentro del código Kotlin de la interfaz; se usa `stringResource(R.string.nombre)`.
- Existe al menos una función con la anotación `@Preview` que muestra la pantalla principal con el tema de la aplicación.

**Requisito adicional:** dado que la pantalla tiene muchos elementos, la columna principal debe permitir desplazamiento vertical con `Modifier.verticalScroll(rememberScrollState())`, como en el Codelab 2.

### 3.4 Casos de verificación

Utilice los siguientes casos para comprobar manualmente que su aplicación calcula correctamente. Los valores están redondeados al peso más cercano; se aceptan diferencias de hasta $1 por redondeo.

**Caso A – Salario mínimo con horas extra en día hábil**

Entradas: salario $1.750.905; 10 horas extra diurnas; 4 horas extra nocturnas; domingo o festivo desactivado; transporte de la empresa desactivado.

| Concepto | Valor esperado |
|----------|----------------|
| Valor hora ordinaria | $ 8.338 |
| Horas extra | $ 162.584 |
| Auxilio de transporte | $ 249.095 |
| Total devengado | $ 2.162.584 |
| Salud | $ 76.540 |
| Pensión | $ 76.540 |
| Fondo de Solidaridad | $ 0 |
| Salario neto | $ 2.009.505 |
| Rango | Rango 1 |

**Caso B – Horas extra en domingo o festivo**

Entradas: salario $2.500.000; 6 horas extra diurnas; 2 horas extra nocturnas; domingo o festivo activado; transporte de la empresa desactivado.

| Concepto | Valor esperado |
|----------|----------------|
| Valor hora ordinaria | $ 11.905 |
| Horas extra | $ 216.667 |
| Auxilio de transporte | $ 249.095 |
| Total devengado | $ 2.965.762 |
| Salud | $ 108.667 |
| Pensión | $ 108.667 |
| Fondo de Solidaridad | $ 0 |
| Salario neto | $ 2.748.428 |
| Rango | Rango 1 |

**Caso C – Transporte suministrado por la empresa**

Entradas: salario $3.000.000; 5 horas extra diurnas; 0 horas extra nocturnas; domingo o festivo desactivado; transporte de la empresa activado.

| Concepto | Valor esperado |
|----------|----------------|
| Valor hora ordinaria | $ 14.286 |
| Horas extra | $ 89.286 |
| Auxilio de transporte | $ 0 |
| Total devengado | $ 3.089.286 |
| Salud | $ 123.571 |
| Pensión | $ 123.571 |
| Fondo de Solidaridad | $ 0 |
| Salario neto | $ 2.842.143 |
| Rango | Rango 1 |

**Caso D – Salario alto con Fondo de Solidaridad Pensional**

Entradas: salario $8.000.000; sin horas extra; ambos interruptores desactivados.

| Concepto | Valor esperado |
|----------|----------------|
| Valor hora ordinaria | $ 38.095 |
| Horas extra | $ 0 |
| Auxilio de transporte | $ 0 |
| Total devengado | $ 8.000.000 |
| Salud | $ 320.000 |
| Pensión | $ 320.000 |
| Fondo de Solidaridad | $ 80.000 |
| Salario neto | $ 7.280.000 |
| Rango | Rango 3 |

**Caso E – Validaciones**

| Entrada | Resultado esperado |
|---------|--------------------|
| Salario vacío | Mensaje de error, sin cálculo |
| Salario $1.000.000 | Mensaje de salario inferior al mínimo |
| Salario $2.000.000 con 30 horas diurnas y 20 nocturnas | Mensaje de exceso de horas extra |
| Salario $2.000.000 con horas extra vacías | Cálculo correcto con 0 horas extra |

### 3.5 Estructura sugerida del código

```
app/src/main/java/.../calculadoranomina/
    MainActivity.kt          -> Actividad, pantalla principal y composables reutilizables
    NominaCalculator.kt      -> Constantes, data class ResultadoNomina, enum RangoSalarial,
                                funciones calcularNomina y clasificarRango
app/src/main/res/
    drawable/                -> Imágenes de los tres rangos salariales
    values/strings.xml       -> Todos los textos de la aplicación
```

### 3.6 Funcionalidades opcionales (hasta 0.5 puntos adicionales)

- Tema de colores propio y diseño visual cuidado (alineación, espaciado, jerarquía tipográfica).
- Soporte correcto de modo oscuro.
- Rotación de pantalla sin pérdida de los datos ingresados, usando `rememberSaveable`.
- Mostrar, como información adicional, cuánto representa el salario neto en número de salarios mínimos (por ejemplo, "Equivale a 1.15 SMMLV").

---

## 4. Entrega

La entrega se realiza mediante el **enlace a un repositorio público en GitHub**. El repositorio debe contener:

1. **El proyecto completo de Android Studio** de la Calculadora de Nómina, que compile sin errores.
2. **Un archivo `README.md`** en la raíz del repositorio con:
   - Nombre completo y código del estudiante.
   - Descripción breve de la aplicación.
   - Capturas de pantalla de la aplicación mostrando los resultados de los casos A, B, C y D de la sección 3.4.
   - Al menos una captura de un mensaje de validación del caso E.
3. **Una carpeta `evidencias/`** con las capturas de la Parte 1 (Dice Roller y Tip Time), con el nombre del estudiante visible.

**Condiciones de la entrega:**
- La hora límite es las **10:00 p. m. del 24 de septiembre de 2026**. Se evaluará el último *commit* realizado antes de esa hora; los *commits* posteriores no serán tenidos en cuenta.
- Se recomienda realizar *commits* frecuentes durante el desarrollo, con mensajes descriptivos. El historial de *commits* hace parte de la evidencia del trabajo realizado.
- El repositorio debe ser público o, en su defecto, compartido con el docente antes de la hora límite.
- Se permite consultar documentación oficial y herramientas de inteligencia artificial. Sin embargo, el estudiante debe estar en capacidad de explicar cualquier parte de su código si se le solicita en la siguiente sesión de clase.

---

## 5. Rúbrica de evaluación

| Criterio | Peso |
|----------|------|
| Parte 1: evidencias de ambos codelabs con el nombre del estudiante visible | 30 % |
| Funcionalidades F1 a F12 implementadas según lo descrito | 40 % |
| Correctitud de los cálculos frente a los casos de verificación A a D | 15 % |
| Validaciones y estabilidad de la aplicación (caso E, sin cierres inesperados) | 10 % |
| Calidad del repositorio: README, capturas e historial de *commits* | 5 % |
| Funcionalidades opcionales | hasta +0.5 |

---

## 6. Recomendaciones para el desarrollo

1. Comience por el archivo `NominaCalculator.kt`: constantes, `data class`, `enum` y las funciones `calcularNomina` y `clasificarRango`. Esta es la parte central del taller.
2. Construya una interfaz mínima con el campo de salario y el botón "Calcular", y compruebe el caso D, que no tiene horas extra.
3. Agregue los campos de horas extra y compruebe el caso A.
4. Agregue los interruptores y compruebe los casos B y C.
5. Agregue la imagen por rango, el botón de limpieza y las validaciones del caso E.
6. Deje el diseño visual y las funcionalidades opcionales para el final.
7. Realice un *commit* cada vez que complete un paso, de modo que siempre tenga una versión estable a la cual regresar.

**Nota:** esta aplicación tiene fines exclusivamente académicos. El modelo de cálculo es simplificado y no reemplaza la liquidación de nómina realizada por un empleador ni la asesoría de un profesional contable o laboral.
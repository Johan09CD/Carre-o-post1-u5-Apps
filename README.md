# RetrofitLab — Carreño Post1 U5

Aplicación Android desarrollada para la **Unidad 5: Consumo de Servicios y Comunicación con Backend** de la asignatura Aplicaciones Móviles — Ingeniería de Sistemas, Universidad de Santander (UDES) 2026.

## Descripción

La app consume la API pública [JSONPlaceholder](https://jsonplaceholder.typicode.com) para mostrar una lista de posts con paginación simulada, gestión de estados de UI y manejo tipado de errores de red.

---

## Requisitos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17 o superior
- Android 8.0+ (API 26+)
- Conexión a internet activa

---

## Configuración y ejecución

1. Clona el repositorio:
```bash
git clone https://github.com/Johan09CD/Carre-o-post1-u5-Apps
```

2. Abre el proyecto en Android Studio

3. Espera que Gradle sincronice las dependencias

4. Ejecuta la app en un emulador o dispositivo físico con API 26+

---

## Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Retrofit 2.9.0 | Cliente HTTP para consumir la API REST |
| OkHttp 4.12.0 | Cliente HTTP base con interceptors |
| HttpLoggingInterceptor | Logging de requests y responses en Logcat |
| kotlinx.serialization | Deserialización de DTOs desde JSON |
| Kotlin Coroutines | Llamadas asíncronas a la API |
| StateFlow | Manejo reactivo de estados de UI |
| Jetpack Compose | Interfaz de usuario declarativa |
| ViewModel | Separación de lógica de presentación |

---

## Arquitectura

El proyecto sigue la arquitectura **MVVM** con separación en capas:

```
com.udes.retrofitlab
├── data
│   ├── remote
│   │   ├── api         # Interfaz PostApi (Retrofit)
│   │   └── dto         # PostDto + mapper toDomain()
│   └── repository      # PostRepository
├── di
│   └── NetworkModule   # Configuración de OkHttp y Retrofit
├── domain
│   ├── error           # AppError (sealed class) + mappers
│   └── model           # Post (modelo de dominio)
└── presentation
    ├── ui              # PostsScreen + PostCard (Compose)
    └── viewmodel       # PostsViewModel + PostsUiState
```

---

## Decisiones de diseño

### Interceptor de autenticación
Se configuró un interceptor en OkHttp que agrega automáticamente los headers `Accept: application/json` y `X-App-Version: 1.0.0` a cada request, simulando el flujo de autenticación que tendría una app real con tokens.

### Mapeo de errores con sealed class
Se usó `AppError` como sealed class para tipar todos los posibles errores de red. Esto permite manejar cada caso de forma exhaustiva en el `when` del ViewModel, evitando errores silenciosos y mostrando mensajes claros al usuario según el tipo de fallo (red, 401, 404, 500, etc.).

### DTO vs Modelo de dominio
Se separó `PostDto` (capa de datos) del modelo `Post` (capa de dominio) para desacoplar la app de la estructura del JSON. El mapper `toDomain()` transforma el body completo en un excerpt de máximo 100 caracteres, limpiando los datos antes de llegar a la UI.

---

## Estados de la UI

La app maneja 4 estados mediante `PostsUiState`:

| Estado | Descripción |
|---|---|
| `Loading` | Muestra un `CircularProgressIndicator` mientras carga |
| `Success` | Muestra la lista de posts con botón "Cargar más" |
| `Error` | Muestra el mensaje de error con botón "Reintentar" |
| `Empty` | Muestra mensaje cuando la API no retorna posts |

---

## Capturas de pantalla

### Lista de posts cargada
![Lista cargada](capturas/screenshot_success.png)

---

## Flujo implementado

1. `MainActivity` inicializa `PostsScreen`
2. `PostsScreen` observa el `StateFlow` del `PostsViewModel`
3. Al iniciar, `PostsViewModel` llama a `PostRepository.getPosts(page=1)`
4. `PostRepository` ejecuta la llamada con Retrofit y mapea los DTOs a modelos de dominio
5. Si la llamada es exitosa, el estado cambia a `Success` con la lista de posts
6. Si falla, el `Throwable` se convierte a `AppError` mediante `toAppError()` y el estado cambia a `Error` con el mensaje correspondiente
7. Al presionar "Cargar más", se incrementa la página y se agregan los nuevos posts a la lista existente

---

## Autor

**Johan Carreño** — Ingeniería de Sistemas, UDES 2026
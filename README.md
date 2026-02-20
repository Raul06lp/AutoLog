# 🔧 AutoLog

**Plataforma integral de gestión para talleres mecánicos**

AutoLog es una solución multiplataforma que digitaliza el ciclo de vida completo de un vehículo en un taller, desde su recepción hasta la entrega final, ofreciendo trazabilidad total tanto para el mecánico como para el cliente.

> Proyecto de DAM — IES Abastos · Curso 2025/26 · Grupo 7DMM  
> **Carla Fernández · Raúl Lavara · Brandon Quispe**

---

## 📦 Estructura del proyecto

```
AutoLog/
├── BackEnd/          # API REST en Spring Boot (Java)
├── Android/          # App móvil en Kotlin (Clean Architecture)
└── Electron/         # App de escritorio en Electron + Node.js
```

---

## 🚀 Tecnologías utilizadas

| Capa | Tecnología |
|------|-----------|
| Backend | Java · Spring Boot · Maven · Spring Security |
| Base de datos | MySQL · JPA / Hibernate |
| Despliegue | Render · UptimeRobot |
| App móvil | Kotlin · Jetpack Compose · Navigation3 · Retrofit · OkHttp · Koin · Coroutines · DataStore |
| App escritorio | Electron · Node.js · HTML · CSS · JavaScript |
| Documentación API | Swagger / OpenAPI |

---

## 🌐 API REST

La API está desplegada en Render y es accesible públicamente:

- **Base URL:** `https://autolog-0mnd.onrender.com`
- **Autenticación:** HTTP Basic
- **Documentación interactiva (Swagger):** [`/swagger-ui/index.html`](https://autolog-0mnd.onrender.com/swagger-ui/index.html)

### Endpoints principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/mecanicos/login` | Login de mecánico |
| `POST` | `/api/clientes/login` | Login de cliente |
| `POST` | `/api/mecanicos/registro` | Registro de mecánico |
| `POST` | `/api/clientes/registro` | Registro de cliente |
| `GET` | `/api/vehiculos/mecanico/{id}` | Vehículos de un mecánico |
| `GET` | `/api/vehiculos/cliente/{id}` | Vehículos de un cliente |
| `POST` | `/api/vehiculos/con-imagen` | Crear vehículo con imagen |
| `PUT` | `/api/vehiculos/{id}` | Actualizar vehículo |
| `PATCH` | `/api/vehiculos/{id}/estado` | Cambiar estado de revisión |
| `GET` | `/health` | Health check del servidor |

---

## 📱 App móvil (Android)

Desarrollada en Kotlin siguiendo **Clean Architecture** con tres capas: Presentación, Dominio y Datos. El patrón de presentación utilizado es **MVI**, con ViewModels que gestionan un estado único e inmutable.

**Funcionalidades:**
- Login y registro (mecánico / cliente)
- Listado de vehículos filtrado por usuario
- Detalle del vehículo con historial
- Añadir y editar vehículos
- Cambio de estado de revisión
- Gestión de perfil de usuario
- Logout seguro

---

## 🖥️ App de escritorio (Electron)

Desarrollada con **Electron y Node.js**, orientada a la gestión completa del taller desde un ordenador.

**Funcionalidades:**
- Login y registro (mecánico / cliente)
- CRUD completo de vehículos
- Generación de informes en PDF
- Visualización del estado de los vehículos por cliente

---

## 🗄️ Base de datos

La base de datos cuenta con 4 tablas generadas automáticamente por Hibernate:

| Tabla | Descripción |
|-------|-------------|
| `mecanicos` | Datos de los mecánicos registrados |
| `clientes` | Datos de los clientes registrados |
| `vehiculos` | Expediente digital de cada vehículo |
| `app_users` | Usuarios de acceso a la API (Spring Security) |

---

## 🔄 Estados de revisión

Los vehículos pasan por los siguientes estados a lo largo de su ciclo en el taller:

```
pendiente  →  en_revision  →  finalizado
```

---

## 👤 Usuarios de prueba

Para probar la aplicación sin necesidad de registrarse:

| Rol | Email | Contraseña |
|-----|-------|-----------|
| Mecánico | `mecanico@gmail.com` | `mecanico` |
| Cliente | `cliente@gmail.com` | `cliente` |

---

## ⚙️ Instalación y ejecución local

### Backend
```bash
cd BackEnd
./mvnw spring-boot:run
```
> Requiere una base de datos MySQL configurada en `application.properties`.

### App Android
Abre la carpeta `Android/` con **Android Studio** y ejecuta el proyecto en un emulador o dispositivo físico.

### App Electron
```bash
cd Electron
npm install
npm start
```

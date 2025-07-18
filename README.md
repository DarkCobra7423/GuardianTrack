<p align="center">
  <img src="/docs/GuardianTrack-logo.svg1" alt="GuardianTrack" height="200px" />
</p>

# 🛡️ GuardianTrack  
### **Tecnología al servicio de los desaparecidos**

> **GuardianTrack** es una plataforma comunitaria de prevención y respuesta ante desapariciones.

---

## 🚀 Funcionalidades Clave

### 📍 Geolocalización Continua y Segura  
- Seguimiento pasivo en segundo plano (modo de bajo consumo).  
- Registro automático de las últimas 10 ubicaciones (una cada 10 minutos), accesibles solo por usuarios autorizados.

### 🛡️ Zonas Seguras Personalizadas  
- Define áreas seguras (hogar, escuela, trabajo).  
- Si se detecta una salida sin autorización, se solicita un **código de verificación personal**.  

### 🔐 Código de Verificación Inteligente  
- Si no se ingresa el código en un plazo de 5 minutos:  
  - Se activa una **prealerta privada** a contactos de confianza.  
  - Se emite una **alerta comunitaria inmediata** a usuarios Guardian en un radio de 10 km.  
  - El evento se guarda y se monitorea en tiempo real.

### 🚨 Botón de Pánico  
- Al presionar, se envía una alerta urgente con ubicación y datos clave a:  
  - Contactos de confianza  
  - Comunidad Guardian cercana  

### 🧩 Boletines de Búsqueda y Reportes de Avistamiento  
- Subida rápida de reportes: personas vistas, pertenencias, hallazgos o señales.  
- Publicación opcional y automática en redes sociales con formato adaptado para búsqueda.

### 🔒 Privacidad Reforzada  
- CURP se utiliza exclusivamente para validar alertas.  
- Datos personales encriptados y accesibles **solo** para autoridades verificadas y contactos designados.  
- Las alertas comunitarias **nunca revelan datos personales** ni identificadores oficiales.

### 🌍 Mapa de Calor Interactivo  
- Visualiza zonas de alto riesgo, reportes recientes y hallazgos confirmados.

### 📬 Canal Anónimo para Testimonios  
- Recibe pistas, reportes o información relevante sin necesidad de revelar identidad.

### 📲 Difusión Automatizada en Redes  
- Permite compartir reportes o alertas en redes sociales de manera instantánea y controlada.

---

## ✨ Módulos Principales

- 🛰️ **Módulo de Geolocalización** – Gestión, historial y seguimiento pasivo de ubicación.  
- 🔐 **Módulo de Autenticación y Código Seguro** – Verificación personal, alertas automáticas, contactos de confianza.  
- 📡 **Módulo de Alertas** – Botón de pánico, alertas comunitarias, notificaciones multicanal.  
- 🧾 **Módulo de Reportes y Avistamientos** – Formulario de registro, carga de imágenes, validación cruzada.  
- 🗺️ **Módulo de Visualización** – Mapas de calor, zonas de riesgo, rutas recientes.  
- 📬 **Módulo de Comunicación Anónima** – Pistas y testimonios encriptados.  
- 🤖 **Módulo de IA (futuro)** – Análisis de patrones, correlación de reportes, búsqueda inteligente.

---

## 🧑‍🤝‍🧑 ¿A quién está dirigido?

- 🧭 Colectivos de búsqueda  
- 👨‍👩‍👧 Familiares de personas desaparecidas  
- 🌐 ONGs y organizaciones humanitarias  
- 📰 Periodistas e investigadores  
- 🧑‍🏫 Comunidad académica y estudiantil  
- 👥 Público general comprometido con la causa

---

## 🧰 Tecnologías sugeridas

| Componente             | Tecnología Propuesta                              |
| ---------------------- | ------------------------------------------------- |
| **App Móvil (actual)** | Android (Kotlin / Java) – Android Studio          |
| **App Móvil (futuro)** | iOS (Swift) – Xcode                               |
| **Backend API**        | NodeJS / Spring Boot / Python (FastAPI)           |
| **Base de datos**      | MongoDB Atlas (nube) / MongoDB local              |
| **Geolocalización**    | Android Location Services / Google Maps SDK       |
| **Infraestructura**    | Firebase (Auth, FCM) / VPS + Docker               |
| **IA opcional**        | TensorFlow Lite (para análisis en el dispositivo) |

---

## ⚙️ Requisitos mínimos (Android)

 - Android Studio Flamingo o superior
 - SDK de Android 24+ (Android 7.0+)
 - Kotlin 1.9+ o Java 17
 - Acceso a MongoDB Atlas o servidor NodeJS/API
 - Firebase opcional para autenticación y notificaciones
 - Permisos de ubicación y notificaciones activados

---

## Estructura de carpetas para Android Studio (Kotlin)

~~~
GuardianTrack/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/guardiantrack/
│   │   │   │   ├── data/           # Repositorios, modelos, DB
│   │   │   │   ├── ui/             # Pantallas (fragments/activities)
│   │   │   │   ├── utils/          # Funciones generales
│   │   │   │   ├── network/        # API calls (Retrofit, etc.)
│   │   │   ├── res/
│   │   │   │   ├── layout/         # XMLs
│   │   │   │   ├── drawable/
│   │   │   │   ├── values/
├── build.gradle
└── README.md
~~~

## APIs clave para funciones

| Funcionalidad                | API o Herramienta Recomendada          |
| ---------------------------- | -------------------------------------- |
| Geolocalización continua     | `FusedLocationProviderClient` (Google) |
| Zonas seguras / geofencing   | `GeofencingClient`                     |
| Notificaciones push          | Firebase Cloud Messaging (FCM)         |
| Mapa interactivo             | Google Maps SDK for Android            |
| Almacenamiento local cifrado | EncryptedSharedPreferences / Room      |
| Envío de datos / alertas     | Retrofit o Ktor (cliente HTTP)         |
| Comunicación con backend     | REST API (NodeJS + MongoDB)            |


## ⚖️ Ética y principios

🔒 **GuardianTrack respeta la privacidad, dignidad y memoria de las víctimas.**  
🧑‍⚕️ **No reemplaza el trabajo de peritos forenses.**  
🤝 Su objetivo es **complementar, no interferir** con los canales oficiales, brindando apoyo civil y humanitario basado en tecnología responsable.

---

## 🧪 Estado actual del proyecto

📍 *Fase conceptual* – Sin código implementado.  
🧭 Este repositorio está destinado al diseño, planeación y estructura inicial.  
🤝 ¿Quieres colaborar? ¡Contáctanos!

> Especialmente si tienes experiencia con colectivos, UX en contextos de emergencia o análisis forense digital.

---

## 🧭 Casos de uso

- **Carlos activa el botón de pánico** tras notar que lo siguen. Sus contactos y usuarios cercanos reciben su ubicación.  
- **Ana no responde a su código de verificación** tras salir de su zona segura. Se lanza una prealerta automática.  
- **Una persona anónima reporta un avistamiento** en el mapa con una fotografía y descripción del lugar.

---


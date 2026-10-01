# 📦 StockApp

StockApp es una aplicación móvil para Android enfocada en la **gestión y control de inventario**.

El proyecto está siendo desarrollado como práctica para aplicar conceptos de desarrollo móvil moderno utilizando **Kotlin y Jetpack Compose**, incluyendo diseño de interfaces, navegación, validación de formularios y, posteriormente, persistencia de datos y conexión con servicios externos.

> 🚧 **Proyecto en desarrollo:** actualmente se está construyendo la estructura inicial y el módulo de autenticación.

---

## ✨ Funcionalidades actuales

### 🔐 Inicio de sesión

- Campo de correo electrónico.
- Campo de contraseña.
- Mostrar y ocultar contraseña.
- Validación de correo electrónico.
- Validación de contraseña.
- Acceso al registro de usuarios.
- Acceso a recuperación de contraseña.

### 👤 Registro

- Nombre completo.
- Correo electrónico.
- Contraseña.
- Confirmación de contraseña.
- Validación de los datos ingresados.
- Verificación de coincidencia entre contraseñas.
- Mostrar y ocultar contraseñas.

### 🔑 Recuperación de contraseña

- Captura del correo electrónico.
- Validación del formato del correo.
- Navegación de regreso al inicio de sesión.

> Por el momento, la recuperación de contraseña corresponde únicamente a la interfaz y sus validaciones. El envío real de instrucciones se implementará posteriormente junto con el sistema de autenticación.

### 🧭 Navegación

Actualmente la aplicación permite navegar entre:

```text
                Login
               /     \
              /       \
        Registro     Recuperar
            \          /
             \        /
                Login
```

La navegación está implementada utilizando **Navigation Compose**.

---

## 🛠️ Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material Design 3
- Navigation Compose
- Gradle
- Android Studio
- Git
- GitHub

---

## 🗂️ Estructura actual

```text
com.minidany.stockapp
│
├── MainActivity.kt
│
└── ui
    │
    ├── navigation
    │   └── AppNavigation.kt
    │
    ├── screens
    │   ├── LoginScreen.kt
    │   ├── RegisterScreen.kt
    │   └── ForgotPasswordScreen.kt
    │
    ├── theme
    │
    └── utils
        └── ValidationUtils.kt
```

### `screens`

Contiene las diferentes interfaces de usuario de la aplicación.

### `navigation`

Controla la navegación entre las diferentes pantallas mediante `NavHost` y `NavController`.

### `utils`

Contiene funciones reutilizables, como las validaciones de correo electrónico, contraseña y otros campos.

### `theme`

Contiene la configuración visual de Material Design utilizada por StockApp.

---

## 🚀 Próximas funcionalidades

El desarrollo continuará incorporando gradualmente:

- 🏠 Pantalla principal.
- 📦 Gestión de productos.
- 📊 Control de inventario.
- ➕ Registro de entradas y salidas.
- 👥 Gestión de usuarios.
- 🚚 Gestión de proveedores.
- 🔐 Autenticación real de usuarios.
- 💾 Persistencia de datos.
- 🌐 Conexión con API REST.
- 📷 Posible integración con códigos de barras.

---

## 🎯 Objetivo

El objetivo de StockApp es construir progresivamente una aplicación de inventario mientras se aplican buenas prácticas de desarrollo Android.

El proyecto busca separar correctamente responsabilidades como:

- Interfaz de usuario.
- Navegación.
- Validación de datos.
- Estado de la aplicación.
- Lógica de negocio.
- Acceso a datos.

---

## 📱 Estado del proyecto

```text
[██████░░░░░░░░░░░░░░] En desarrollo
```

### Completado

- [x] Diseño de Login
- [x] Diseño de Registro
- [x] Diseño de Recuperar contraseña
- [x] Validaciones del Login
- [x] Validaciones del Registro
- [x] Validación de recuperación de contraseña
- [x] Navegación inicial

### Pendiente

- [ ] Pantalla principal
- [ ] Arquitectura de la aplicación
- [ ] Gestión de inventario
- [ ] Persistencia de datos
- [ ] Autenticación real
- [ ] API REST
- [ ] Funcionalidades avanzadas

---

## 👨‍💻 Autor

Desarrollado por **Daniel González Hernández**.

Proyecto desarrollado con fines académicos y de aprendizaje de desarrollo móvil Android.

---

<p align="center">
  <b>📦 StockApp</b><br>
  Gestiona tu inventario de forma sencilla.
</p>

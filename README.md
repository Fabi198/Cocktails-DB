<div align="center">
  <div style="display: inline-flex; align-items: center; gap: 10px;">
    <img src="https://raw.githubusercontent.com/Fabi198/fabi198.github.io/main/assets/db_cocktails/logo_main.webp"
         width="60" />
    <h1 style="margin: 0;">CocktailsDB App</h1>
  </div>
</div>

Aplicación Android moderna desarrollada con **Jetpack Compose** que consume datos de cócteles desde una API y los presenta en una interfaz limpia, rápida y totalmente declarativa.

[![Ver demo](https://img.youtube.com/vi/X3F38hSY9iE/maxresdefault.jpg)](https://youtube.com/shorts/X3F38hSY9iE)

---

## 🚀 Características

* 🔍 Búsqueda de cócteles en tiempo real
* 📋 Visualización de recetas e ingredientes
* 🖼️ Carga de imágenes optimizada
* ⚡ Interfaz moderna con Jetpack Compose
* 📱 Diseño responsive y fluido

---

## 🧠 Tecnologías

* **Kotlin**
* **Jetpack Compose**
* **REST API (CocktailDB)**
* **Arquitectura MVVM**
* **Coroutines / Flow**
* **ViewModel + State Management**

---

## 📸 Screenshots

<p align="center">
  <img src="https://raw.githubusercontent.com/Fabi198/fabi198.github.io/main/assets/db_cocktails/main_screen.webp" width="250">
  <img src="https://raw.githubusercontent.com/Fabi198/fabi198.github.io/main/assets/db_cocktails/cocktail_full_view.webp" width="250">
  <img src="https://raw.githubusercontent.com/Fabi198/fabi198.github.io/main/assets/db_cocktails/drawer_menu.webp" width="250">
  <img src="https://raw.githubusercontent.com/Fabi198/fabi198.github.io/main/assets/db_cocktails/ingredient_view.webp" width="250">
  <img src="https://raw.githubusercontent.com/Fabi198/fabi198.github.io/main/assets/db_cocktails/language_filter.webp" width="250">
  <img src="https://raw.githubusercontent.com/Fabi198/fabi198.github.io/main/assets/db_cocktails/list_by_filter.webp" width="250">
  <img src="https://raw.githubusercontent.com/Fabi198/fabi198.github.io/main/assets/db_cocktails/save_favorite.webp" width="250">
</p>

---

## 🏗️ Arquitectura

El proyecto sigue una estructura basada en **MVVM**, separando claramente:

* `UI` → Compose (pantallas y componentes)
* `ViewModel` → Manejo de estado
* `Data` → API + repositorio

Esto permite:

* código mantenible
* fácil testeo
* escalabilidad

---

## ⚙️ Instalación

```bash
git clone https://github.com/Fabi198/CocktailsDBComposeVersion.git
```

Abrir en **Android Studio** y ejecutar.

---

## 📡 API

La app consume datos de una API de cócteles, lo que permite acceder a:

* recetas
* ingredientes
* imágenes

Este tipo de APIs abiertas suelen proveer bases de datos completas de bebidas para aplicaciones y proyectos de práctica ([GitHub][1])

---

## 🎯 Objetivo del Proyecto

Este proyecto fue desarrollado para:

* Practicar **Jetpack Compose**
* Aplicar arquitectura moderna en Android
* Consumir APIs reales
* Construir una app funcional de principio a fin

---

## 🧪 Posibles mejoras

* ⭐ Favoritos
* 🔐 Login / usuario
* 📥 Cache local (Room)
* 🎨 Animaciones avanzadas
* 🌐 Modo offline

---

## 📫 Contacto

* GitHub: https://github.com/Fabi198

---

## 🧾 Licencia

Proyecto de uso educativo / portfolio.

[1]: https://github.com/carlagesa/CocktailDB?utm_source=chatgpt.com "GitHub - carlagesa/CocktailDB: 🍸 🍹🥂🍷🍸🍾🍹This API contains a collection of free, open source cocktail data. This project was created for entry level developers who need access to free API's."

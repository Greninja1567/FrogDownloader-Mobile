<p align="center">
  <img src="https://github.com/user-attachments/assets/0ed7ed23-eda3-4782-ae8d-f221e8565c54" alt="FrogDownloader Logo" width="120" height="120">
</p>

<h1 align="center">FrogDownloader Pro</h1>


FrogDownloader Pro es una aplicación nativa para Android diseñada para la gestión y descarga que que permite la reproducción de contenido. La app resuelve dos grandes problemas: permite descargar videos y música de casi cualquier plataforma de internet y ofrece una experiencia de visualización en YouTube completamente libre de publicidad.El motor de esta aplicación es yt-dlp, lo que garantiza descargas rápidas, seguras y compatibles con formatos libres.
## Características

* 🎵 Descarga de audio y video en múltiples formatos.
* 🔗 Soporte para cientos de páginas web compatibles.
* ⚡ Interfaz gráfica rápida y responsiva construida en Android Studio.
* 🚗 Soporte con Android Auto para reproduccion de musica de youtube

## 📺 Reproductor de YouTube Premium (Sin Anuncios)
* Cero Publicidad: Bloquea todos los anuncios de video, banners e interrupciones antes y durante la reproducción.
* Streaming Eficiente: Carga los videos directamente usando el extractor de formatos libres, reduciendo el consumo de datos innecesarios.
* Reproducción en Segundo Plano: Sigue escuchando el audio de tus videos incluso con la pantalla apagada o al salir de FrogDownloader Pro.

## 🚗 Soporte para Android Auto
Esta aplicación incluye soporte nativo para Android Auto, permitiéndote controlar la reproducción de tu música de forma segura directamente desde la 
pantalla de tu vehículo.
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/39a32ca5-3312-4360-9c22-7598edcfb6f9"alt="Pantalla Auto 1" width="450px"/>
      <br>
      <sub><b>Reproduccion de Android Auto</b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/6b54315b-f8e8-497f-aa56-1e42c5db4b1c"alt="Pantalla Auto 2" width="450px"/>
      <br>
      <sub><b>Reproduccion de Android Auto</b></sub>
    </td>
  </tr>
</table>

## 🖥️ Interfaz en Ejecución

A continuación se muestran las capturas de pantalla de la aplicación en funcionamiento:

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/a15a358e-54d5-4378-803a-e40b3a8430a8" alt="Pantalla 1" width="300px"/>
      <br>
      <sub><b>Pestaña Principal (Stream)</b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/a61a0f6a-5a04-4003-887f-26414a5debad"alt="Pantalla 2" width="300px"/>
      <br>
      <sub><b>Pestaña de descargas</b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/c6762a53-db20-4313-8151-08b241a9e1db" alt="Pantalla 3" width="300px"/>
      <br>
      <sub><b>Pestaña de Ajustes</b></sub>
    </td>
  </tr>
</table>

## 📱 Requisitos del Sistema
Para que FrogDownloader Pro funcione correctamente, el dispositivo móvil debe cumplir con las siguientes especificaciones:
* Sistema Operativo Mínimo: Android 8.0 (Oreo) / API 26 o superior.
* Sistema Operativo Optimizado: Android 16 / API 36 (Target SDK).
* Conexión a Internet: Requerida para realizar las búsquedas, reproducciones y descargas de medios.
* Almacenamiento Interno: Espacio disponible suficiente para guardar los videos y música descargados.
* **yt-dlp**: Motor principal de descargas (asegúrate de tener la versión más reciente).
* **ffmpeg**: Para la correcta conversión de formatos multimedia (se encuentra empaquetado dentro del aplicativo).


## ⚙️ ¿Cómo funciona por dentro?
La aplicación actúa como una interfaz gráfica para yt-dlp. Cuando pones un enlace en la app, ocurren los siguientes pasos:
* Análisis: FrogDownloader Pro usa yt-dlp para revisar el enlace y encontrar los formatos de video y audio disponibles en el servidor.
* Selección: El usuario elige la calidad que desea en la pantalla de la app.
* Procesamiento: yt-dlp extrae los archivos en su formato nativo libre. Si es necesario, se procesan para unirse en un solo archivo final en tu celular.

## Créditos y Reconocimientos Especiales

FrogDownloader Pro es una interfaz gráfica (GUI) que actúa como puente para el usuario final, pero el verdadero motor técnico detrás de esta aplicación es **yt-dlp**.

Queremos expresar nuestro más sincero agradecimiento y otorgar el crédito correspondiente al proyecto **yt-dlp** (un fork del célebre *youtube-dl*). Este software de código abierto es el encargado de realizar el análisis de las páginas web, la evasión de restricciones de velocidad y la extracción directa de los flujos multimedia.

* **Proyecto Oficial:** [https://github.com](https://github.com/yt-dlp/yt-dlp)
* **Licencia de yt-dlp:** Unlicense (Dominio Público).

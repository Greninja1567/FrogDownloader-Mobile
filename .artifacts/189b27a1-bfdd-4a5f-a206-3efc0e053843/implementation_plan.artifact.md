# Plan de Implementación - Glassmorfismo en Tema Cristal iOS

El usuario desea mejorar el tema "Cristal iOS (Estilo Glass)" aplicando efectos de Glassmorfismo (translucidez, bordes sutiles y desenfoque) en toda la interfaz de la aplicación.

## Revisión del Usuario Requerida

> [!IMPORTANT]
> El efecto de desenfoque (Blur) en tiempo real sobre el fondo es limitado en Android. Utilizaremos una combinación de `Modifier.blur` (disponible en Android 12+) para las capas de fondo y translucidez avanzada con bordes de "cristal" para versiones anteriores, asegurando que la app se vea moderna en todos los dispositivos.

## Cambios Propuestos

### [Interfaz de Usuario]

#### [MODIFICAR] [InterfazApp.kt](file:///home/greninja/AndroidStudioProjects/FrogDownloader/app/src/main/java/com/greninja/frogdownloader/InterfazApp.kt)
- **Extensión `.glass()`**: Crearé una extensión de `Modifier` para aplicar de forma consistente:
    - Fondo semi-transparente con degradado sutil.
    - Borde blanco ultra-fino (estilo cristal).
    - Desenfoque de capa (en Android 12+).
    - Esquinas redondeadas pronunciadas.
- **Rediseño de Componentes**:
    - **Reproductor**: La tarjeta del video tendrá un efecto de cristal más marcado.
    - **Listas**: Los elementos de la lista de reproducción y descargas usarán el nuevo modificador.
    - **Barras de Navegación**: El `TopAppBar`, `NavigationBar` y `NavigationRail` se integrarán mejor con el fondo usando translucidez.
    - **Diálogos y BottomSheets**: Aplicar el estilo glass a las ventanas emergentes.
- **Ajuste de Colores**: Refinar `EsquemaColoresGlass` para mejorar la legibilidad del texto sobre fondos translúcidos.

## Plan de Verificación

### Pruebas Manuales
1. Cambiar al tema "Cristal iOS" en los Ajustes.
2. Verificar que las tarjetas tengan bordes brillantes y fondo translúcido.
3. Comprobar que el texto sea legible sobre la imagen de fondo.
4. Probar la navegación entre pestañas para asegurar la consistencia del efecto.
5. (Si hay dispositivo Android 12+) Verificar el efecto de desenfoque en los paneles.

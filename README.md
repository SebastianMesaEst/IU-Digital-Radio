IU Digital Radio
Aplicación móvil para Android desarrollada con Kotlin, Jetpack Compose y Media3 ExoPlayer.

IU Digital Radio es un proyecto académico de la Tecnología en Desarrollo de Software de la Institución Universitaria Digital de Antioquia (IU Digital). Su propósito es aplicar conocimientos de desarrollo móvil mediante una interfaz de radio por Internet que permite seleccionar transmisiones, controlar la reproducción y utilizar funciones del dispositivo.

El proyecto evolucionó de un prototipo con controles visuales a una aplicación que reproduce audio en streaming, informa sobre su estado y maneja errores de conexión.
1. Características principales
- Reproducción de audio en streaming mediante Media3 ExoPlayer.
- Controles Play, Pause, Mute y Unmute.
- Cambio de emisora sin necesidad de reiniciar la aplicación.
- Indicadores de carga, reproducción, pausa y errores de conexión.
- Catálogo con descripciones musicales y estado de disponibilidad.
- Captura de fotografías mediante la cámara del dispositivo para mostrarlas como imagen de perfil durante la ejecución.
- Respuesta háptica mediante vibración en determinadas interacciones.
- Interfaz creada con Jetpack Compose y Material 3, con una paleta inspirada en los colores de IU Digital.
- Ícono personalizado para la aplicación.
2. Catálogo de emisoras
Emisora	Categoría musical	Estado en el proyecto
Radio Paradise - Rock Mix	Rock clásico y alternativo	Reproducción comprobada
Radio Paradise - Main Mix	Selección musical variada	Reproducción comprobada
Radio Paradise - Mellow Mix	Pop y rock tranquilo	Reproducción comprobada
La Ochentera 91.9 FM	Clásicos de los años ochenta	Pendiente de enlace de transmisión verificado
98.9 Clásicos	Música retro y clásicos	Pendiente de enlace de transmisión verificado


Las emisoras pendientes se muestran deshabilitadas para evitar intentos de reproducción sin una dirección de audio válida.

Importante: las transmisiones pertenecen a sus respectivos proveedores. Las pruebas de reproducción no implican autorización para redistribuir o comercializar sus señales. Antes de publicar o distribuir la aplicación con emisoras integradas, deben verificarse los términos de uso y obtener los permisos que correspondan.
3. Tecnologías utilizadas
- Kotlin: lenguaje de programación.
- Android Studio: entorno de desarrollo.
- Jetpack Compose: construcción declarativa de interfaces.
- Material 3: componentes visuales.
- AndroidX Media3 ExoPlayer: reproducción de audio por Internet.
- Activity Result API: solicitud de permisos y captura de fotografía.
- Gradle: configuración y construcción del proyecto.
- Git y GitHub: control de versiones y publicación del código.
4. Requisitos
- Android Studio con un SDK de Android compatible con el proyecto.
- JDK y Gradle configurados mediante Android Studio.
- Dispositivo Android o emulador con conexión a Internet.
- Permiso de cámara, si se desea utilizar la fotografía de perfil.
El proyecto está configurado con minSdk 24. Las versiones de compilación y dependencias pueden consultarse en app/build.gradle.kts y gradle/libs.versions.toml.
5. Instalación y ejecución
1. Clonar el repositorio:
   git clone https://github.com/SebastianMesaEst/IU-Digital-Radio.git
2. Abrir la carpeta descargada en Android Studio.
3. Esperar a que termine la sincronización de Gradle.
4. Seleccionar un emulador o dispositivo Android compatible.
5. Ejecutar el módulo app desde Android Studio.
6. Seleccionar una emisora disponible y utilizar los controles del reproductor.
6. Generación de APK de prueba
En Android Studio, usar Build > Generate App Bundles or APKs > Generate APKs (el nombre del menú puede variar según la versión).

La APK de depuración suele generarse en:
app/build/outputs/apk/debug/app-debug.apk

Esta APK es un artefacto de prueba y no constituye una versión de publicación firmada ni una autorización de uso o distribución de transmisiones de terceros.

7. Pruebas realizadas
   
Durante el desarrollo se ejecutaron pruebas manuales en un emulador Android, entre ellas:
Prueba	Resultado observado
Compilación e inicio de la aplicación	Satisfactorio
Reproducción de las tres estaciones Radio Paradise	Satisfactorio
Cambio entre estaciones	Satisfactorio
Play y Pause	Satisfactorio
Mute y Unmute	Satisfactorio
Detección de error con una URL de prueba inválida	Se mostró un mensaje de error
Recuperación al restaurar la URL válida	Reproducción restablecida
Captura de fotografía	Satisfactorio
Visualización de emisoras pendientes como deshabilitadas	Satisfactorio
Interfaz rediseñada e ícono personalizado	Satisfactorio


Estos resultados describen comprobaciones manuales durante el desarrollo; no sustituyen una batería automatizada de pruebas ni garantizan disponibilidad continua de los servicios externos.

8. Estructura general
   
app/
  src/main/
    java/com/example/iudigitalradio/
      MainActivity.kt
    res/
      mipmap-*/
      drawable/
    AndroidManifest.xml
  build.gradle.kts
gradle/
  libs.versions.toml


MainActivity.kt contiene la lógica principal de interfaz, selección de emisoras, reproducción y cámara de esta versión del proyecto.

9. Limitaciones y posibles mejoras
    
- Las estaciones sin URL confirmada permanecen deshabilitadas.
- La reproducción depende de la conexión a Internet y de la disponibilidad de los servidores externos.
- La fotografía de perfil se mantiene en memoria durante la sesión; esta versión no implementa almacenamiento persistente de la imagen.
- El reproductor está vinculado al ciclo de vida de la pantalla actual. La reproducción persistente en segundo plano mediante un servicio multimedia es una mejora futura.
- Se recomienda añadir pruebas automatizadas, mejoras de accesibilidad y una gestión de permisos de transmisión antes de distribuir la aplicación.
  
10. Contexto académico
  
El proyecto forma parte de una experiencia de aprendizaje en desarrollo de software móvil y sirve como base para una tesina de sistematización de experiencias. El documento académico analiza la evolución del prototipo, las decisiones técnicas, las dificultades encontradas, las pruebas y los aprendizajes adquiridos durante el desarrollo.

11. Autor
    
Sebastián Mesa Meneses
Tecnología en Desarrollo de Software
Institución Universitaria Digital de Antioquia
Nota: IU Digital Radio es un proyecto académico. La referencia a la institución y el uso de una paleta inspirada en sus colores no significan que sea una aplicación oficial o respaldada por la universidad.

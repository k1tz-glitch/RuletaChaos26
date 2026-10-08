# Ruleta Chaos - Minecraft 26.3 (Fabric)

Mod de ruleta automática para **Minecraft 26.3** (Fabric).

## Características
- Gira sola cada 5 / 10 / 20 minutos (aleatorio)
- 8 colores con efectos, desafíos y premios
- Barra de progreso para desafíos rojos
- Archivo de configuración: `config/ruletacaos.json`
- Comandos: `/ruleta girar`, `/ruleta parar`, `/ruleta amarillo <1-3>`, `/ruleta recargar`, `/ruleta estado`
- Tecla **I** para ocultar notificaciones

## Requisitos
- Minecraft **26.3**
- Fabric Loader **0.19.5+**
- Fabric API **0.162.0+26.3** (o superior)
- Java **25**

## Cómo compilar

1. Asegúrate de tener **Java 25** instalado.
2. Abre una terminal en esta carpeta.
3. Ejecuta:
   ```
   ./gradlew build
   ```
   (En Windows: `gradlew.bat build`)

4. El `.jar` estará en:
   ```
   build/libs/ruletacaos-1.0.0.jar
   ```

5. Copia el jar a la carpeta `mods` de tu servidor/cliente junto con Fabric API.

## Notas
- Este mod está portado a los nombres oficiales de Minecraft 26.3 (sin ofuscar).
- Algunos efectos amarillos (mobs más fuertes, etc.) están preparados pero necesitan mixins adicionales para ser 100% permanentes.
- Compatible con multiplayer.

## Licencia
MIT

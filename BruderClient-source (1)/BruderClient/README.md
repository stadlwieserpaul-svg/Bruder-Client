# BruderClient (Fabric, Minecraft 1.21.11, Java 21)

## Build
1. Install JDK 21 and Gradle 8.14+ (or 9.x, whatever your Loom version requires).
2. Check https://fabricmc.net/develop (select 1.21.11) and copy the current
   `yarn_mappings`, `loader_version`, `loom_version`, `fabric_version` into gradle.properties.
3. `gradle wrapper` once, then `./gradlew build`  (Windows: `gradlew.bat build`)
4. Jar: build/libs/bruderclient-1.0.0.jar  (NOT the -sources jar) -> put in .minecraft/mods with Fabric API.

## Use
Right Shift = GUI. All modules have (unbound) keybinds under Options > Controls > BruderClient.
Settings are saved to config/bruderclient.json (also holds the Block/Chunk Finder block list).

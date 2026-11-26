# Teamcraft

Teamcraft is a Minecraft team mod. This README describes the current state of the code.

## Current progress

- Fabric project configured for Minecraft Java 26.3.
- Fabric metadata and the `Teamcraft` mod initializer are configured.
- `/team` is registered in the source and replies that Teamcraft is loaded.
- The first Minecraft launch has not been verified yet.
- Team creation, membership, join requests, admin controls, and persistent storage are not implemented.

## Development environment

- Minecraft Java 26.3
- Fabric Loader 0.20.1
- Fabric API 0.167.0+26.3
- Java 25
- Gradle 9.8.2 via the included Gradle wrapper

Open this folder in VSCodium. From its terminal, start the development client with:

```powershell
.\gradlew.bat runClient
```

Build the mod jar with:

```powershell
.\gradlew.bat build
```

The built jar is placed in `build/libs`.

## Commands

| Command | Current behavior |
| --- | --- |
| `/team` | Confirms that Teamcraft is loaded. |

## Project links

- GitHub: https://github.com/xSpletti312x/Teamcraft
- Issues: https://github.com/xSpletti312x/Teamcraft/issues

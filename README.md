# Teamcraft — Fabric

Teamcraft is a Minecraft Fabric mod project for Minecraft Java 26.3.

## Current progress

The current development version supports:

- Creating a team with `/teamcraft create <name>`.
- Leaving a team with `/teamcraft leave`.
- Listing team names with `/teamcraft list`.
- Changing the team's name and color with `/teamcraft modify name <new-name>` and `/teamcraft modify color <color>` (owner only).
- Showing the colored team name, player name, and message in chat.
- Saving team data to the world.

Team data also has an initial join-request model. The join commands, approval flow, and five-minute expiry are not implemented yet.

## Development

Open this folder in VSCodium. From its integrated PowerShell terminal, start the development client with:

```powershell
.\gradlew.bat runClient
```

Build the mod jar with:

```powershell
.\gradlew.bat build
```

The jar is created in `build/libs`.

## Project links

- GitHub: https://github.com/xSpletti312x/Teamcraft
- Issues: https://github.com/xSpletti312x/Teamcraft/issues

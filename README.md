# Teamcraft

Teamcraft is a planned Minecraft team system for server owners who want players to create and manage teams with simple commands. Players can request to join teams, and current members can approve or reject requests directly from clickable chat messages.

> **Status:** Planning. No plugin or mod implementation has started yet.

## Project goals

- Let players create teams, request to join, leave, and manage team membership.
- Let team owners remove members and manage their teams.
- Give server admins full control over every team.
- Keep all team data local to each server; teams will not be shared across servers.
- Use commands as the only interface, permanently. There will be no GUI.

## Agreed rules

- There can be any number of teams, and team membership has no limit.
- Each player can belong to only one team at a time.
- Any current team member can accept or reject a join request.
- The request decision appears in chat with clickable accept and reject actions.
- If the owner leaves, ownership passes to the longest-standing remaining member: the person who joined next after the owner.
- The first phase focuses on teams, membership, and admin controls. Team customization comes later.
- The initial target is Minecraft Java 26.3, with older versions considered afterward. The oldest supported version is not decided yet.

## Planned platforms

- Paper
- Spigot
- Folia
- Bukkit
- Fabric
- Forge
- NeoForge
- Quilt

The exact Minecraft and loader version matrix still needs to be defined. Paper, Spigot, Bukkit, and Folia share parts of the Bukkit plugin ecosystem, but Folia needs specific compatibility and threading work. Fabric, Forge, NeoForge, and Quilt require their own mod adapters. Quilt's support for newer Minecraft releases should be confirmed before promising a version range.

## Phase 1: teams, members, and admins

### Players and teams

- Create and disband a team.
- Request to join a team.
- Notify team members in chat with clickable accept and reject actions.
- Allow any current member to decide a pending request.
- Leave a team or be removed by its owner.
- Transfer ownership to the longest-standing remaining member when the owner leaves.

### Admin controls

Admins should be able to find, inspect, and fully manage all teams. Planned actions include assigning or removing players, editing team data, reviewing membership and requests, and deleting teams. Admin actions should require explicit permissions and provide clear feedback.

## Later phase: team customization

Customization is intentionally deferred until the core team, membership, and admin workflows are established. Possible options include a team name, tag, description, or color; the exact fields are not decided yet.

## Technical planning principles

1. **Separate core rules from platform adapters.** Team rules, membership, and persistence should not depend directly on Bukkit, Fabric, or other loader APIs.
2. **Keep the server authoritative.** The server validates membership, ownership, and request decisions.
3. **Persist team data.** Team state must survive server restarts. The storage backend (for example, SQLite or a file format) is still undecided.
4. **Choose a bounded version matrix.** Define a minimum Minecraft version and the supported loader/API versions before implementing adapters and CI.
5. **Use player UUIDs as stable identities.** Names can change and should be treated as display information.
6. **Plan for data migrations.** Persisted schemas need versioning and a migration path.
7. **Handle Folia deliberately.** Any server scheduling or world-facing work must respect Folia's threading model.

## Proposed repository layout

```text
teamcraft/
  README.md
  docs/
    decisions/       # Architecture Decision Records
    requirements.md
    platform-matrix.md
  core/              # Platform-independent rules and models
  adapters/          # Platform-specific plugins and mods
  tests/             # Core rule and integration tests
```

## Open planning questions

- What is the oldest Minecraft version to support?
- Can a player have pending requests for multiple teams at once? Should requests expire?
- What should happen if the owner is banned or otherwise unavailable?
- Does “Bukkit” mean direct Bukkit API compatibility in addition to Spigot and Paper?
- Which storage backend should be used for the first release?

## Quality requirements

- Enforce one-team-per-player and permission checks consistently.
- Ensure only the first valid response resolves a pending request.
- Validate command input and return clear error messages.
- Use UUIDs so offline players and name changes are handled correctly.
- Log admin actions and team disbanding.
- Handle simultaneous join-request decisions without corrupting team state.
- Provide clear behavior for damaged or incompatible saved data.

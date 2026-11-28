# Teamcraft — Remaining work

This file tracks agreed features that are not yet implemented in the current Fabric 26.3 project.

## Teams and membership

- Finish join requests with a five-minute expiry and remove or ignore expired requests.
- Add `/teamcraft join <team>`: players without a team can request to join.
- Add a command for team members to review and approve join requests; any team member may approve.
- Let server administrators join any team without approval, while preserving the one-team-per-player rule.
- Add a command for the owner to delete their team.
- Let server administrators delete any team.
- Add member kicking and the agreed owner/team-admin permissions.
- Add team information showing the name, color, owner, team admins, and members.

## Team administration and ownership

- Implement team-admin promotion and demotion by the owner only. Team admins cannot promote or demote admins.
- Implement server-administrator controls over every team.
- Update owner succession: prefer the earliest-promoted team admin; otherwise choose the earliest-joined member.
- Warn the owner before leaving and allow them to demote the proposed successor to select the next candidate.

## Compatibility and release

- Decide the supported Minecraft version range below 26.3.
- Add and maintain platform versions for Paper, Spigot, Folia/Bukkit, Fabric, Forge, NeoForge, and Quilt.
- Test the implemented commands, persistence, permission checks, and owner succession on each supported platform/version.
- Prepare public release metadata and platform-specific installation instructions.

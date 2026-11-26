# Teamcraft — Projektplanung

Ein Minecraft-Team-System, mit dem Spieler Teams erstellen, Beitrittsanfragen stellen, Teams verwalten und Admins serverweit eingreifen können.

> Status: Konzeptphase. Dieser Ordner enthält nur Planung; es wurde noch keine Plugin-/Mod-Implementierung begonnen.

## Ziel und vorläufiger Umfang

### Spieler und Teams (Phase 1)

- Ein Spieler kann ein Team erstellen und ist zunächst dessen Owner.
- Spieler können ein Team verlassen. Der Owner kann Mitglieder entfernen.
- Spieler können Beitritt anfragen; ein bereits aufgenommenes Teammitglied bestätigt oder lehnt die Anfrage ab.
- Teamname und weitere Teamdaten sollen durch den Owner anpassbar sein.
- Unbegrenzt viele Teams; jeder Spieler darf höchstens einem Team angehören.
- Jedes Teammitglied darf Beitrittsanfragen bestätigen oder ablehnen.

### Administration (Phase 1)

- Admins können alle Teams finden, ansehen und verwalten.
- Geplante Adminaktionen: Team/Spieler zuweisen oder entfernen, Teamdaten ändern, Team löschen und Anfragen/Mitglieder moderieren.
- Jede privilegierte Aktion braucht eine explizite Permission und eine nachvollziehbare Rückmeldung.

## Festgelegte Produktentscheidungen

- Projektname: **Teamcraft**.
- GitHub-Ziel: **öffentlich**.
- Zielplattformen: **Paper, Spigot, Folia, Bukkit, Fabric, Forge, NeoForge und Quilt**.
- Teamanzahl: unbegrenzt; pro Spieler höchstens eine Teammitgliedschaft.
- Anfragefreigabe: ein beliebiges aktuelles Teammitglied darf bestätigen.
- Reihenfolge: erst Teams, Mitgliedschaft und Adminfunktionen; Team-Anpassungen folgen danach.

## Vorläufige technische Entscheidungen

## Weitere festgelegte Entscheidungen

- Start: Minecraft Java 26.3; abwärtskompatible Releases sind geplant.
- Beim Austritt des Owners wird die Leitung an das älteste verbleibende Mitglied übergeben, also an die Person, die als Nächstes nach dem Owner beigetreten ist.
- Ein Spieler gehört höchstens einem Team an. Die Anzahl der Teams und Teammitglieder ist unbegrenzt.
- Jedes Teammitglied darf Anfragen bestätigen.
- Die erste Phase umfasst Teams, Mitglieder und Adminfunktionen. Team-Anpassungen kommen danach.
- Bedienung ausschließlich über Commands, dauerhaft; es wird keine GUI geben.

## Versionierungs- und Plattformhinweis

Minecraft Java 26.3 ist veröffentlicht und Paper dokumentiert 26.3 als aktuelle Zielversion. Der Start auf 26.3 ist damit plausibel. „Abwärts“ benötigt noch eine konkrete Untergrenze, denn jede ältere Minecraft-/Loader-Kombination erweitert Build- und Testaufwand.

Paper, Spigot, Bukkit und Folia teilen Teile des Bukkit-Ökosystems, brauchen aber eine Kompatibilitäts- und Threadingprüfung, besonders Folia. Fabric, Forge, NeoForge und Quilt benötigen eigene Mod-Adaptionen. Quilt ist ein besonderer Unsicherheitsfaktor: Laut Quilt FAQ wurde die Pflege der Kompatibilität mit neuen Minecraft-Releases im Dezember 2025 eingestellt. Vor einer Zusage für 26.3 sollte geklärt werden, ob Quilt-Unterstützung eine bestehende Quilt-Version, Fabric-Mod-Kompatibilität oder nur gepflegte Releases meint.

1. **Kernlogik getrennt von Plattformadaptern.** Teamregeln, Rollen und Persistenz sollen nicht an Bukkit/Fabric-APIs gebunden sein.
2. **Serverautorität.** Mitgliedschaften, Owner-Rechte und Bestätigungen werden serverseitig geprüft; Clients dürfen den Zustand nicht selbst festlegen.
3. **Persistente Speicherung.** Daten müssen Neustarts überleben. Backend (SQLite, JSON oder plattformspezifisch) wird nach Zielplattform und Serverumfang entschieden.
4. **Versionen bewusst begrenzen.** Erst eine konkrete Minecraft-Version und API-Matrix auswählen; danach Adapter und CI darauf aufbauen.
5. **Datapack als möglicher Teilumfang, nicht als vorausgesetzter Hauptadapter.** Erst prüfen, ob es die gewünschte Bedienung, Verwaltung, Speicherung und Loaderabdeckung sauber tragen kann.

## Zu klärende Produktregeln

- Darf jeder Spieler nur einem Team angehören? Darf ein Owner mehreren Teams vorstehen?
- Wie viele Teams und Mitglieder sind erlaubt? Gibt es konfigurierbare Limits?
- Darf jedes Mitglied Anfragen bestätigen, oder nur Owner/Leitung? Was passiert bei widersprüchlichen Entscheidungen?
- Soll ein Spieler eine Anfrage an mehrere Teams gleichzeitig stellen können? Wie laufen Anfragen ab?
- Was passiert, wenn der Owner austritt oder gebannt wird: Übergabe, Auflösung oder Adminentscheidung?
- Welche Teamfelder sind veränderbar: Name, Kurzname/Tag, Beschreibung, Farbe, Sichtbarkeit?
- Gibt es Einladung per Spielername zusätzlich zur offenen Beitrittsanfrage?
- Sind Teamchat, Rollen, Allianzen, gemeinsame Homes oder nur Mitgliederverwaltung im ersten Release?
- Müssen Teams bei Servern desselben Netzwerks geteilt werden, oder ist zunächst ein einzelner Server das Ziel?

## Plattform- und Versionsstrategie

Der Begriff „große Loader“ muss konkretisiert werden. Mögliche getrennte Ziele:

- **Paper/Spigot:** Plugin-API für Bukkit-Server.
- **Fabric:** Mod-Loader im Java-Ökosystem.
- **NeoForge** (ggf. Forge, falls ältere Versionen wichtig sind): weitere Java-Mod-APIs.
- **Datapack:** kann ergänzend Reichweite bieten, muss aber gegen UI-, Datenhaltungs- und Wartungsanforderungen geprüft werden.
- **Bedrock:** eigenes Add-on-/Script-Ökosystem, keine Java-Loader-Variante.

Empfehlung für die Planung: zunächst ein gemeinsames Domänenmodell und einen ersten Adapter wählen. Weitere Plattformen folgen nach einem funktionsfähigen Kern. Paper, Spigot, Bukkit und Folia teilen Teile des Bukkit-Ökosystems, benötigen aber eine Kompatibilitäts- und Threadingprüfung, besonders für Folia. Fabric, Forge, NeoForge und Quilt brauchen eigene Mod-Adaptionen. Vor der Implementierung eine Tabelle mit Minecraft-Version, Loader/API-Version, Java-Version, Buildtool und Testumgebung festlegen.

## Architekturfragen für die technische Planung

- Gemeinsames Monorepo mit Modulen oder getrennte Repositories?
- Gemeinsame Java-Bibliothek (z. B. Gradle Multi-Project) versus Adapter mit unabhängigem Build?
- Speicherung: lokale Datei/SQLite für Einzelserver oder SQL-Datenbank für mehrere Server?
- Bedienung zuerst über Commands, Menüs/GUI oder beides?
- UUID als dauerhafte Spieleridentität; Namen nur als veränderliche Anzeigeinformation.
- Schema-Versionierung und Migrationsweg früh vorsehen.
- Permissions und Konfiguration je Adapter dokumentieren.

## Vorschlag für MVP (Phase 1)

Owner-Übergabe beim Austritt: an das älteste verbleibende Mitglied.

Keine GUI: Commands bleiben die einzige Bedienoberfläche.

1. Team erstellen, umbenennen und auflösen.
2. Beitrittsanfrage stellen, ansehen, annehmen und ablehnen.
3. Team verlassen und durch Owner entfernen.
4. Owner-Übergabe oder geregelte Auflösung beim Owner-Austritt.
5. Admin-Commands für Auflisten, Anzeigen, Zuweisen, Entfernen und Löschen.
6. Persistenz mit Neustartverhalten und Datenschema.
7. Ein Plattformadapter, eine festgelegte Minecraft-Version und automatisierte Regeltests.

Team-Anpassungen wie Name/Tag/Farbe sowie Teamchat, Allianzen, GUI und Cross-Server-Synchronisierung werden als spätere Erweiterungen behandelt.

## Qualitäts- und Sicherheitsanforderungen

- Regeln zentral testen: keine doppelte Mitgliedschaft, nur berechtigte Bestätigung, Owner kann nicht unbemerkt verschwinden.
- Alle Befehle validieren Eingaben und liefern klare Fehlermeldungen.
- Spielerbezug über UUID; Offline-Spieler und Namensänderungen berücksichtigen.
- Adminaktionen und Teamauflösung protokollieren.
- Parallel eingehende Annahmen dürfen keine inkonsistenten Teams erzeugen.
- Definierte Fehlerbehandlung bei beschädigten oder nicht migrierbaren Daten.

## Noch benötigte Entscheidungen

- Welche älteste Minecraft-Version soll unterstützt werden? „26.3 und abwärts“ braucht eine endliche Untergrenze.
- Darf ein Spieler gleichzeitig bei mehreren Teams anfragen? Verfallen offene Anfragen automatisch?
- Ist zunächst ein einzelner Server das Ziel oder sollen Teams serverübergreifend geteilt werden?
- Bedeutet „Bukkit“ in der Liste direkte Bukkit-API-Kompatibilität zusätzlich zu Spigot/Paper, oder ist „Bukkit“ als Oberbegriff gemeint?

Bitte beantworte vor dem technischen Grundgerüst mindestens:

1. Welche Minecraft-Version soll zuerst unterstützt werden, und wie weit soll die Rückwärtskompatibilität reichen?
2. Was passiert beim Austritt des Owners: automatische Übergabe, Teamauflösung oder Adminentscheidung?
3. Darf ein Spieler parallel Anfragen an mehrere Teams stellen? Verfallen offene Anfragen automatisch?
4. Gibt es ein Mitgliederlimit pro Team? „Unbegrenzt“ wurde hier als unbegrenzte Anzahl von Teams verstanden.
5. Ist das Ziel zunächst ein einzelner Server, oder sollen Teams über mehrere Server geteilt werden?
6. Soll Phase 1 eine GUI enthalten, oder zunächst Commands?

## Repository-Struktur (Vorschlag, noch nicht angelegt)

```text
teamcraft/
  README.md
  docs/
    decisions/       # kurze Architecture Decision Records
    requirements.md
    platform-matrix.md
  core/              # Regeln und Modelle, unabhängig von Loadern
  adapters/          # je Plattform ein Adapter
  tests/             # gemeinsame Regel-/integrationschecks
```

Scissors [![Version](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fartifactory.papermc.io%2Fartifactory%2Funiverse%2Fio%2Fpapermc%2Fpaper%2Fpaper-api%2Fmaven-metadata.xml&strategy=highestVersion&filter=26.1*&label=version&color=%23344ceb
)](https://papermc.io/downloads/paper)
[![Build Status](https://img.shields.io/github/actions/workflow/status/PaperMC/Paper/build.yml?branch=main)](https://github.com/PaperMC/Paper/actions)
[![Discord](https://img.shields.io/discord/289587909051416579.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/papermc)
===========

A [Paper](https://github.com/PaperMC/Paper) fork with useful built-in commands and a GUI/Inventory API for plugins.

## What's different from Paper?

### Built-in commands

These work out of the box without any plugins:

| Command | Description | Permission |
|---|---|---|
| `/ping [player]` | Show a player's current latency | `bukkit.command.ping` |
| `/seen <player>` | Show when a player was last online | `bukkit.command.seen` |
| `/playtime [player]` | Show total time a player has spent on the server | `bukkit.command.playtime` |
| `/invsee <player>` | Open and edit another player's inventory | `bukkit.command.invsee` |
| `/controlpanel` | In-game control panel for power, moderation and world options | `bukkit.command.controlpanel` |

All commands default to OP access.

### GUI/Inventory API

Scissors ships a built-in GUI API so plugins don't need to pull in a separate library just to open a chest menu. It supports static GUIs, fill patterns, and paginated GUIs with built-in navigation.

```java
Gui gui = Gui.builder()
    .title(Component.text("My Menu"))
    .rows(3)
    .build();

gui.fill(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
gui.slot(13, new ItemStack(Material.DIAMOND), click -> click.player().sendMessage("Hi!"));
gui.open(player);
```

---

How To (Server Admins)
------
Download a Scissors jar from the [Releases](https://github.com/StacikM/Scissors/releases) page and run it like any other Paper jar.

```
java -Xms4G -Xmx4G -jar (file).jar nogui
```

* Documentation on using Paper (applies to Scissors): [docs.papermc.io](https://docs.papermc.io)

How To (Plugin Developers)
------
Scissors is fully API-compatible with Paper. Any plugin targeting Paper will work on Scissors without changes.

* See the Paper API [here](paper-api)
* Paper API javadocs: [papermc.io/javadocs](https://papermc.io/javadocs/)

#### Repository (for paper-api)
##### Gradle
```kotlin
repositories {
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}
```
##### Maven

```xml
<repository>
    <id>papermc</id>
    <url>https://repo.papermc.io/repository/maven-public/</url>
</repository>
```

```xml
<dependency>
    <groupId>io.papermc.paper</groupId>
    <artifactId>paper-api</artifactId>
    <version>[26.1.2.build,)</version>
    <scope>provided</scope>
</dependency>
```

How To (Compiling From Source)
------
You need JDK 25 and an internet connection.

```
git clone https://github.com/StacikM/Scissors.git
cd Scissors
.\gradlew applyPatches
.\gradlew createPaperclipJar
```

The compiled jar will be in `paper-server/build/libs/`.

To get a full list of build tasks, run `.\gradlew tasks`.

How To (Contributing)
------
See [Contributing](CONTRIBUTING.md)

Upstream
------
Scissors is a fork of [PaperMC/Paper](https://github.com/PaperMC/Paper) and stays in sync with upstream. All Paper patches are included on top of which Scissors adds its own.

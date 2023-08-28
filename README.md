![banner](https://github.com/Aeldit/Cyan/blob/1.19.x/images/banner_small.png?raw=true)

> If you fork this repo, make sure to state clearly that your repo is a fork of this one, and make a link to this one

> ❌ I do NOT allow this mod to be ported on the Forge loader, nor to be uploaded on Curseforge

***

![LICENSE_PLATE](https://img.shields.io/github/license/Aeldit/Cyan?style=for-the-badge&color=2e0078&labelColor=2e0078)
![modrinth downloads](https://img.shields.io/modrinth/dt/Cyan?color=2e0078&labelColor=2e0078&label=downloads&logo=modrinth&style=for-the-badge)

[![fabric-api](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_vector.svg)](https://modrinth.com/mod/fabric-api)
or
[![quilted-fabric-api](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/quilted-fabric-api_vector.svg)](https://modrinth.com/mod/qsl)

| Supported MC Version | Up to date | Last version |
|:--------------------:|:----------:|:------------:|
|   1.17.x - 1.18.x    |     ❌      |    0.9.3     |
|    1.19 - 1.19.2     |     ❌      |    0.9.13    |
|        1.19.3        |     ❌      |    0.9.7     |
|        1.19.4        |     ✅      |    latest    |
|        1.20.x        |     ✅      |    latest    |

***

### 💲`Features`

1. Teleport yourself to :
    - your bed / respawn anchor
    - the surface (highest block on your current position)

2. Save your current location so anyone can teleport to it
   (useful on servers where you don't want players to set a point but want to let them teleport to predefined ones)

3. Remove items that are floating on the ground (useful if there is a lot of them, which causes lag)

4. See and edit plenty of options, like the minimum OP level required to run a command, or the
   distance in which items will be removed

5. Teleport yourself back to the place you died (`/back` command)

6. Plans for the future :
    - Claims

***

> If you find any issue, please make sure to report it on github so I can fix it (both badges can be clicked on to
> follow the link)
>
> [![github_issues](https://img.shields.io/github/issues/Aeldit/Cyan?color=red&style=for-the-badge&logo=github)](https://github.com/Aeldit/Cyan/issues)
>
> If you have a suggestion, you can go on my discord server and create a post in 🗽-suggestions-forum
>
> [![discord_badge](https://img.shields.io/discord/750243612473819188?color=7289da&label=DISCORD&logo=discord&logoColor=7289da&style=for-the-badge)](https://discord.gg/PcYPpqzhKS)

***

### ✅ `List of commands (and their aliases)`

|                    Command                    |                            Description                             |         Alias         |
|:---------------------------------------------:|:------------------------------------------------------------------:|:---------------------:|
|                    `/bed`                     |            Teleports you to your bed or respawn anchor             |         `/b`          |
|              `/killgrounditems`               |      Kills a items floating on the ground in a certain radius      |        `/kgi`         |
|                  `/surface`                   |                    Teleports you to the surface                    |         `/s`          |
|                                               |                                                                    |                       |
|        `/set-location <location_name>`        |              Saves the current position as a location              | `/sl <location_name>` |
|      `/remove-location <location_name>`       |                     Removes the given location                     | `/rl <location_name>` |
|            `/remove-all-locations`            |                     Removes the given location                     |           ❌           |
|          `/location <location_name>`          |                Teleports you to the given location                 | `/l <location_name>`  |
|               `/get-locations`                |                  Displays all the saved locations                  |         `/gl`         |
| `/rename-location <location_name> <new_name>` |            Renames the given location to the given name            |           ❌           |
|                                               |                                                                    |                       |
|                    `/back`                    |              Teleports you to the last place you died              |           ❌           |
|                                               |                                                                    |                       |
|          `/cyan reload-translations`          |     Reloads the custom translations (useful when editing them)     |           ❌           |
|        `/cyan remove-properties-files`        |  Transfers the properties files to json files, then deletes them   |           ❌           |
|                                               |                                                                    |                       |
|              `/cyan get-config`               | Displays in the player's chat the current value of all the options |           ❌           |

***

### 🌐 `Default Translations`

> If you have set the option `useTranslations` to false, the default language was english. But with the latest
> update
> (0.9.3), you can put a file in the config directory with the translations in the language you want (you will have to
> translate the file by yourself,
> but this way you can also customize the messages)

How to do this ?

1. Download the file `translations.json`
   located [here](https://github.com/Aeldit/Cyan/tree/1.19.4/docs) and put it in your Minecraft server folder
   (`minecraft/config/cyan`)
2. Then you can translate the file to any language you want
3. There are some unicode characters in the file (they look like `\u00A7c`). They are used to determine the
   color of the text so don't delete them unless you don't want the colors. If you want to change the colors, here
   is a link where you can find the [unicode colors](https://minecraft.tools/en/color-code.php)

***

### ❗ Important :

If you are not using this mod on client but only on server side, download the resource pack for messages translations
(Cyan 1.1X.x located in this mod's github repository)

📖 If you launch the mod on server side, it will use the latest version of minecraft, but if you use it on client side,
it will allow you to play any version of the same number (ex: 1.17, 1.17.1 ...)

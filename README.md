![banner](https://github.com/Aeldit/Aeldit/blob/main/banners/cyan.png?raw=true)
<!-- modrinth_exclude.start -->
> ❌ I do NOT allow this mod to be ported on the Forge loader, nor to be uploaded on Curseforge
<!-- modrinth_exclude.end -->
[![fabric-api](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_vector.svg)](https://modrinth.com/mod/fabric-api)
or
[![quilted-fabric-api](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/quilted-fabric-api_vector.svg)](https://modrinth.com/mod/qsl)
[![cyanlib_badge_use](https://raw.githubusercontent.com/Aeldit/Aeldit/e84549f8cef529270bd41775357d577e1f71978a/images/cyanlib-cozy.svg)](https://modrinth.com/mod/cyanlib)
<details>
<summary>🎴 Show available versions</summary>

| Supported MC Version | Up To Date | Last Version |
|:--------------------:|:----------:|:------------:|
|   1.17.x - 1.18.x    |     ❌      |    0.9.3     |
|    1.19 - 1.19.2     |     ❌      |    0.9.13    |
|        1.19.3        |     ❌      |    0.9.7     |
|         1.20         |     ❌      |    0.9.16    |
|        1.19.4        |     ✅      |    latest    |
|   1.20.1 - 1.20.6    |     ✅      |    latest    |
|    1.21 - 1.21.1     |     ✅      |    latest    |

</details>

***

### 💲Features

1. Teleport yourself to :
    - your bed / respawn anchor
    - the surface (highest block on your current position)

2. Save your current location so anyone can teleport to it
   (useful on servers where you don't want players to set a point but want to let them teleport to predefined ones)

3. Remove items that are floating on the ground (useful if there is a lot of them, which causes lag)

4. See and edit plenty of options, like the minimum OP level required to run a command, or the
   distance in which items will be removed

5. Teleport yourself back to the place you died (`/back` command)

6. Request to teleport to another player using the `/tpa` command

7. Consume XP when the player uses a teleportation command (`/bed`, `/surface`, `/tpa`)

8. Execute a command for all targets of a given selector (ex: `/cyan execute bed @a`)

<details>
<summary>✅ List of commands (and their aliases)</summary>

|                Command                |                            Description                             |         Alias         |
|:-------------------------------------:|:------------------------------------------------------------------:|:---------------------:|
|                `/bed`                 |            Teleports you to your bed or respawn anchor             |         `/b`          |
|          `/killgrounditems`           |      Kills a items floating on the ground in a certain radius      |        `/kgi`         |
|              `/surface`               |                    Teleports you to the surface                    |         `/s`          |
|                                       |                                                                    |                       |
|    `/set-location <location_name>`    |              Saves the current position as a location              | `/sl <location_name>` |
|  `/remove-location <location_name>`   |                     Removes the given location                     | `/rl <location_name>` |
|        `/remove-all-locations`        |                     Removes the given location                     |           ❌           |
|      `/location <location_name>`      |                Teleports you to the given location                 | `/l <location_name>`  |
|           `/get-locations`            |                  Displays all the saved locations                  |         `/gl`         |
| `/rename-location <name> <new_name>`  |            Renames the given location to the given name            |           ❌           |
|                                       |                                                                    |                       |
|                `/back`                |              Teleports you to the last place you died              |           ❌           |
|                                       |                                                                    |                       |
|      `/cyan reload-translations`      |     Reloads the custom translations (useful when editing them)     |           ❌           |
|    `/cyan remove-properties-files`    |  Transfers the properties files to json files, then deletes them   |           ❌           |
|                                       |                                                                    |                       |
|          `/cyan get-config`           | Displays in the player's chat the current value of all the options |           ❌           |
|                                       |                                                                    |                       |
|         `/tpa <player_name>`          |         Sends a teleportation request to the given player          |           ❌           |
|      `/tpaAccept <player_name>`       |      Accepts the teleportation request from the given player       |           ❌           |
|      `/tpaRefuse <player_name>`       |      Refuses the teleportation request from the given player       |           ❌           |
|                                       |                                                                    |                       |
| `/cyan execute <command> <target(s)>` |           Executes the given command for all the targets           |           ❌           |

</details>

### 🌐 Translations

This mod allows you to create and use custom translations or another language than english, instead of the default
messages.

To do this, go in the `config/cyan` folder of your minecraft instance / server and add the `fr_fr.json` file that
can be found [here](https://github.com/Aeldit/CyanLib/tree/1.20.4/custom_languages)

Once this is done, make sure to rename the file to `custom_lang.json` (for now, only one language can be loaded so every
player of the server will have the same language, but I might add multi-language support at some point)

***

> If you find an issue, please make sure to report it on GitHub so I can fix it (both badges can be clicked on to
> follow the link)
>
> [![github_issues](https://img.shields.io/github/issues/Aeldit/Cyan?color=red&style=for-the-badge&logo=github)](https://github.com/Aeldit/Cyan/issues)
>
> If you have a suggestion, you can go on my discord server and create a post in 🗽-suggestions-forum
>
> [![discord_badge](https://img.shields.io/discord/750243612473819188?color=7289da&label=DISCORD&logo=discord&logoColor=7289da&style=for-the-badge)](https://discord.gg/PcYPpqzhKS)

### Check out the rest of my projects !

[![cyansethome_badge](https://raw.githubusercontent.com/Aeldit/Aeldit/fdcc5b2b359f2bcc51654d9a973674c4d8557fd4/images/cyansethome-cozy-minimal.svg)](https://modrinth.com/mod/cyansethome)
[![ctms_badge](https://raw.githubusercontent.com/Aeldit/Aeldit/d668bc7cd71d654d2331905a5ad425283dedab94/images/ctms-cozy-minimal.svg)](https://modrinth.com/mod/ctm-selector)
[![cyanlib_badge](https://raw.githubusercontent.com/Aeldit/Aeldit/bef8e5f6a837ee8c3479a2550e92c0ac028200f3/images/cyanlib-cozy-minimal.svg)](https://modrinth.com/mod/cyanlib)
[![ctm_badge](https://raw.githubusercontent.com/Aeldit/Aeldit/e2fb5f7ffe92301f627540cebca28d9aa90c641d/images/ctm-cozy-minimal.svg)](https://modrinth.com/resourcepack/ctm-of-fabric)
[![ctm_faithful_badge](https://raw.githubusercontent.com/Aeldit/Aeldit/54529d9dbb33d35184f386269c889cef818e7e79/images/ctm-faithful-cozy-minimal.svg)](https://modrinth.com/resourcepack/ctm-faithful)
[![ctm_create_badge](https://raw.githubusercontent.com/Aeldit/Aeldit/54529d9dbb33d35184f386269c889cef818e7e79/images/ctm-create-cozy-minimal.svg)](https://modrinth.com/resourcepack/ctm-create)
[![dark_gui_badge](https://raw.githubusercontent.com/Aeldit/Aeldit/2f4a47b3752b28cbcd13c6d76c66a803d7fe1df5/images/dark-gui-cozy-minimal.svg)](https://modrinth.com/resourcepack/dark-smooth-gui)
[![light_gui_badge](https://raw.githubusercontent.com/Aeldit/Aeldit/2f4a47b3752b28cbcd13c6d76c66a803d7fe1df5/images/light-gui-cozy-minimal.svg)](https://modrinth.com/resourcepack/light-smooth-gui)
[![floating_texts_badge](https://raw.githubusercontent.com/Aeldit/Aeldit/c4163b0470c0d710ba2cd3314cd241b5669ef175/images/floating-texts-cozy-minimal.svg)](https://modrinth.com/datapack/floating-texts)
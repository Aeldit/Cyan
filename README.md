![banner](images/banner.png)

> ❌ I do NOT allow this mod to be ported on the Forge loader, nor to be uploaded on Curseforge

[![fabric-api](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_vector.svg)](https://modrinth.com/mod/fabric-api)
or
[![quilted-fabric-api](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/quilted-fabric-api_vector.svg)](https://modrinth.com/mod/qsl)
[![cyanlib_badge_use](https://raw.githubusercontent.com/Aeldit/Aeldit/e84549f8cef529270bd41775357d577e1f71978a/images/cyanlib-cozy.svg)](https://modrinth.com/mod/cyanlib)

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

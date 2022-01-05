# 📝 REQUIRES FABRIC API

📖 If you launch the mod on server side, it will use the latest version of minecraft, but if you use it on client side, it will allow you to play any version of the same number (ex: 1.17, 1.17.1 ...)

#### ✅ List of commands :

    /bed -> teleport the player to his bed or respawn anchor, depending one which one is the respawn point
    /bedof [player_name] -> teleport the player to the bed or respawn anchor of the specified player
        (only works if the player is online)
    /surface -> teleport the player to the surface

    /killgrounditems [distance_in_chunks] -> kill the items that are floating on the ground
        (if a radius is specified, it will only kill items that are within it;
         ex: /kgi 5 -> radius of 5 chunks
         If no radius is specified, the default value defined in the config will be used.
            This value can be changed with /setDistanceToEntitiesKgi
        )

    /Chelp (Cyan help) -> Displays help for this mod
    /ops -> displays a list of all op players

    /setAllowBed [true|false] -> allows or not the use of /bed
    /setAllowKgi [true|false] -> allows or not the use of /kgi
    /setAllowSurface [true|false] -> allows or not the use of /surface

    /setDistanceToEntitiesKgi [distance in chunks] -> change the value that is used by default by the /kgi
    /setRequiredOpLevelKgi [integer between 0 and 4 (both included)] -> change the value of the minimum required OP
        level used for the /kgi

    /getCyanConfigOptions -> display the values of the different options of the mod in the player's chat
![example_image](https://github.com/Raphoulfifou/Cyan/images/getCyanConfigOptions_example.png)
#### ✔️ List of current aliases:

    /bed -> b
    /surface -> s
    /killgrounditems -> kgi

### ❗ Important :

If you are not using this mod on client but only on server side, download the resource pack for messages translations (Cyan 1.17.X located in this repository)

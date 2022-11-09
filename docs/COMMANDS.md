# Documentation for the commands of the mod

Commands :

1. /cyan (for description and config editing)
2. Teleportation
3. Misc

## I). /cyan commands

> These commands are mostly for configuration and description

```bash
cyan
├── config -> If no argument is given after this one, the list of option and their value is send to the chat of the player
│   ├── allow -> Used to allow or not the usage of the available commands (from the Cyan mod)
│   │   ├── all [true|false] -> Sets all the 'allow' options to the given value
│   │   ├── bed [true|false] -> Sets the 'allowBed' option to the given value
│   │   ├── kgi [true|false] -> Sets the 'allowkgi' option to the given value
│   │   └── surface [true|false] -> Sets the 'allowSurface' option to the given value
│   │
│   ├── minOpLevelExe -> Used to define which OP level is required for a player to execute a command (0 means the player is not OP)
│   │   ├── all [0|1|2|3|4] -> Sets all the 'minOpLevelExe' options to the given value
│   │   ├── modifConfig [0|1|2|3|4] -> Sets the 'minOpLevelExeModifConfig' options to the given value
│   │   ├── bed [0|1|2|3|4] -> Sets the 'minOpLevelExeBed' option to the given value
│   │   ├── kgi [0|1|2|3|4] -> Sets the 'minOpLevelExekgi' option to the given value
│   │   └── surface [0|1|2|3|4] -> Sets the 'minOpLevelExeSurface' option to the given value
│   │
│   └── other -> Used to define different parameters for the other different commands
│       ├── boolean -> Commands with boolean argument
│       │   └── useOneLanguage [true|false] -> Sets the 'useOneLanguage' option to the given value
│       │
│       └── integer -> Commands with integer argument
│           └── distanceToEntitiesKgi [integer] -> Sets the 'distanceToEntitiesKgi' option to the given value
│
└── description
    ├── commands -> If no argument is given after this one, displays the description for all options
    │   ├── bed -> Displays the description of the '/bed' command
    │   ├── kgi -> Displays the description of the '/kgi' command
    │   └── surface -> Displays the description of the '/surface' command
    │
    └── options
        ├── allow -> Displays the description of the 'allow' options
        ├── minOpLevelExe -> Displays the description of the 'minOpLevelExe' options
        └── other -> Displays the description of the 'other' options
```

## II). Teleportation

- /bed → teleports the player executing the command to its bed or respawn anchor
- /surface → teleports the player executing the command to the highest block on its position

## III). Misc

- /kgi → removes items floating on the ground in the default radius (in chunks), or in the specified radius

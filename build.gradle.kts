plugins {
    id("java")
    id("maven-publish")
    id("fabric-loom") version "1.7-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.+"
    id("me.modmuss50.mod-publish-plugin") version "0.5.+"
}

repositories {
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
    }
    maven("https://maven.terraformersmc.com/releases/") {
        name = "TerraformersMC"
    }
}

object Constants {
    const val MOD_VERSION: String = "0.11.0"
    const val LOADER_VERSION: String = "0.16.2"
    const val CYANLIB_VERSION: String = "0.5.1"
}

class ModData {
    val hasVersionRange = properties.containsKey("range_name")

    val mcVersion = property("minecraft_version").toString()
    val rangedName = if (hasVersionRange) property("range_name").toString() else mcVersion

    private val modrinthVersions = if (hasVersionRange) property("modrinth_versions") else mcVersion
    val versionsList = modrinthVersions.toString().split(" ")
    val min = if (hasVersionRange) versionsList[0] else mcVersion
    val max = if (hasVersionRange) versionsList[versionsList.size - 1] else mcVersion

    val fabricVersion = property("fabric_version").toString()
    val modmenuVersion = property("modmenu_version").toString()
    val cyanlibVersion = "${Constants.CYANLIB_VERSION}+${rangedName}"

    val fullVersion = "${Constants.MOD_VERSION}+${rangedName}"

    val isj21 = mcVersion !in listOf("1.19.4", "1.20.1", "1.20.2", "1.20.4")

    val javaVersion = if (isj21) "21" else "17"
}

val mod = ModData()


// Sets the name of the output jar files
base {
    archivesName = "${rootProject.name}-${mod.fullVersion}"
    group = "fr.aeldit.cyan"
}

dependencies {
    minecraft("com.mojang:minecraft:${mod.mcVersion}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${Constants.LOADER_VERSION}")

    // Fabric API
    for (name in listOf(
        // ModMenu dependencies
        "fabric-resource-loader-v0",
        "fabric-key-binding-api-v1",
        // CyanLib dependencies
        "fabric-command-api-v2",
        "fabric-lifecycle-events-v1",
        "fabric-screen-api-v1",
        // Cyan dependencies
        "fabric-networking-api-v1",
        "fabric-entity-events-v1"
    )) {
        val module = fabricApi.module(name, mod.fabricVersion)
        modImplementation(module)
    }

    // ModMenu
    modImplementation("com.terraformersmc:modmenu:${mod.modmenuVersion}")

    // CyanLib
    val debug = false
    if (debug) {
        modImplementation(files(projectDir.resolve("../../run/mods/cyanlib-0.4.15-beta2+1.21.jar")))
    } else {
        modImplementation("maven.modrinth:cyanlib:${mod.cyanlibVersion}")
        include("maven.modrinth:cyanlib:${mod.cyanlibVersion}")
    }

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")
}

loom {
    runConfigs.all {
        ideConfigGenerated(true) // Run configurations are not created for subprojects by default
        runDir = "../../run" // Use a shared run folder and just create separate worlds
    }
}

java {
    sourceCompatibility = if (mod.isj21) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = if (mod.isj21) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
}

val buildAndCollect = tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${rootProject.name}-${mod.fullVersion}"))
    dependsOn("build")
}

if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
        dependsOn(buildAndCollect)
    }
}

tasks {
    processResources {
        inputs.property("version", mod.fullVersion)
        inputs.property("loader_version", Constants.LOADER_VERSION)
        inputs.property("min", mod.min)
        inputs.property("max", mod.max)
        inputs.property("java_version", mod.javaVersion)

        filesMatching("fabric.mod.json") {
            expand(
                mapOf(
                    "version" to mod.fullVersion,
                    "loader_version" to Constants.LOADER_VERSION,
                    "min" to mod.min,
                    "max" to mod.max,
                    "java_version" to mod.javaVersion
                )
            )
        }
    }

    jar {
        from("LICENSE")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release = if (mod.isj21) 21 else 17
    }
}

publishMods {
    modrinth {
        accessToken = System.getenv("MODRINTH_TOKEN")

        projectId = "zGxxQr33"
        displayName = "[${mod.rangedName}] Cyan ${Constants.MOD_VERSION}"
        version = mod.fullVersion
        type = STABLE

        file = tasks.remapJar.get().archiveFile

        if (mod.hasVersionRange) {
            minecraftVersions.addAll(mod.versionsList)
        } else {
            minecraftVersions.add(mod.mcVersion)
        }
        modLoaders.add("fabric")

        requires("fabric-api", "modmenu")
        embeds("cyanlib")

        changelog = rootProject.file("changelogs/latest.md")
            .takeIf { it.exists() }
            ?.readText()
            ?: "No changelog provided."

        dryRun = true
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))

    projectId.set(rootProject.name)
    if (rootProject.file("README.md").exists()) {
        syncBodyFrom.set(rootProject.file("README.md").readText())
    }

    debugMode = false
}
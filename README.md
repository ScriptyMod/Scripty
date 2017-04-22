# Scripty for Forge

Scripty is a work-in-progress mod that attempts to extend the capabilities of Minecraft's current command blocks system
by adding new versions of them that support various scripting languages.

## Building and testing

#### Windows

* Set up the workspace with `gradlew.bat setupDecompWorkspace`
* Build the mod with `gradlew.bat build shadowJar`
    * The mod JAR will be located in `build/libs` and end with `-all`
* To test the client/server formally, use `gradlew.bat runClient` and `gradlew.bat runServer`

#### Linux and Others

* Set up the workspace with `./gradlew setupDecompWorkspace`
* Build the mod with `./gradlew build shadowJar`
    * The mod JAR will be located in `build/libs` and end with `-all`
* To test the client/server formally, use `./gradlew runClient` and `./gradlew runServer`

---

CI builds can be found [on Bamboo](http://bamboo.gserv.me/browse/SCRPTY-MOD)

Scripty is open-source software released under the MIT license. Please see the [LICENSE file](https://github.com/ScriptyMod/Scripty/blob/master/LICENSE) for details.
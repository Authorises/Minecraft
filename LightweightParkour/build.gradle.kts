import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

tasks {
    jar {
        archiveFileName.set("server.jar")
    }
}

var displayName = "lightweight-lobby"

group = "dev.authorises.lightweightlobby"
version = "1.1.0-SNAPSHOT"

dependencies {
    //implementation("com.github.Minestom:Minestom:85febebd09")
    // use hollowcube instead
    implementation("com.github.hollow-cube:minestom-ce:a9535e5d29")
    implementation("com.github.CatDevz:SlimeLoader:master-SNAPSHOT")
    implementation("org.java-websocket:Java-WebSocket:1.5.3")
    implementation("net.kyori:adventure-text-minimessage:4.13.1")
    implementation("com.github.emortalmc:tnt:latest")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.konghq:unirest-java:3.11.09")
    implementation("io.minio:minio:8.5.2")


    implementation("com.sk89q.worldedit:worldedit-core:7.3.0-SNAPSHOT")
    // for block placing
    implementation("com.github.hollow-cube.common:block-placement:e297e8f999")

}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks {
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes (
                    "Main-Class" to "dev.authorises.lightweightlobby.Server",
                    "Multi-Release" to true
            )
        }
        archiveBaseName.set("lightweight-lobby")
        mergeServiceFiles()
    }

    build { dependsOn(shadowJar) }
}
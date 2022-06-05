plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

application {
    mainClass.set("com.alexsobiek.tinymcp.TinyMCP")
}

repositories {
    mavenCentral();
    maven("https://maven.fabricmc.net")
}

dependencies {
    implementation("cuchaz:enigma:2.1.0")
    implementation("org.ow2.asm:asm:9.3")
}

tasks {
    jar {
        archiveClassifier.set("unshaded")
    }

    shadowJar {
        archiveClassifier.set("shaded")
    }

    build {
        dependsOn(shadowJar)
    }
}
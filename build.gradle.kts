plugins {
    application
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}


java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

group = "com.github.quillmc"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.github.quillmc.tinymcp.CLI")
}

repositories {
    mavenCentral();
    maven("https://maven.fabricmc.net")
    maven("https://jitpack.io")
}

dependencies {
    implementation("cuchaz:enigma:2.1.0")
    implementation("org.ow2.asm:asm:9.3")
    implementation("commons-cli:commons-cli:1.5.0")
    implementation ("net.fabricmc:tiny-remapper:0.8.2")
    implementation("com.github.alexsobiek:async:7cd100b5df");
}

tasks {
    jar {
        archiveClassifier.set("unshaded")
        sourceSets["main"].allSource;
    }

    shadowJar {
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
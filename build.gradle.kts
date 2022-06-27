import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.teamhelios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.triumphteam.dev/snapshots/")
}

dependencies {
    implementation(group = "io.javalin", name = "javalin", version = "4.6.1")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation(group = "org.jline", name = "jline", version = "3.21.0")
    implementation(group = "org.fusesource.jansi", name = "jansi", version = "2.4.0")
    implementation("org.slf4j:slf4j-jdk14:2.0.0-alpha7")
    implementation("org.reflections:reflections:0.10.2")
    implementation("dev.triumphteam:triumph-cmd-core:2.0.0-SNAPSHOT")
    implementation("com.google.guava:guava:31.1-jre")

}

tasks.withType<Jar> {
    dependsOn(":PebbleConfigCreator:copyCreator")
    manifest {
        attributes["Main-Class"] = "dev.teamhelios.stonepowered.StonePowered"
    }
}

tasks.withType<ShadowJar> {
    relocate("dev.triumphteam.cmd", "dev.teamhelios.libs.cmd")
}

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"

}

group = "dev.teamhelios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(group = "io.javalin", name = "javalin", version = "4.6.1")
    implementation("org.apache.logging.log4j:log4j-api:2.17.2")
    implementation("org.apache.logging.log4j:log4j-core:2.17.2")

    implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.2")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation(group = "org.jline", name = "jline", version = "3.21.0")
    implementation(group = "org.fusesource.jansi", name = "jansi", version = "2.4.0")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "dev.teamhelios.stonepowered.StonePowered"
    }
}
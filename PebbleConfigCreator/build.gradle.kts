plugins {
    kotlin("jvm") version "1.7.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "dev.teamhelios.pebbleconfigcreator.PebbleConfigCreator"
    }
}

dependencies {
    implementation("org.apache.logging.log4j:log4j-api:2.17.2")
    implementation("org.apache.logging.log4j:log4j-core:2.17.2")
    implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.2")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
}

tasks.register<Copy>("copyCreator") {
    dependsOn("shadowJar")
    from("build/libs/PebbleConfigCreator-all.jar")
    rename("PebbleConfigCreator-all.jar", "PebbleConfigCreator.jar.temp")
    into(project.parent!!.sourceSets.main.get().resources.srcDirs.first().absolutePath)
}
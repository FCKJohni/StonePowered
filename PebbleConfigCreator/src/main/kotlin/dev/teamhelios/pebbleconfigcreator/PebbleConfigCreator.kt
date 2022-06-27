@file:JvmName("PebbleConfigCreator")

package dev.teamhelios.pebbleconfigcreator

import org.spongepowered.configurate.ConfigurationOptions
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import java.nio.file.Path
import java.util.*
import kotlin.system.exitProcess


fun main() {
    HeliosLogger.info("Pebble Config Creator v1.0.0")
    HeliosLogger.info("Created by Team Helios")
    HeliosLogger.info("")
    HeliosLogger.info("")
    HeliosLogger.info("please enter the Path to the config file [relative to the Creator]")
    val configPath = readln()
    HeliosLogger.info("please enter the name of the Pebble")
    val pebbleName = readln()
    HeliosLogger.info("please enter the command which will be used to start the Pebble")
    val pebbleCommand = readln()
    HeliosLogger.info("should the Pebble be started automatically? [true/false]")
    val autoStart = readln()

    if (autoStart.toBooleanStrictOrNull() == null) {
        HeliosLogger.error("You have to enter a boolean value! Exiting...")
        exitProcess(0)
    }

    val loader: HoconConfigurationLoader = HoconConfigurationLoader
        .builder()
        .defaultOptions { options: ConfigurationOptions ->
            options.serializers { build: TypeSerializerCollection.Builder ->
                build
                    .register(
                        Pebble::class.java,
                        PebbleConfigSerializer.INSTANCE
                    )
            }
        }
        .path(Path.of(configPath).resolve(".pebble.st")).build()
    val config = loader.load()
    val pebble = Pebble(pebbleName, pebbleCommand.split(" "), UUID.randomUUID(), autoStart.toBoolean())
    config.set(pebble);
    loader.save(config)
    HeliosLogger.info("Pebble Config created successfully!")
    exitProcess(0);

}

package dev.teamhelios.stonepowered.pebble;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.pebble.serializers.PebbleConfigSerializer;
import dev.teamhelios.stonepowered.utils.HeliosLogger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PebbleConfigFinder {

    public List<Pebble> findPebbleConfigs() {
        List<Path> configs = new ArrayList<>();
        try (Stream<Path> walkStream = Files.walk(StonePowered.getRootDirectory())) {
            walkStream.filter(p -> p.toFile().isFile()).filter(p -> p.toFile().getName().equalsIgnoreCase(".pebble.st")).forEach(configs::add);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (configs.isEmpty()) {
            HeliosLogger.error("No Pebble config found! Terminating");
            Runtime.getRuntime().exit(0);
        } else {
            HeliosLogger.info("Found " + configs.size() + " Pebble config" + (configs.size() == 1 ? "" : "s!") + "!");
        }
        return handleConfigs(configs);
    }

    public List<Pebble> handleConfigs(List<Path> configs) {
        List<Pebble> result = new ArrayList<>();
        HeliosLogger.info("Handling " + configs.size() + " Pebble config" + (configs.size() == 1 ? "" : "s!") + "!");
        configs.forEach(p -> {
            HoconConfigurationLoader loader = HoconConfigurationLoader
                    .builder()
                    .defaultOptions(options ->
                            options.serializers(build -> build
                                    .register(Pebble.class, PebbleConfigSerializer.INSTANCE)))
                    .path(p).build();
            try {
                CommentedConfigurationNode node = loader.load();
                Pebble pebble = node.get(Pebble.class);
                pebble.setWorkingDir(p.getParent().toFile());
                pebble.setLoader(loader);
                pebble.setNode(node);
                result.add(pebble);
            } catch (ConfigurateException e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }

}


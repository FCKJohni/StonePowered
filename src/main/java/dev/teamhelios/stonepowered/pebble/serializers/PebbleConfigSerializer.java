package dev.teamhelios.stonepowered.pebble.serializers;

import dev.teamhelios.stonepowered.pebble.Pebble;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class PebbleConfigSerializer implements TypeSerializer<Pebble> {

    public static final PebbleConfigSerializer INSTANCE = new PebbleConfigSerializer();

    @Override
    public Pebble deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.virtual()) {
            return null;
        }
        ConfigurationNode pebbleNode = node.node("pebble");
        String name = pebbleNode.node("name").getString();
        List<String> cmd = pebbleNode.node("cmd").getList(String.class);
        UUID uuid = pebbleNode.node("uuid").get(UUID.class, UUID.randomUUID());
        return new Pebble(name, cmd, uuid);
    }

    @Override
    public void serialize(Type type, @Nullable Pebble obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw();
            return;
        }
        ConfigurationNode pebbleNode = node.node("pebble");
        pebbleNode.node("name").set(obj.getName());
        pebbleNode.node("cmd").setList(String.class, obj.getCmd());
        pebbleNode.node("uuid").set(obj.getUuid());
    }
}

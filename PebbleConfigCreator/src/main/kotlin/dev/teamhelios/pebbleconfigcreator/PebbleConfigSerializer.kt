package dev.teamhelios.pebbleconfigcreator

import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import java.util.*

class PebbleConfigSerializer : TypeSerializer<Pebble?> {
    @Throws(SerializationException::class)
    override fun deserialize(type: Type, node: ConfigurationNode): Pebble? {
        if (node.virtual()) {
            return null
        }
        val pebbleNode = node.node("pebble")
        val name = pebbleNode.node("name").getString("")
        val cmd = pebbleNode.node("cmd").getList(String::class.java, ArrayList())
        val uuid = pebbleNode.node("uuid").get(UUID::class.java, UUID.randomUUID())
        val autoRun = pebbleNode.node("autoRun").getBoolean(false)
        return Pebble(name, cmd, uuid, autoRun)
    }

    @Throws(SerializationException::class)
    override fun serialize(type: Type, obj: Pebble?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw()
            return
        }
        val pebbleNode = node.node("pebble")
        pebbleNode.node("name").set(obj.name)
        pebbleNode.node("cmd").setList(String::class.java, obj.cmd)
        pebbleNode.node("uuid").set(obj.uuid)
        pebbleNode.node("autoRun").set(obj.autorun)
    }

    companion object {
        val INSTANCE = PebbleConfigSerializer()
    }
}
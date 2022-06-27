package dev.teamhelios.pebbleconfigcreator

import java.util.UUID

data class Pebble(val name: String, val cmd: List<String>, val uuid: UUID, val autorun: Boolean)

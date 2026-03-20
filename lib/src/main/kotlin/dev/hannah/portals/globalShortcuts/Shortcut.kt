package dev.hannah.portals.globalShortcuts

import org.freedesktop.dbus.types.Variant

class Shortcut(
    description: String,
    preferredTrigger: String
) {
    val shortcut = mapOf("description" to Variant(description), "preferred_trigger" to Variant(preferredTrigger))
}
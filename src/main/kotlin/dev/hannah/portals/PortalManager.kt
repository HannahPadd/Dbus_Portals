package dev.hannah.portals

import dev.hannah.portals.globalShortcuts.GlobalShortcutsHandler
import dev.hannah.portals.globalShortcuts.ShortcutTuple
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.freedesktop.dbus.types.Variant

class PortalManager(
    appID: String
) {
    private val options = mutableMapOf<String, Variant<*>>()
    private val connection: DBusConnection = DBusConnectionBuilder.forSessionBus().build()

    init {
        options["app_id"] = Variant(appID)
    }

    fun globalShortcutsRequest(shortcutsList: MutableList<ShortcutTuple>): GlobalShortcutsHandler {
        val globalShortcutsHandler = GlobalShortcutsHandler(options, shortcutsList, connection)
        globalShortcutsHandler.createSession()
        return globalShortcutsHandler
    }
}
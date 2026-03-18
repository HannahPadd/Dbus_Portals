package dev.hannah.portals

import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.freedesktop.dbus.types.Variant

class PortalManager(
    val appID: String
) {
    private val options = mutableMapOf<String, Variant<*>>()
    private var requestPath: DBusPath
    private val connection: DBusConnection = DBusConnectionBuilder.forSessionBus().build()

    init {
        requestPath = generateRequestPath()
        options["handle_token"] = Variant(appID)
        options["app_id"] = Variant(appID)
    }

    private fun generateRequestPath(): DBusPath {
        val sender = connection.uniqueName.replaceFirst(":", "").replace(".", "_")
        return DBusPath("/org/freedesktop/portal/desktop/request/$sender/$appID")
    }

    fun globalShortcutsRequest(shortcutsList: MutableList<ShortcutTuple>): GlobalShortcutsHandler {
        val globalShortcutsHandler = GlobalShortcutsHandler(appID, options, shortcutsList, connection, requestPath)
        globalShortcutsHandler.createSession()
        return globalShortcutsHandler
    }
}
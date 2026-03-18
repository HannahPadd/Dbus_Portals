package dev.hannah.portals

import APP_ID
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.freedesktop.dbus.types.Variant

class PortalManager(
) {
    private val options = mutableMapOf<String, Variant<*>>()
    private var requestPath: DBusPath
    private val connection: DBusConnection = DBusConnectionBuilder.forSessionBus().build()

    init {
        requestPath = generateRequestPath()
        options["handle_token"] = Variant(APP_ID)
        options["app_id"] = Variant(APP_ID)
    }

    private fun generateRequestPath(): DBusPath {
        val sender = connection.uniqueName.replaceFirst(":", "").replace(".", "_")
        return DBusPath("/org/freedesktop/portal/desktop/request/$sender/$APP_ID")
    }

    fun globalShortcutsRequest(shortcutsList: MutableList<ShortcutTuple>): GlobalShortcutsHandler {
        val globalShortcutsHandler = GlobalShortcutsHandler(options, shortcutsList, connection, requestPath)
        globalShortcutsHandler.createSession()
        return globalShortcutsHandler
    }
}
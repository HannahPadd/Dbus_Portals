package dev.hannah.portals

import dev.hannah.portals.globalShortcuts.GlobalShortcutsHandler
import dev.hannah.portals.globalShortcuts.ShortcutTuple
import dev.hannah.portals.openUri.OpenURIHandler
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.freedesktop.dbus.types.Variant

val DESKTOP = System.getenv("XDG_CURRENT_DESKTOP").uppercase()

class PortalManager(
    appID: String
) {
    private val options = mutableMapOf<String, Variant<*>>()
    private val connection: DBusConnection = DBusConnectionBuilder.forSessionBus().build()

    init {
        options["app_id"] = Variant(appID)
    }

    fun globalShortcutsRequest(shortcutsList: MutableList<ShortcutTuple>): GlobalShortcutsHandler {
        val globalShortcutsHandler = GlobalShortcutsHandler(connection, shortcutsList, options)
        globalShortcutsHandler.createSession()
        return globalShortcutsHandler
    }

    fun openURIRequest(uri: String) {
        val openURIHandler = OpenURIHandler(connection, options)
        openURIHandler.openURI(uri)
    }
    fun openGlobalShortcutsSettings() {
        val settingsURI =
            when (DESKTOP) {
                "GNOME" -> "gnome-control-center:keyboard"
                "KDE" -> "systemsettings:kcm_keys"
                "CINNAMON" -> "cinnamon-settings keyboard"
                "COSMIC" -> "cosmic-settings:shortcuts"
                else -> return
            }
        openURIRequest(settingsURI)
    }
}
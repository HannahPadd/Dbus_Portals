package dev.hannah.portals.globalShortcuts
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.Struct
import org.freedesktop.dbus.annotations.DBusInterfaceName
import org.freedesktop.dbus.annotations.DBusProperty
import org.freedesktop.dbus.annotations.Position
import org.freedesktop.dbus.interfaces.DBusInterface
import org.freedesktop.dbus.messages.DBusSignal
import org.freedesktop.dbus.types.UInt32
import org.freedesktop.dbus.types.UInt64
import org.freedesktop.dbus.types.Variant

@DBusProperty(name = "version", type = UInt32::class, access = DBusProperty.Access.READ)
@DBusInterfaceName("org.freedesktop.portal.GlobalShortcuts")
interface GlobalShortcuts : DBusInterface {
    fun CreateSession(options: Map<String, Variant<*>>): DBusPath

    fun BindShortcuts(
        sessionHandle: DBusPath?,
        shortcuts: MutableList<ShortcutTuple>,
        parentWindow: String,
        options: MutableMap<String, Variant<*>>
    ): DBusPath?

    fun ListShortcuts(sessionHandle: DBusPath?, options: MutableMap<String, Variant<*>>): DBusPath

    class Activated(
        path: String,
        sessionHandle: DBusPath,
        shortcutId: String,
        timestamp: UInt64,
        options: MutableMap<String, Variant<*>>
    ) : DBusSignal(path, sessionHandle, shortcutId, timestamp, options) {
        val sessionHandle: DBusPath
        val shortcutId: String
        val timestamp: UInt64
        val options: MutableMap<String, Variant<*>>

        init {
            this.sessionHandle = sessionHandle
            this.shortcutId = shortcutId
            this.timestamp = timestamp
            this.options = options
        }
    }

    class Deactivated(
        path: String,
        sessionHandle: DBusPath,
        shortcutId: String?,
        timestamp: UInt64?,
        options: MutableMap<String?, Variant<*>?>?
    ) : DBusSignal(path, sessionHandle, shortcutId, timestamp, options) {
        val sessionHandle: DBusPath?
        val shortcutId: String?
        val timestamp: UInt64?
        val options: MutableMap<String?, Variant<*>?>?

        init {
            this.sessionHandle = sessionHandle
            this.shortcutId = shortcutId
            this.timestamp = timestamp
            this.options = options
        }
    }

    class ShortcutsChanged(
        path: String,
        val sessionHandle: DBusPath,
        val shortcuts: List<ShortcutTuple>
    ) : DBusSignal(path, sessionHandle, shortcuts)
}


class ShortcutTuple(
    @Position(0) val id: String,
    @Position(1) val options: Map<String, Variant<*>>
) : Struct()
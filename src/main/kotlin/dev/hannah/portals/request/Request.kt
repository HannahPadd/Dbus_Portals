package dev.hannah.portals.request

import org.freedesktop.dbus.annotations.DBusInterfaceName
import org.freedesktop.dbus.interfaces.DBusInterface
import org.freedesktop.dbus.messages.DBusSignal
import org.freedesktop.dbus.types.UInt32
import org.freedesktop.dbus.types.Variant

@DBusInterfaceName("org.freedesktop.portal.Request")
interface Request : DBusInterface {
    @DBusInterfaceName("org.freedesktop.portal.Request")
    class Response(
        path: String,
        val responseCode: UInt32,
        val results: Map<String, Variant<*>>
    ) : DBusSignal(path, responseCode, results)
}
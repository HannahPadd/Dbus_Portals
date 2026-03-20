package dev.hannah.portals.openUri

import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.FileDescriptor
import org.freedesktop.dbus.annotations.DBusInterfaceName
import org.freedesktop.dbus.annotations.DBusProperty
import org.freedesktop.dbus.interfaces.DBusInterface
import org.freedesktop.dbus.types.UInt32
import org.freedesktop.dbus.types.Variant

@DBusProperty(name = "version", type = UInt32::class, access = DBusProperty.Access.READ)
@DBusInterfaceName("org.freedesktop.portal.OpenURI")
interface OpenURI : DBusInterface {

    fun OpenURI(parentWindow: String, uri: String, options: MutableMap<String, Variant<*>>): DBusPath

    fun OpenFile(parentWindow: String, fd: FileDescriptor, options: MutableMap<String, Variant<*>>): DBusPath

    fun OpenDirectory(parentWindow: String, fd: FileDescriptor, options: MutableMap<String, Variant<*>>): DBusPath

    fun SchemeSupported(scheme: String, options: MutableMap<String, Variant<*>>): Boolean
}

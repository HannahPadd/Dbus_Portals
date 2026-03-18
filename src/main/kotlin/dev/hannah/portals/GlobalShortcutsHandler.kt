package dev.hannah.portals

import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.interfaces.DBusSigHandler
import org.freedesktop.dbus.types.Variant
import java.util.concurrent.CountDownLatch
import kotlin.collections.mutableMapOf

class GlobalShortcutsHandler(
    appID: String,
    val options: MutableMap<String, Variant<*>>,
    val shortcutsList: MutableList<ShortcutTuple>,
    val connection: DBusConnection,
    val requestPath: DBusPath,
) {
    private val expectedRequestPath: String
    private var sessionHandle: DBusPath? = null
    private val sessionReady = CountDownLatch(1)

    private val globalShortcuts: GlobalShortcuts = connection.getRemoteObject(
        "org.freedesktop.portal.Desktop",
        "/org/freedesktop/portal/desktop",
        GlobalShortcuts::class.java
    )

    private val globalShortcutsResponseHandler = DBusSigHandler<Request.Response> { response ->
        println("dev.hannah.portals.Request path: $requestPath")
        println("dev.hannah.portals.Request response path: ${expectedRequestPath}")
        if (response.path != expectedRequestPath) {
            return@DBusSigHandler
        }

        val sessionHandleResponse = response.results["session_handle"]?.value as String
        println("Session Handle Response $sessionHandleResponse")
        sessionHandle = DBusPath(sessionHandleResponse)
        globalShortcuts.BindShortcuts(sessionHandle, shortcutsList, "", mutableMapOf())
        sessionReady.countDown()
    }

    init {
        options["session_handle_token"] = Variant("${appID}${System.currentTimeMillis()}")
        val sender = connection.uniqueName.replaceFirst(":", "").replace(".", "_")
        expectedRequestPath = "/org/freedesktop/portal/desktop/request/$sender/$appID"

        connection.addSigHandler(Request.Response::class.java, globalShortcutsResponseHandler)
    }

    fun createSession() {
        globalShortcuts.CreateSession(options)
        sessionReady.await()
    }

    fun close() {
        connection.disconnect()
    }
}
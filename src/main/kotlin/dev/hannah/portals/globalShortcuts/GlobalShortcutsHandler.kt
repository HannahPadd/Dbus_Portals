package dev.hannah.portals.globalShortcuts

import dev.hannah.portals.request.Request
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.interfaces.DBusSigHandler
import org.freedesktop.dbus.types.Variant
import java.util.UUID
import java.util.concurrent.CountDownLatch

class GlobalShortcutsHandler(
    val options: MutableMap<String, Variant<*>>,
    val shortcutsList: MutableList<ShortcutTuple>,
    val connection: DBusConnection,
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
        if (response.path != expectedRequestPath) {
            return@DBusSigHandler
        }

        val responseCode = response.responseCode.toInt()

        if (responseCode != 0) {
            return@DBusSigHandler
        }

        if (response.results["session_handle"]?.value == null) {
            return@DBusSigHandler
        }
        val sessionHandleResponse = response.results["session_handle"]?.value as String
        sessionHandle = DBusPath(sessionHandleResponse)
        globalShortcuts.BindShortcuts(sessionHandle, shortcutsList, "", options)
        sessionReady.countDown()
    }

    private val shortcutsActivatedHandler = DBusSigHandler<GlobalShortcuts.Activated> { response ->
        if (response.sessionHandle == sessionHandle) {
            println("Shortcut pressed: ${response.shortcutId}")
        }
    }

    init {
        val sender = connection.uniqueName.replaceFirst(":", "").replace(".", "_")
        val handleToken = "req_${UUID.randomUUID().toString().replace("-", "")}"
        val sessionHandleToken = "sess_${UUID.randomUUID().toString().replace("-", "")}"

        options["handle_token"] = Variant(handleToken)
        options["session_handle_token"] = Variant(sessionHandleToken)
        expectedRequestPath = "/org/freedesktop/portal/desktop/request/$sender/$handleToken"
        connection.addSigHandler(Request.Response::class.java, globalShortcutsResponseHandler)
        connection.addSigHandler(GlobalShortcuts.Activated::class.java, shortcutsActivatedHandler)
    }

    fun createSession() {
        globalShortcuts.CreateSession(options)
        sessionReady.await()
    }

    fun close() {
        connection.disconnect()
    }
}
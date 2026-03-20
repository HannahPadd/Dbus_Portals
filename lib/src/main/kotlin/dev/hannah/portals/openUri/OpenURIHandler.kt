package dev.hannah.portals.openUri

import dev.hannah.portals.request.Request
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.interfaces.DBusSigHandler
import org.freedesktop.dbus.types.Variant
import java.util.UUID

class OpenURIHandler(
    connection: DBusConnection,
    val options: MutableMap<String, Variant<*>>,
) {
    private val expectedRequestPath: String
    private var sessionHandle: DBusPath? = null

    private val openUri: OpenURI = connection.getRemoteObject(
        "org.freedesktop.portal.Desktop",
        "/org/freedesktop/portal/desktop",
        OpenURI::class.java
    )

    private val responseHandler = DBusSigHandler<Request.Response> { response ->
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
    }


    init {
        val sender = connection.uniqueName.replaceFirst(":", "").replace(".", "_")
        val handleToken = "req_${UUID.randomUUID().toString().replace("-", "")}"
        val sessionHandleToken = "sess_${UUID.randomUUID().toString().replace("-", "")}"

        options["handle_token"] = Variant(handleToken)
        options["session_handle_token"] = Variant(sessionHandleToken)
        expectedRequestPath = "/org/freedesktop/portal/desktop/request/$sender/$handleToken"
        connection.addSigHandler(Request.Response::class.java, responseHandler)
    }

    fun openURI(uri: String) {
        openUri.OpenURI("", uri, options)
    }


}
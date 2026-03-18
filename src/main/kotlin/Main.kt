import dev.hannah.portals.PortalManager
import dev.hannah.portals.ShortcutTuple
import org.freedesktop.dbus.types.Variant

const val APP_ID = "Slime"

fun main() {
    val portalManager = PortalManager()
    val shortcutsList = mutableListOf(ShortcutTuple(APP_ID, mapOf("description" to Variant("Yaw Reset"))))
    val globalShortcutsHandler = portalManager.globalShortcutsRequest(shortcutsList)

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Closing connection")
        globalShortcutsHandler.close()
    })

    while (true) {
        Thread.sleep(1000)
    }
}
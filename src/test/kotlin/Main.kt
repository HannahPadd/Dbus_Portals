import dev.hannah.portals.PortalManager
import dev.hannah.portals.globalShortcuts.ShortcutTuple
import org.freedesktop.dbus.types.Variant
import kotlin.test.Test

const val APP_ID = "dev.hannah.portals"


fun main() {
    LibraryTest().testCreateShortcut()

    while (true) {
        Thread.sleep(1000)
    }
}

class LibraryTest {

    @Test()
    fun testCreateShortcut() {
        val portalManager = PortalManager(APP_ID)
        val shortcutsList = mutableListOf(ShortcutTuple("YAW_RESET_5", mapOf("description" to Variant("Yaw Reset"))))
        val globalShortcutsHandler = portalManager.globalShortcutsRequest(shortcutsList)

        Runtime.getRuntime().addShutdownHook(Thread {
            println("Closing connection")
            globalShortcutsHandler.close()
        })

        Thread.sleep(10000)

    }
}
import dev.hannah.portals.PortalManager
import dev.hannah.portals.globalShortcuts.ShortcutTuple
import org.freedesktop.dbus.types.Variant
import kotlin.test.Test

const val APP_ID = "dev.hannah.portals"


fun main() {
    LibraryTest().testCreateShortcut()
}

class LibraryTest {

    @Test()
    fun testCreateShortcut() {
        var isRunning = true
        val portalManager = PortalManager(APP_ID)
        val shortcutsList = mutableListOf(
            ShortcutTuple("FULL_RESET", mapOf("description" to Variant("Full Reset"), "trigger_description" to Variant("CTRL+ALT+SHIFT+Y"))),
            ShortcutTuple("YAW_RESET", mapOf("description" to Variant("Yaw Reset"), "trigger_description" to Variant("CTRL+ALT+SHIFT+U"))),
            ShortcutTuple("MOUNTING_RESET", mapOf("description" to Variant("Mounting Reset"), "trigger_description" to Variant("CTRL+ALT+SHIFT+I"))),
            ShortcutTuple("FEET_MOUNTING_RESET", mapOf("description" to Variant("Feet Mounting Reset"), "trigger_description" to Variant("CTRL+ALT+SHIFT+P"))),
            ShortcutTuple("PAUSE_TRACKING", mapOf("description" to Variant("Pause Tracking"), "trigger_description" to Variant("CTRL+ALT+SHIFT+O"))))
        val globalShortcutsHandler = portalManager.globalShortcutsRequest(shortcutsList)
        Runtime.getRuntime().addShutdownHook(Thread {
            println("Closing connection")
            globalShortcutsHandler.close()
        })

         while (isRunning) {
            globalShortcutsHandler.onShortcutActivated = { shortcutId ->
                when (shortcutId) {
                    "FULL_RESET" -> {
                        println("Full reset triggered")
                    }
                    "YAW_RESET" -> {
                        println("Yaw reset triggered")
                    }
                    "MOUNTING_RESET" -> {
                        println("Mounting reset triggered")
                    }
                    "FEET_MOUNTING_RESET" -> {
                        println("Feet mounting reset triggered")
                    }
                    "PAUSE_TRACKING" -> {
                        println("Pause tracking triggered")
                        isRunning = false
                    }
                }
            }
        }
    }
}
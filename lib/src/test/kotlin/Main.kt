import dev.hannah.portals.PortalManager
import dev.hannah.portals.globalShortcuts.ShortcutTuple
import dev.hannah.portals.globalShortcuts.Shortcut
import kotlin.test.Test

const val APP_ID = "dev.hannah.portals"

/*
Options for shortcut include
"description"
"preferred_trigger"
"trigger_description"
 */


class LibraryTest {

    @Test
    fun testCreateShortcut() {
        var isRunning = true
        val portalManager = PortalManager(APP_ID)
        val fullReset = Shortcut("Full Reset", "CTRL+ALT+SHIFT+Y")
        val yawReset = Shortcut("Yaw Reset", "CTRL+ALT+SHIFT+U")
        val mountingReset = Shortcut("Mounting Reset", "CTRL+ALT+SHIFT+I")
        val feetMountingReset = Shortcut("Feet Mounting Reset","CTRL+ALT+SHIFT+P")
        val pauseTracking = Shortcut("Pause Tracking", "CTRL+ALT+SHIFT+O")
        val shortcutsList = mutableListOf(
            ShortcutTuple("FULL_RESET", fullReset.shortcut),
            ShortcutTuple("YAW_RESET", yawReset.shortcut),
            ShortcutTuple("MOUNTING_RESET", mountingReset.shortcut),
            ShortcutTuple("FEET_MOUNTING_RESET", feetMountingReset.shortcut),
            ShortcutTuple("PAUSE_TRACKING", pauseTracking.shortcut))
        val globalShortcutsHandler = portalManager.globalShortcutsRequest(shortcutsList)
        Runtime.getRuntime().addShutdownHook(Thread {
            println("Closing connection")
            globalShortcutsHandler.close()
        })

        portalManager.openGlobalShortcutsSettings()

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

             globalShortcutsHandler.onShortcutsChanged = { shortcuts ->
                 for (shortcut in shortcuts) {
                     println("${shortcut.id} ${shortcut.options.values.first()} ${shortcut.options.values.last()}")
                 }
             }
        }
    }
}
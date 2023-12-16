package cn.buffcow.hyperaodzen

import android.content.Context
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
import fake.android.provider.MiuiSettings
import fake.com.android.systemui.shared.plugins.PluginInstance
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Hooker for systemui while plugin is loading.
 *
 * @author qingyu
 * <p>Create on 2023/12/01 16:05</p>
 */
internal object PluginLoader : YukiBaseHooker() {

    private val aodHooked = AtomicBoolean(false)

    override fun onHook() {
        PluginInstance.CLASS_NAME.toClass().constructor().hookAll {
            after { onPluginLoaded(PluginInstance(instance)) }
        }
    }

    private fun onPluginLoaded(plugin: PluginInstance) {
        when (plugin.componentName) {
            SystemUI.AOD.CMP_AOD_DZON -> {
                if (aodHooked.compareAndSet(false, true)) {
                    hookAodZen(plugin.pluginClassLoader)
                    YLog.info("Plugin for sysui aod hooked.")
                }
            }
        }
    }

    private fun hookAodZen(loader: ClassLoader) {
        "${SystemUI.AOD.PACKAGE_NAME}.AODView".toClass(loader).method {
            name = "scheduleNotificationAnimation"
        }.hook {
            replaceUnit {
                instance.current().field {
                    name = "mContext"
                }.cast<Context>()?.takeIf { ctx ->
                    MiuiSettings.SoundMode.isZenModeOn(ctx)
                } ?: callOriginal()
            }
        }
    }

    fun String.toClassByAppClsLoader() = this.toClass<Any>(appClassLoader)
}

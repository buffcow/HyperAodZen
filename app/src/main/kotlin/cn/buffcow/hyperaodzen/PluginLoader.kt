package cn.buffcow.hyperaodzen

import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
import fake.android.provider.MiuiSettings
import fake.com.android.systemui.shared.plugins.PluginFactory
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
        PluginFactory.CLASS_NAME.toClass().method {
            name = PluginFactory.M_createPluginContext
        }.hook().after {
            val wrapper = result<ContextWrapper>() ?: kotlin.run {
                YLog.error("Failed to create plugin context.")
                return@after
            }
            onPluginLoaded(PluginFactory(instance).componentName, wrapper.classLoader)
        }
    }

    private fun onPluginLoaded(cmp: ComponentName, pluginClsLoader: ClassLoader) {
        when (cmp) {
            SystemUI.AOD.CMP_AOD_DZON -> {
                if (aodHooked.compareAndSet(false, true)) {
                    hookAodZen(pluginClsLoader)
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

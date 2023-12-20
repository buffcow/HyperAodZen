package fake.com.android.systemui.shared.plugins

import android.content.ComponentName
import fake.BaseFaker

/**
 * @author qingyu
 * <p>Create on 2023/12/01 14:58</p>
 */

internal class PluginFactory(factory: Any) : BaseFaker(factory) {

    val componentName: ComponentName get() = fieldAny("mComponentName")

    companion object {
        const val CLASS_NAME = "com.android.systemui.shared.plugins.PluginInstance\$PluginFactory"
        const val M_createPluginContext = "createPluginContext"
    }
}


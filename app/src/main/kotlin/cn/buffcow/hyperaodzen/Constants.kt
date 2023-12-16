/**
 * Objects that stores string constants.
 *
 * @author qingyu
 * <p>Create on 2023/12/01 15:56</p>
 */

package cn.buffcow.hyperaodzen

import android.content.ComponentName

const val LOG_TAG = "HyperAodZen"

object SystemUI {
    const val PACKAGE_NAME = "com.android.systemui"

    object AOD {
        const val PACKAGE_NAME = "com.miui.aod"
        val CMP_AOD_DZON = ComponentName(PACKAGE_NAME, "$PACKAGE_NAME.doze.DozeServicePluginImpl")
    }
}

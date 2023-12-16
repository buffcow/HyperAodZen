package fake.android.provider

import android.content.Context
import fake.BaseStaticFacker

/**
 * @author qingyu
 * <p>Create on 2023/12/16 17:53</p>
 */
internal object MiuiSettings {
    object SoundMode : BaseStaticFacker("android.provider.MiuiSettings\$SoundMode") {
        fun isZenModeOn(context: Context): Boolean = invokeAny("isZenModeOn", context)
    }
}

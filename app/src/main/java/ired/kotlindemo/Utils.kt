package ired.kotlindemo

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by kevin on 16/10/11.
 */
object Utils {

    fun dpTopx(resources: Resources, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics);
    }
}
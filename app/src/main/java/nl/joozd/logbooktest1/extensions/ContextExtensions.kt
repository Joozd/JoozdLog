package nl.joozd.logbooktest1.extensions

import android.content.Context
import androidx.annotation.AttrRes
import android.util.TypedValue

fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}
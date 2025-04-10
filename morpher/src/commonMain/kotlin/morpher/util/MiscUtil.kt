package morpher.util

import androidx.compose.ui.geometry.Offset
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

internal fun getClampedIndex(fraction: Float, maxIndex: Int): Int {
    var toReturn = (maxIndex * fraction).roundToInt()
    if (toReturn > maxIndex) {
        val extra = toReturn % maxIndex
        toReturn = maxIndex - extra
    } else if (toReturn < 0) {
        val extra = toReturn % maxIndex
        toReturn = -extra
    }
    return toReturn
}

internal fun Offset.distanceTo(target: Offset): Float {
    return sqrt((this.x - target.x).pow(2) + (this.y - target.y).pow(2))
}
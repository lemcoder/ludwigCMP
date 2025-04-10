package morpher.model.primitives

import kotlin.math.hypot

/**
 * PointF holds two float coordinates
 */
class PointF(
    var x: Float = 0f,
    var y: Float = 0f
) {

    /**
     * Create a new PointF initialized with the values in the specified
     * PointF (which is left unmodified).
     *
     * @param p The point whose values are copied into the new
     * point.
     */
    constructor(p: PointF): this(p.x, p.y)

    /**
     * Set the point's x and y coordinates
     */
    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    /**
     * Set the point's x and y coordinates to the coordinates of p
     */
    fun set(p: PointF) {
        this.x = p.x
        this.y = p.y
    }

    fun negate() {
        x = -x
        y = -y
    }

    fun offset(dx: Float, dy: Float) {
        x += dx
        y += dy
    }

    /**
     * Returns true if the point's coordinates equal (x,y)
     */
    fun equals(x: Float, y: Float): Boolean {
        return this.x == x && this.y == y
    }

    override fun toString(): String {
        return "PointF($x, $y)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PointF

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }

    companion object {
        /**
         * Returns the euclidian distance from (0,0) to (x,y)
         */
        /**
         * Return the euclidian distance from (0,0) to the point
         */
        fun length(x: Float, y: Float): Float {
            return hypot(x.toDouble(), y.toDouble()).toFloat()
        }
    }
}

inline operator fun PointF.plus(p: PointF): PointF {
    return PointF(x, y).apply { offset(p.x, p.y) }
}

inline operator fun PointF.times(scalar: Float): PointF {
    return PointF(this.x * scalar, this.y * scalar)
}

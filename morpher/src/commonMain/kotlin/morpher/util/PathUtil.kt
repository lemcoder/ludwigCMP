package morpher.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.vector.PathNode
import morpher.model.path.LudwigSubpath
import morpher.model.path.PathSegment
import morpher.model.primitives.PointF
import morpher.model.primitives.plus
import morpher.model.primitives.times
import kotlin.math.abs
import androidx.compose.ui.graphics.PathSegment as ComposePathSegment

internal fun List<ComposePathSegment>.toSubpaths(
    bounds: Rect,
    targetWidth: Float,
    targetHeight: Float,
): List<LudwigSubpath> {

    val scaleFactorX = targetWidth / bounds.width
    val scaleFactorY = targetHeight / bounds.height
    val scaleFactor = minOf(scaleFactorX, scaleFactorY)
    val scaledWidth = scaleFactor * bounds.width
    val scaledHeight = scaleFactor * bounds.height
    val offsetX = ((targetWidth - scaledWidth) / 2) - bounds.left * scaleFactor
    val offsetY = ((targetHeight - scaledHeight) / 2) - bounds.top * scaleFactor
    val offset = PointF(offsetX, offsetY)

    val toReturn = mutableListOf<LudwigSubpath>()
    val currSubpath = mutableListOf<PathSegment>()

    var currPosition = PointF(0f, 0f)
    var area = 0f

    this.forEach { segment ->
        when (segment.type) {
            ComposePathSegment.Type.Move -> {
                // FIXME
                val point = PointF(segment.points[0], segment.points[1])
                val end = point * scaleFactor + offset

                if (currSubpath.isNotEmpty()) {
                    if (currSubpath.first() != currSubpath.last() &&
                        currSubpath.first().startPosition.x.approxEquals(currSubpath.last().endPosition.x) &&
                        currSubpath.first().startPosition.y.approxEquals(currSubpath.last().endPosition.y)
                    ) {
                        val trapezoidArea = (end.x - currPosition.x) * (currPosition.y + end.y) / 2
                        area += trapezoidArea
                        toReturn.add(
                            LudwigSubpath(
                                pathSegments = currSubpath.toList(),
                                bounds = Rect(0f, 0f, targetWidth, targetHeight),
                                isClosed = true,
                                area = area
                            )
                        )
                    } else {
                        toReturn.add(
                            LudwigSubpath(
                                pathSegments = currSubpath.toList(),
                                bounds = Rect(0f, 0f, targetWidth, targetHeight),
                                isClosed = false
                            )
                        )
                    }
                    currSubpath.clear()
                    area = 0f
                }
                currPosition = end
            }

            ComposePathSegment.Type.Line -> {
                // FIXME
                val startPoint = PointF(segment.points[0], segment.points[1])
                val endPoint = PointF(segment.points[2], segment.points[3])

                val start = startPoint * scaleFactor + offset
                val end = endPoint * scaleFactor + offset
                val cp1 = lerp(start, end, 1 / 3f)
                val cp2 = lerp(start, end, 2 / 3f)

                currSubpath.add(
                    PathSegment(
                        startPosition = Offset(start.x, start.y),
                        endPosition = Offset(end.x, end.y),
                        pathNode = PathNode.CurveTo(cp1.x, cp1.y, cp2.x, cp2.y, end.x, end.y)
                    )
                )
                val trapezoidArea = (end.x - start.x) * (start.y + end.y) / 2
                area += trapezoidArea
                currPosition = end
            }

            ComposePathSegment.Type.Quadratic -> {
                // FIXME
                val startPoint = PointF(segment.points[0], segment.points[1])
                val inCpPoint = PointF(segment.points[2], segment.points[3])
                val endPoint = PointF(segment.points[4], segment.points[5])

                val start = startPoint * scaleFactor + offset
                val inCp = inCpPoint * scaleFactor + offset
                val end = endPoint * scaleFactor + offset

                val cp1 = Offset(
                    start.x + 2.0f / 3.0f * (inCp.x - start.x),
                    start.y + 2.0f / 3.0f * (inCp.y - start.y)
                )
                val cp2 = Offset(
                    end.x + 2.0f / 3.0f * (inCp.x - end.x), end.y + 2.0f / 3.0f * (inCp.y - end.y)
                )

                currSubpath.add(
                    PathSegment(
                        startPosition = Offset(start.x, start.y),
                        endPosition = Offset(end.x, end.y),
                        pathNode = PathNode.CurveTo(cp1.x, cp1.y, cp2.x, cp2.y, end.x, end.y)
                    )
                )
                val trapezoidArea = (end.x - start.x) * (start.y + end.y) / 2
                area += trapezoidArea
                currPosition = end
            }

            ComposePathSegment.Type.Conic -> {/* no-op */
            }

            ComposePathSegment.Type.Cubic -> {
                // FIXME
                val startPoint = PointF(segment.points[0], segment.points[1])
                val cp1Point = PointF(segment.points[2], segment.points[3])
                val cp2Point = PointF(segment.points[4], segment.points[5])
                val endPoint = PointF(segment.points[6], segment.points[7])

                val start = startPoint * scaleFactor + offset
                val cp1 = cp1Point * scaleFactor + offset
                val cp2 = cp2Point * scaleFactor + offset
                val end = endPoint * scaleFactor + offset

                currSubpath.add(
                    PathSegment(
                        startPosition = Offset(start.x, start.y),
                        endPosition = Offset(end.x, end.y),
                        pathNode = PathNode.CurveTo(cp1.x, cp1.y, cp2.x, cp2.y, end.x, end.y)
                    )
                )
                val trapezoidArea = (end.x - start.x) * (start.y + end.y) / 2
                area += trapezoidArea
                currPosition = end
            }

            ComposePathSegment.Type.Close -> {
                if (currSubpath.isNotEmpty()) {
                    val end = PointF(
                        currSubpath.first().startPosition.x, currSubpath.first().startPosition.y
                    )
                    val trapezoidArea = (end.x - currPosition.x) * (currPosition.y + end.y) / 2
                    area += trapezoidArea
                    toReturn.add(
                        LudwigSubpath(
                            pathSegments = currSubpath.toList(),
                            bounds = Rect(0f, 0f, targetWidth, targetHeight),
                            isClosed = true,
                            area = area
                        )
                    )
                    currSubpath.clear()
                    area = 0f
                    currPosition = end
                }
            }

            ComposePathSegment.Type.Done -> {
                return@forEach
            }
        }
    }
    if (currSubpath.isNotEmpty()) {
        val end = currSubpath.first().startPosition
        if (currPosition.x.approxEquals(end.x) &&
            currPosition.y.approxEquals(end.y)) {
            val trapezoidArea = (end.x - currPosition.x) * (currPosition.y + end.y) / 2
            area += trapezoidArea
            toReturn.add(
                LudwigSubpath(
                    pathSegments = currSubpath.toList(),
                    bounds = Rect(0f, 0f, targetWidth, targetHeight),
                    isClosed = true,
                    area = area
                )
            )
        } else {
            toReturn.add(
                LudwigSubpath(
                    pathSegments = currSubpath.toList(),
                    bounds = Rect(0f, 0f, targetWidth, targetHeight),
                    isClosed = false
                )
            )
        }
    }
    return toReturn.toList()
}

private fun Float.approxEquals(other: Float, threshold: Float = 0.0001f): Boolean {
    return abs(this - other) <= threshold
}
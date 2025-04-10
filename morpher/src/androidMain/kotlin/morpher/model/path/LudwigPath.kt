package morpher.model.path

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.graphics.vector.PathParser
import morpher.util.toSubpaths

data class LudwigPath(
    val subpaths: List<LudwigSubpath>,
    val width: Float,
    val height: Float
) {
    companion object {
        /**
         * Compose Path
         */
        fun fromPath(
            path: Path,
            targetWidth: Float,
            targetHeight: Float
        ): LudwigPath {
            val bounds = path.getBounds()
            val subpaths = path.iterator().asSequence().toList().toSubpaths(bounds, targetWidth, targetHeight)

            return LudwigPath(subpaths, targetWidth, targetHeight)
        }
    }

    fun toComposePath(): Path {
        return PathParser().addPathNodes(subpaths.map { it.toPathNodes() }
            .flatten()).toPath()
    }
}

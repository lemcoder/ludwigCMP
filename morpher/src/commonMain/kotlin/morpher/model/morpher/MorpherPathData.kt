package morpher.model.morpher

import morpher.model.path.PairedSubpath
import morpher.model.path.UnpairedSubpath

data class MorpherPathData(
    val pairedSubpaths: List<PairedSubpath>,
    val unpairedStartSubpaths: List<UnpairedSubpath>,
    val unpairedEndSubpaths: List<UnpairedSubpath>,
)

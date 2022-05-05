import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*

@Composable
fun Editor(engrams: MutableList<Engram>) {
	LaunchedEffect(Unit) {
		engrams.addAll(Engram.fromWiki(wiki!!.split("\n").map { it.trim() }))
		config!!.split("\n").map { it.trim() }.forEach {
			try {
				val engram = Engram.fromConfig(it)
				when (val existing = engrams.firstOrNull { existingEngram -> existingEngram.code == engram.code }) {
					null -> engrams.add(engram)
					else -> existing.let { existingEngram ->
						existingEngram.epCost = engram.epCost
						existingEngram.requiredLevel = engram.requiredLevel
					}
				}
			} catch (e: Exception) {
				println("Error parsing config: $it")
			}
		}
	}
	Table(engrams)
}
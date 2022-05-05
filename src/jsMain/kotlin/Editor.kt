import androidx.compose.runtime.*
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
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
	var exportedData by remember { mutableStateOf<String?>(null) }
	Button({
		type(ButtonType.Button)
		classes("btn", "btn-primary", "btn-lg", "float-end", "mt-1")
		style {
			backgroundColor(backgroundColor)
			property("border-color", "#09E2DE")
		}
		onClick {
			exportedData = engrams.joinToString("\n") { it.toConfig() }
			js("copyExported()")
			window.setTimeout({
				exportedData = null
			}, 1000)
		}
	})
	{
		Text("Export")
	}
	if (exportedData != null) {
		TextArea(exportedData!!)
		{
			id("exported")
		}
	}
}
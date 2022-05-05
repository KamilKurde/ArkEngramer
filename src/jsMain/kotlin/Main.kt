import androidx.compose.runtime.*
import kotlinx.browser.document
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.asList

var wiki by mutableStateOf<String?>(null)
var config by mutableStateOf<String?>(null)

fun main() {
	document.body?.getElementsByClassName("loading")?.asList()?.forEach { it.remove() }

	val engram = mutableStateListOf<Engram>()

	renderComposable(rootElementId = "root") {
		when
		{
			wiki == null -> WikiTaker()
			config == null -> ConfigTaker()
			else -> Editor(engram)
		}
	}
}


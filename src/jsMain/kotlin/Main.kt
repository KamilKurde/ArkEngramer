import androidx.compose.runtime.*
import kotlinx.browser.document
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.asList

var wiki by mutableStateOf<String?>(null)
var config by mutableStateOf<String?>(null)

var loading by mutableStateOf(false)

val backgroundColor = Color("#05232f")
val textColor = Color("#09E2DE")

fun main() {
	document.body?.getElementsByClassName("loading")?.asList()?.forEach { it.remove() }

	val engram = mutableStateListOf<Engram>()

	renderComposable(rootElementId = "root") {
		if (loading) {
			Img("favicon.png")
			{
				style {
					width(120.px)
					height(120.px)
					property("animation", "spin 2s linear infinite")
					position(Position.Absolute)
					left(50.percent)
					right(50.percent)
					top(50.percent)
					bottom(50.percent)
					property("transform", "translate(-50%,-50%)")
				}
			}
		}
		when
		{
			wiki == null -> WikiTaker()
			config == null -> ConfigTaker()
			else -> Editor(engram)
		}
	}
}


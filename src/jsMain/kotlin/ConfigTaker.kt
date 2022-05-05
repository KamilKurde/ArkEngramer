import androidx.compose.runtime.*
import kotlinx.browser.window

@Composable
fun ConfigTaker()
{
	var value by remember { mutableStateOf("") }
	textTaker("Config", { value = it }){
		loading = true
		window.setTimeout({
			config = value
		}, 100)
	}
}
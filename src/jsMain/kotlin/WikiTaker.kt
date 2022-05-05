import androidx.compose.runtime.*

@Composable
fun WikiTaker() {
	var value by remember { mutableStateOf("") }
	textTaker("Wiki", { value = it }) {
		wiki = value
	}
}
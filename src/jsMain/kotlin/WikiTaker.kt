import androidx.compose.runtime.*

@Composable
fun WikiTaker()
{
	var value by remember { mutableStateOf("") }
	textTaker(value, "Wiki", { value = it }){
		wiki = value
	}
}
import androidx.compose.runtime.*

@Composable
fun ConfigTaker()
{
	var value by remember { mutableStateOf("") }
	textTaker("Config", { value = it }){
		config = value
	}
}
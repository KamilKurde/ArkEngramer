import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun textTaker(label: String, onChange: (String) -> Unit, onClick: () -> Unit) {
	Div(attrs = {
		classes("col-12")
	}) {
		H2({ classes("text-center", "display-1") }) {
			Text("Give me your $label data")
		}
		TextArea(attrs = {
			classes("form-control")
			style {
				property("resize", "none")
				property("border-color", "#09E2DE")
				backgroundColor(backgroundColor)
			}
			rows(10)
			this.onChange {
				onChange(it.value)
			}
		})
		Button({
			type(ButtonType.Button)
			classes("btn", "btn-primary", "btn-lg", "float-end", "mt-1")
			style {
				backgroundColor(backgroundColor)
				property("border-color", "#09E2DE")
			}
			onClick {
				onClick()
			}
		})
		{
			Text("Next")
		}
	}
}
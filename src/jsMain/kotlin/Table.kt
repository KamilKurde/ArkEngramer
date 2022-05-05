import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

private val headers = listOf("Name", "Code", "Index", "EP Cost", "Required Level")

@Composable
fun Table(data: List<Engram>) {
	Table({
		id("table")
		attr("data-show-columns", true.toString())
		classes("table", "table-bordered", "table-hover")
		style {
			border(color = textColor)
		}
	})
	{
		Thead({
			style {
				position(Position.Sticky)
				top(0.px)
				style {
					backgroundColor(backgroundColor)
				}
			}
		}) {
			Tr {
				headers.forEach { header ->
					Th({
						classes("th-inner", "sortable", "both")
						scope(Scope.Col)
					}) {
						Text(header)
					}
				}
			}
		}
		Tbody {
			data.sortedBy { it.requiredLevel }.forEachIndexed { index, engram ->
				Tr({
					attr("data-index", index.toString())
				}) {
					Td {
						Text(engram.name ?: "-")
					}
					Td {
						Text(engram.code)
					}
					Td {
						Text(engram.index?.toString() ?: "-")
					}
					Td {
						Input(InputType.Number) {
							classes("form-control")
							defaultValue(engram.epCost)
							min("0")
							step(1)
							max("100")
							onChange {
								engram.epCost = it.value?.toInt() ?: 0
							}
						}
					}
					Td {
						Input(InputType.Number) {
							classes("form-control")
							defaultValue(engram.requiredLevel)
							min("0")
							step(1)
							max("200")
							onChange {
								engram.requiredLevel = it.value?.toInt() ?: 0
							}
						}
					}
				}
				if (index == data.lastIndex)
				{
					loading = false
				}
			}
		}
	}
}
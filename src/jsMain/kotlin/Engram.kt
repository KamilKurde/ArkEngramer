data class Engram(val name: String?, val code: String, val index: Int?, var epCost: Int, var requiredLevel: Int) {
	companion object {
		fun fromWiki(wikiRow: String): Engram {
			val split = wikiRow.split("||").mapIndexed { index, it ->
				val trimmed = it.trim()
				if (index == 0) trimmed.removeSurrounding(" ") else trimmed.replace(" ", "")
			}.filterNot { it.isEmpty() }
			println("wiki: $wikiRow")
			val name = split[0].removePrefix("| {{ItemLink|").removeSuffix("}}")
			val code = split[1]
			val index = split[2].toInt()
			val epCost = split[3].toInt()
			val requiredLevel = split[4].toInt()
			return Engram(name, code, index, epCost, requiredLevel)
		}

		fun fromWiki(wiki: List<String>): List<Engram> {
			val wiki = wiki.toMutableList()
			wiki.removeAll { it.contains("|-") || it.isEmpty() }
			return wiki.map { Engram.fromWiki(it) }
		}

		fun fromConfig(configRow: String): Engram {
			println("config: $configRow")
			val split = configRow.trim().removePrefix("OverrideNamedEngramEntries=(").removeSuffix(")").split(",")
			val code = split[0].removePrefix("EngramClassName=\"").removeSuffix("\"")
			val epCost = split[2].removePrefix("EngramPointsCost=").toInt()
			val requiredLevel = split[3].removePrefix("EngramLevelRequirement=").toInt()
			return Engram(null, code, null, epCost, requiredLevel)
		}
	}
}
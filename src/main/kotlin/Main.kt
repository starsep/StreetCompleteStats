import com.geodesk.feature.FeatureLibrary
import com.soywiz.korte.TemplateProvider
import com.soywiz.korte.Templates
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis

typealias CityName = String

data class City(
    val name: CityName,
    val slug: String,
)

data class QuestStats(
    val solved: Long,
    val all: Long,
    val ratio: Double,
)

data class OutputData(
    val cities: List<City>,
    val results: Map<CityName, Map<String, QuestStats>>
)

fun generateData(): OutputData {
    val library = FeatureLibrary("data/poland.gol")
    val cities = library.select("a[admin_level=8][population>20000][name!~'(Görlitz|Karviná|Opava|Ostrava|Český Těšín|Trutnov|Zittau|Třinec)']")
    val results = mutableMapOf<CityName, Map<String, QuestStats>>()
    for (city in cities) {
        val cityResult = mutableMapOf<String, QuestStats>()
        for ((questName, quest) in quests) {
            val cityFeatures = library.`in`(city.bounds())
            val allFeatures = quest.allFeatures(cityFeatures)
            val solvedCount = quest.solvedFeatures(allFeatures).count()
            val allCount = allFeatures.count()
            cityResult[questName] = QuestStats(
                solved = solvedCount,
                all = allCount,
                ratio = solvedCount.toDouble() / allCount,
            )
        }
        results[city.tag("name")] = cityResult
    }
    return OutputData(
        cities = cities.map { it.tag("name") }.sorted().map { City(name=it, slug = it.slugify()) },
        results = results,
    )
}


fun main() {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val startTime = LocalDateTime.now().format(formatter)
    var data: OutputData?
    val generationTime = measureTimeMillis {
        data = generateData()
    }

    val outputDir = File("output")
    outputDir.mkdir()
    val iconsSourceDir = File("icons")
    val iconsOutputDir = File(outputDir, "icons")
    iconsOutputDir.mkdir()
    for (quest in quests.values) {
        val iconSource = File(iconsSourceDir, quest.iconFilename)
        if (iconSource.exists()) {
            iconSource.copyTo(File(iconsOutputDir, quest.iconFilename), overwrite = true)
        } else {
            println("Missing icon: $iconSource")
        }
    }
    runBlocking {
        val renderer = Templates(ResourceTemplateProvider("views"), cache = true)
        val output = renderer.render(
            "index.html", mapOf(
                "startTime" to startTime, "generationSeconds" to generationTime / 1000.0,
                "cities" to data!!.cities
            )
        )
        File(outputDir, "index.html").writeText(output)
        for (city in data!!.cities) {
            val cityOutput = renderer.render(
                "city.html", mapOf(
                    "startTime" to startTime,
                    "generationSeconds" to generationTime / 1000.0,
                    "result" to data!!.results[city.name],
                    "cityName" to city.name,
                    "quests" to quests,
                )
            )
            File(outputDir, "${city.slug}.html").writeText(cityOutput)
        }
    }
}

class ResourceTemplateProvider(private val basePath: String) : TemplateProvider {
    override suspend fun get(template: String): String? {
        return this::class.java.classLoader.getResource(Paths.get(basePath, template).toString())?.readText()
    }
}

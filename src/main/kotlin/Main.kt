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
typealias QuestName = String

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
    val questResults: Map<QuestName, Map<CityName, QuestStats>>,
    val cityResults: Map<CityName, Map<QuestName, QuestStats>>,
)

fun generateData(): OutputData {
    val library = FeatureLibrary("data/poland.gol")
    val cities = library
        .select("a[admin_level=8][population>20000][name!~'(Görlitz|Karviná|Opava|Ostrava|Český Těšín|Trutnov|Zittau|Třinec)']")
    val cityResults = mutableMapOf<CityName, Map<QuestName, QuestStats>>()
    val questResults = mutableMapOf<QuestName, MutableMap<CityName, QuestStats>>()
    for (quest in quests.values) questResults[quest.name()] = mutableMapOf()
    for (city in cities) {
        val cityResult = mutableMapOf<QuestName, QuestStats>()
        val cityName = city.tag("name")
        for ((questName, quest) in quests) {
            val cityFeatures = library.`in`(city.bounds())
            val allFeatures = quest.allFeatures(cityFeatures)
            val solvedCount = quest.solvedFeatures(allFeatures).count()
            val allCount = allFeatures.count()
            val questStats = QuestStats(
                solved = solvedCount,
                all = allCount,
                ratio = solvedCount.toDouble() / allCount,
            )
            cityResult[questName] = questStats
            questResults[quest.name()]!![cityName] = questStats
        }
        cityResults[cityName] = cityResult
    }
    return OutputData(
        cities = cities.map { it.tag("name") }.sorted().map { City(name=it, slug = it.slugify()) },
        questResults = questResults,
        cityResults = cityResults,
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
        val commonContext = mapOf(
            "startTime" to startTime,
            "generationSeconds" to generationTime / 1000.0,
            "quests" to quests,
        )
        val renderer = Templates(ResourceTemplateProvider("views"), cache = true)
        val indexOutput = renderer.render(
            "index.html", commonContext + mapOf(
                "cities" to data!!.cities,
            )
        )
        File(outputDir, "index.html").writeText(indexOutput)
        for (city in data!!.cities) {
            val cityOutput = renderer.render(
                "city.html", commonContext + mapOf(
                    "result" to data!!.cityResults[city.name],
                    "cityName" to city.name,
                )
            )
            File(outputDir, "${city.slug}.html").writeText(cityOutput)
        }
        for (quest in quests.values) {
            val questOutput = renderer.render(
                "quest.html", commonContext + mapOf(
                    "quest" to quest,
                    "result" to data!!.questResults[quest.name()]!!.toList().sortedByDescending { it.second.solved },
                )
            )
            File(outputDir, "${quest.name()}.html").writeText(questOutput)
        }
    }
}

class ResourceTemplateProvider(private val basePath: String) : TemplateProvider {
    override suspend fun get(template: String): String? {
        return this::class.java.classLoader.getResource(Paths.get(basePath, template).toString())?.readText()
    }
}

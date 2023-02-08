import com.geodesk.feature.FeatureLibrary
import com.soywiz.korte.TemplateProvider
import com.soywiz.korte.Templates
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis

data class City(
    val name: String,
    val slug: String,
)

data class QuestStats(
    val solved: Long,
    val all: Long,
    val ratio: Double,
)

data class OutputData(
    val cities: List<City>,
    val questResults: Map<Quest, Map<City, QuestStats>>,
    val cityResults: Map<City, Map<Quest, QuestStats>>,
)

fun generateData(): OutputData {
    val library = FeatureLibrary("data/poland.gol")
    val cities = library
        .select("a[admin_level=8][population>20000][name!~'(Görlitz|Karviná|Opava|Ostrava|Český Těšín|Trutnov|Zittau|Třinec)']")
    val cityResults = mutableMapOf<City, Map<Quest, QuestStats>>()
    val questResults = mutableMapOf<Quest, MutableMap<City, QuestStats>>()
    for (quest in quests) questResults[quest] = mutableMapOf()
    for (cityFeature in cities) {
        val city = cityFeature.tag("name").let { City(name=it, slug = it.slugify()) }
        val cityResult = mutableMapOf<Quest, QuestStats>()
        for (quest in quests) {
            val cityFeatures = library.`in`(cityFeature.bounds())
            val allFeatures = quest.allFeatures(cityFeatures)
            val solvedCount = quest.solvedFeatures(allFeatures).count()
            val allCount = allFeatures.count()
            val questStats = QuestStats(
                solved = solvedCount,
                all = allCount,
                ratio = solvedCount.toDouble() / allCount,
            )
            cityResult[quest] = questStats
            questResults[quest]!![city] = questStats
        }
        cityResults[city] = cityResult
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
    for (quest in quests) {
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
                    "result" to data!!.cityResults[city],
                    "city" to city,
                )
            )
            File(outputDir, "${city.slug}.html").writeText(cityOutput)
        }
        for (quest in quests) {
            val questOutput = renderer.render(
                "quest.html", commonContext + mapOf(
                    "quest" to quest,
                    "result" to data!!.questResults[quest]!!.toList().sortedByDescending { it.second.solved },
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

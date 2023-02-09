import com.geodesk.feature.Feature
import com.geodesk.feature.Features

interface Quest {
    fun allFeatures(features: Features<Feature>): Features<Feature>
    fun solvedFeatures(features: Features<Feature>): Features<Feature>
    val iconFilename: String

    fun name() = this::class.simpleName!!
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bike_parking_capacity/AddBikeParkingCapacity.kt
object BikeParkingCapacityQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features.select("nwa[amenity=bicycle_parking][access!~'(private|no)'][bicycle_parking!=floor]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[capacity]")

    override val iconFilename = "ic_quest_bicycle_parking_capacity.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bike_parking_cover/AddBikeParkingCover.kt
object BikeParkingCoverQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features.select("nwa[amenity=bicycle_parking][access!~'(private|no)'][bicycle_parking!~'(shed|lockers|building)']")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[covered]")

    override val iconFilename = "ic_quest_bicycle_parking_cover.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bike_parking_type/AddBikeParkingType.kt
object BikeParkingTypeQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features.select("nwa[amenity=bicycle_parking][access!~'(private|no)']")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[bicycle_parking]")

    override val iconFilename = "ic_quest_bicycle_parking.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bike_rental_capacity/AddBikeRentalCapacity.kt
object BikeRentalCapacityQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features.select("nwa[amenity=bicycle_rental][access!~'(private|no)'][bicycle_rental=docking_station]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[capacity]")

    override val iconFilename = "ic_quest_bicycle_rental_capacity.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bike_rental_type/AddBikeRentalType.kt
object BikeRentalTypeQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features.select("nwa[amenity=bicycle_rental][access!~'(private|no)'][!shop]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[bicycle_rental][bicycle_rental!=yes]")

    override val iconFilename = "ic_quest_bicycle_rental.xml.svg"
}

abstract class BusStopQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features.select("nwa[public_transport=platform],nwa[highway=bus_stop][public_transport!=stop_position][physically_present!=no]")
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bus_stop_bench/AddBenchStatusOnBusStop.kt
object BusStopBenchQuest : BusStopQuest() {
    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[bench~'.*']")

    override val iconFilename = "ic_quest_bench_public_transport.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bus_stop_bin/AddBinStatusOnBusStop.kt
object BusStopBinQuest : BusStopQuest() {
    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[bin~'.*']")

    override val iconFilename = "ic_quest_bin_public_transport.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bus_stop_lit/AddBusStopLit.kt
object BusStopLitQuest : BusStopQuest() {
    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[lit~'.*']")

    override val iconFilename = "ic_quest_bus_stop_lit.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bus_stop_name/AddBusStopName.kt
object BusStopNameQuest : BusStopQuest() {
    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[name],*[noname],*[name:signed]")

    override val iconFilename = "ic_quest_bus_stop_name.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/tree/master/app/src/main/java/de/westnordost/streetcomplete/quests/bus_stop_ref
object BusStopRefQuest : BusStopQuest() {
    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[ref]")

    override val iconFilename = "ic_quest_bus_stop_name.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bus_stop_shelter/AddBusStopShelter.kt
object BusStopShelterQuest : BusStopQuest() {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        super.allFeatures(features).select("*[!covered][indoor!=yes][tunnel!=yes]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[shelter]")

    override val iconFilename = "ic_quest_bus_stop_shelter.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/diet_type/AddHalal.kt
object DietHalalQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features
            .select("na[amenity~'(restaurant|cafe|fast_food|ice_cream|food_court)'][food!=no],na[shop~'(butcher|supermarket|ice_cream|convenience)']")
            .select("*[diet:vegan!=only]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[diet:halal~'.*']")

    override val iconFilename = "ic_quest_halal.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/diet_type/AddKosher.kt
object DietKosherQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select(
            "na[amenity~'(restaurant|cafe|fast_food|ice_cream|food_court)'][food!=no]," +
                    "na[amenity~'(pub|nightclub|biergarten|bar)'][food=yes]," +
                    "na[shop~'(butcher|supermarket|ice_cream|convenience)']"
        )

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[diet:kosher~'.*']")

    override val iconFilename = "ic_quest_kosher.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/diet_type/AddVegan.kt
object DietVeganQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select(
            "na[amenity~'(restaurant|cafe|fast_food|ice_cream|food_court)'][food!=no]," +
                    "na[diet:vegetarian]," +
                    "na[amenity~'(pub|nightclub|biergarten|bar)'][food=yes]"
        )

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[diet:vegan~'.*']")

    override val iconFilename = "ic_quest_restaurant_vegan.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/diet_type/AddVegetarian.kt
object DietVegetarianQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select(
            "na[amenity~'(restaurant|cafe|fast_food|ice_cream|food_court)'][food!=no]," +
                    "na[amenity~'(pub|nightclub|biergarten|bar)'][food=yes]"
        ).select("*[diet:vegan!=only]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[diet:vegetarian~'.*']")

    override val iconFilename = "ic_quest_restaurant_vegetarian.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/building_levels/AddBuildingLevels.kt
object BuildingLevelQuest : Quest {

    private val BUILDINGS_WITH_LEVELS = arrayOf(
        "house", "residential", "apartments", "detached", "terrace", "dormitory", "semi",
        "semidetached_house", "bungalow", "school", "civic", "college", "university", "public",
        "hospital", "kindergarten", "transportation", "train_station", "hotel", "retail",
        "commercial", "office", "manufacture", "parking", "farm",
        "cabin"
    )

    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select("a[building~'(${BUILDINGS_WITH_LEVELS.joinToString("|")})'][!man_made][location!=underground][ruins!=yes]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[building:levels]")

    override val iconFilename = "ic_quest_building_levels.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/building_type/AddBuildingType.kt
object BuildingType : Quest {
    private val forbiddenTags = listOf(
        "man_made",
        "historic",
        "military",
        "power",
        "tourism",
        "attraction",
        "amenity",
        "leisure",
        "aeroway",
        "railway",
        "craft",
        "healthcare",
        "office",
        "shop",
        "description",
        "abandoned",
        "abandoned:building"
    )

    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select("a[building]${forbiddenTags.joinToString("") { "[!$it]" }}[location!=underground][ruins!=yes]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[building!~'(yes|unclassified)']")

    override val iconFilename = "ic_quest_building.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/defibrillator/AddIsDefibrillatorIndoor.kt
object DefibrillatorIndoorQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select("n[emergency=defibrillator][access!~'(private|no)'][!location]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> = features.select("*[indoor~'.*']")

    override val iconFilename = "ic_quest_defibrillator.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/toilet_availability/AddToiletAvailability.kt
object ToiletAvailabilityQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select("na[shop~'(mall|department_store)'],nw[highway~'(services|rest_area)'],nw[tourism~'(camp_site|caravan_site)']")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> = features.select("*[toilets~'.*']")

    override val iconFilename = "ic_quest_toilets.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/toilets_fee/AddToiletsFee.kt
object ToiletsFeeQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select("na[amenity=toilets][access!~'(private|customers)'][!seasonal]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> = features.select("*[fee~'.*']")

    override val iconFilename = "ic_quest_toilet_fee.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/surface/AddRoadSurface.kt
object RoadSurfaceQuest : Quest {
    private val roadHighways = listOf(
        "primary", "primary_link", "secondary", "secondary_link", "tertiary", "tertiary_link",
        "unclassified", "residential", "living_street", "pedestrian", "track"
    )

    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select("w[highway~'(${roadHighways.joinToString("|")})'],w[highway=service][service!~'(driveway|slipway)']")
        .select("*[access!~'(private|no)'],*[foot][foot!=private]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> = features.select("*[surface]")

    override val iconFilename = "ic_quest_street_surface.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/surface/AddSidewalkSurface.kt
object SidewalkSurfaceQuest : Quest {
    private val sidewalkHighways = listOf(
        "motorway",
        "motorway_link",
        "trunk",
        "trunk_link",
        "primary",
        "primary_link",
        "secondary",
        "secondary_link",
        "tertiary",
        "tertiary_link",
        "unclassified",
        "residential",
        "service",
        "living_street"
    )

    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select("w[highway~'(${sidewalkHighways.joinToString("|")})'][area!=yes]")
        .select("*[sidewalk~'(both|left|right)'],*[sidewalk:both=yes],*[sidewalk:left=yes][sidewalk:right~'(yes|no|separate)'],*[sidewalk:right=yes][sidewalk:left~'(yes|no|separate)']")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> = features
        .select("*[sidewalk:both:surface],*[sidewalk:left:surface],*[sidewalk:right:surface],*[sidewalk:surface]")

    override val iconFilename = "ic_quest_sidewalk_surface.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/surface/AddPathSurface.kt
object PathSurfaceQuest : Quest {
    private val sidewalkHighways = listOf("path", "footway", "cycleway", "bridleway", "steps")

    private val SOFT_SURFACES = setOf(
        "ground", "earth", "dirt", "grass", "sand", "mud", "ice", "salt", "snow", "woodchips"
    )

    private val ANYTHING_UNPAVED = SOFT_SURFACES + setOf(
        "unpaved", "compacted", "gravel", "fine_gravel", "pebblestone", "grass_paver",
    )

    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select("w[highway~'(${sidewalkHighways.joinToString("|")})'][segregated!=yes][access!~'(private|no)'][!conveying][!indoor]")
        // TODO: .select("*[~'(path|footway|cycleway|bridleway)'!~'link']")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> = features
        .select("*[surface][surface!~'(${ANYTHING_UNPAVED.joinToString("|")})']")

    override val iconFilename = "ic_quest_way_surface.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/surface/AddCyclewayPartSurface.kt
object CyclewaySurfaceQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select("w[highway=cycleway],w[highway~'(path|footway)'][bicycle],w[highway=bridleway][bicycle~'(designated|yes)']")
        .select("*[segregated=yes][!sidewalk]")
        .select("*[access!~'(private|no)'],*[foot][foot!~'(private|no)'],*[bicycle][bicycle!~'(private|no)']")
        // TODO: .select("*[~'(path|footway|cycleway|bridleway)'!~'link']")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> = features
        .select("*[cycleway:surface][cycleway:surface!~'paved|unpaved'],*[cycleway:surface:note],*[note:cycleway:surface]")

    override val iconFilename = "ic_quest_bicycleway_surface.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/surface/AddFootwayPartSurface.kt
object FootwaySurfaceQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> = features
        .select("w[highway=footway],w[path][foot],w[highway~'(cycleway|bridleway)'][foot]")
        .select("*[segregated=yes][!sidewalk]")
        .select("*[access!~'(private|no)'],*[foot][foot!~'(private|no)']")
        // TODO: .select("*[~'(path|footway|cycleway|bridleway)'!~'link']")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> = features
        .select("*[footway:surface][footway:surface~'(paved|unpaved)'],*[footway:surface:note],*[note:footway:surface]")

    override val iconFilename = "ic_quest_footway_surface.xml.svg"
}

val quests = listOf(
    BikeParkingCapacityQuest, BikeParkingTypeQuest, BikeParkingCoverQuest,
    BikeRentalCapacityQuest, BikeRentalTypeQuest,
    BusStopBenchQuest, BusStopBinQuest, BusStopLitQuest, BusStopNameQuest, BusStopRefQuest, BusStopShelterQuest,
    DietHalalQuest, DietKosherQuest, DietVeganQuest, DietVegetarianQuest,
    BuildingLevelQuest, BuildingType,
    DefibrillatorIndoorQuest,
    ToiletAvailabilityQuest, ToiletsFeeQuest,
    RoadSurfaceQuest, SidewalkSurfaceQuest, PathSurfaceQuest, CyclewaySurfaceQuest, FootwaySurfaceQuest,
)
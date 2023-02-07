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
        features.select("nw[amenity=bicycle_parking][access!~'(private|no)'][bicycle_parking!=floor]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[capacity]")

    override val iconFilename = "ic_quest_bicycle_parking_capacity.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bike_parking_cover/AddBikeParkingCover.kt
object BikeParkingCoverQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features.select("nw[amenity=bicycle_parking][access!~'(private|no)'][bicycle_parking!~'(shed|lockers|building)']")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[covered]")

    override val iconFilename = "ic_quest_bicycle_parking_cover.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bike_parking_type/AddBikeParkingType.kt
object BikeParkingTypeQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features.select("nw[amenity=bicycle_parking][access!~'(private|no)']")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[bicycle_parking]")

    override val iconFilename = "ic_quest_bicycle_parking.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bike_rental_capacity/AddBikeRentalCapacity.kt
object BikeRentalCapacityQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features.select("nw[amenity=bicycle_rental][access!~'(private|no)'][bicycle_rental=docking_station]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[capacity]")

    override val iconFilename = "ic_quest_bicycle_rental_capacity.xml.svg"
}

// https://github.com/streetcomplete/StreetComplete/blob/master/app/src/main/java/de/westnordost/streetcomplete/quests/bike_rental_type/AddBikeRentalType.kt
object BikeRentalTypeQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features.select("nw[amenity=bicycle_rental][access!~'(private|no)'][!shop]")

    override fun solvedFeatures(features: Features<Feature>): Features<Feature> =
        features.select("*[bicycle_rental][bicycle_rental!=yes]")

    override val iconFilename = "ic_quest_bicycle_rental.xml.svg"
}

abstract class BusStopQuest : Quest {
    override fun allFeatures(features: Features<Feature>): Features<Feature> =
        features.select("nw[public_transport=platform],nw[highway=bus_stop][public_transport!=stop_position][physically_present!=no]")
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

val quests: Map<String, Quest> = listOf(
    BikeParkingCapacityQuest, BikeParkingTypeQuest, BikeParkingCoverQuest,
    BikeRentalCapacityQuest, BikeRentalTypeQuest,
    BusStopBenchQuest, BusStopBinQuest, BusStopLitQuest, BusStopNameQuest, BusStopRefQuest, BusStopShelterQuest,
).associateBy(Quest::name)
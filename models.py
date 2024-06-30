from dataclasses import dataclass


@dataclass(frozen=True)
class City:
    name: str
    slug: str


@dataclass(frozen=True)
class QuestStats:
    solved: int
    count: int
    ratio: float


@dataclass(frozen=True)
class Quest:
    iconFilename: str
    name: str
    # TODO: matchingFeatures
    # TODO: solvedFeatures


@dataclass(frozen=True)
class OutputData:
    cities: list[City]
    questResults: dict[Quest, dict[City, QuestStats]]
    cityResults: dict[City, dict[Quest, QuestStats]]

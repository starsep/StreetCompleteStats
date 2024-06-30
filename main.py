import logging
import shutil
from geodesk import Features
from pathlib import Path
from time import localtime, strftime

from quests import quests
from jinja2 import Environment, PackageLoader, StrictUndefined


def generateData() -> OutputData:
    library = Features("data/poland.gol")
    cities = library.select("a[admin_level=8][population>20000][name!~'(Görlitz|Karviná|Opava|Ostrava|Český Těšín|Trutnov|Zittau|Třinec)']")

def main():
    outputDir = Path("output")
    outputDir.mkdir(exist_ok=True)
    iconsSourceDir = Path("icons")
    iconsOutputDir = outputDir / "icons"
    iconsOutputDir.mkdir(exist_ok=True)
    for quest in quests:
        iconSource = iconsSourceDir / quest.iconFilename
        if iconSource.exists():
            shutil.copy(iconSource, iconsOutputDir / quest.iconFilename)
        else:
            logging.error("Missing icon: $iconSource")

    cities = [] # TODO

    startTime = strftime("%Y-%m-%d %H:%M:%S", localtime())
    generationSeconds = 12 # TODO
    environment = Environment(loader=PackageLoader("main", "templates"), undefined=StrictUndefined)
    template = environment.get_template("index.html")
    with (outputDir / "index.html").open("w", encoding="utf-8") as f:
        context = dict(quests=quests, cities=cities, startTime=startTime, generationSeconds=generationSeconds)
        f.write(template.render(context))


if __name__ == "__main__":
    main()

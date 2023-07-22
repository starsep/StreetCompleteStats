[StreetComplete](https://github.com/streetcomplete/StreetComplete) Stats.
Hosted at https://starsep.com/StreetCompleteStats

Inspired by https://streetcompleteness.haukauntrie.de

# Docker
```
docker build -t streetcompletestats .
docker run -it --rm -v "$(pwd)/data:/app/data:ro" -t streetcompletestats
```

